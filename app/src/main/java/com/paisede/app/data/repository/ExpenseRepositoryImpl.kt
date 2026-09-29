package com.paisede.app.data.repository

import com.paisede.app.data.db.AppDatabase
import com.paisede.app.data.db.entities.ExpenseActionEntity
import com.paisede.app.data.db.entities.ExpenseEntity
import com.paisede.app.data.db.entities.ExpenseSplitEntity
import com.paisede.app.domain.algorithm.UndoRedoManager
import com.paisede.app.domain.model.ActionType
import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.ExpenseAction
import com.paisede.app.domain.model.ExpenseSplit
import com.paisede.app.domain.model.SplitType
import com.paisede.app.domain.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ExpenseRepositoryImpl(
    private val database: AppDatabase,
    private val undoRedoManager: UndoRedoManager = UndoRedoManager()
) : ExpenseRepository {

    private val expenseDao = database.expenseDao()
    private val actionDao = database.expenseActionDao()

    override fun getExpenses(groupId: String): Flow<List<Pair<Expense, List<ExpenseSplit>>>> {
        return expenseDao.getExpensesForGroup(groupId).map { expenses ->
            withContext(Dispatchers.IO) {
                expenses.map { expEntity ->
                    val splits = expenseDao.getSplitsForExpense(expEntity.id).map { it.toDomain() }
                    Pair(expEntity.toDomain(), splits)
                }
            }
        }
    }

    override fun getActiveExpenses(groupId: String): Flow<List<Pair<Expense, List<ExpenseSplit>>>> {
        return combine(
            expenseDao.getExpensesForGroup(groupId),
            actionDao.getActionsForGroup(groupId)
        ) { expenses, actions ->
            withContext(Dispatchers.IO) {
                undoRedoManager.loadFromHistory(groupId, actions.map { it.toDomain() })
                val activeIds = undoRedoManager.getActiveExpenseIds(groupId)
                val activeEntities = expenses.filter { it.id in activeIds }

                activeEntities.map { expEntity ->
                    val splits = expenseDao.getSplitsForExpense(expEntity.id).map { it.toDomain() }
                    Pair(expEntity.toDomain(), splits)
                }
            }
        }
    }

    override suspend fun getActiveExpensesSync(groupId: String): List<Pair<Expense, List<ExpenseSplit>>> =
        withContext(Dispatchers.IO) {
            val actions = actionDao.getActionsForGroupSync(groupId).map { it.toDomain() }
            undoRedoManager.loadFromHistory(groupId, actions)
            val activeIds = undoRedoManager.getActiveExpenseIds(groupId)
            val allExpenses = expenseDao.getExpensesForGroupSync(groupId)

            allExpenses.filter { it.id in activeIds }.map { expEntity ->
                val splits = expenseDao.getSplitsForExpense(expEntity.id).map { it.toDomain() }
                Pair(expEntity.toDomain(), splits)
            }
        }

    override suspend fun addExpense(expense: Expense, splits: List<ExpenseSplit>) = withContext(Dispatchers.IO) {
        val expEntity = expense.toEntity()
        val splitEntities = splits.map { it.toEntity() }
        val action = undoRedoManager.recordExpenseAddition(expense.groupId, expense.id)
        val actionEntity = action.toEntity()

        database.runInTransaction {
            kotlinx.coroutines.runBlocking {
                expenseDao.insertExpense(expEntity)
                expenseDao.insertExpenseSplits(splitEntities)
                actionDao.insertAction(actionEntity)
            }
        }
    }

    override suspend fun undoExpense(groupId: String): Boolean = withContext(Dispatchers.IO) {
        // Ensure state is up to date
        val actions = actionDao.getActionsForGroupSync(groupId).map { it.toDomain() }
        undoRedoManager.loadFromHistory(groupId, actions)

        val undoAction = undoRedoManager.undo(groupId) ?: return@withContext false
        actionDao.insertAction(undoAction.toEntity())
        true
    }

    override suspend fun redoExpense(groupId: String): Boolean = withContext(Dispatchers.IO) {
        // Ensure state is up to date
        val actions = actionDao.getActionsForGroupSync(groupId).map { it.toDomain() }
        undoRedoManager.loadFromHistory(groupId, actions)

        val redoAction = undoRedoManager.redo(groupId) ?: return@withContext false
        actionDao.insertAction(redoAction.toEntity())
        true
    }

    override fun canUndo(groupId: String): Flow<Boolean> {
        return actionDao.getActionsForGroup(groupId).map { actions ->
            undoRedoManager.loadFromHistory(groupId, actions.map { it.toDomain() })
            undoRedoManager.canUndo(groupId)
        }
    }

    override fun canRedo(groupId: String): Flow<Boolean> {
        return actionDao.getActionsForGroup(groupId).map { actions ->
            undoRedoManager.loadFromHistory(groupId, actions.map { it.toDomain() })
            undoRedoManager.canRedo(groupId)
        }
    }

    override fun getActions(groupId: String): Flow<List<ExpenseAction>> {
        return actionDao.getActionsForGroup(groupId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // Mappers
    private fun ExpenseEntity.toDomain(): Expense = Expense(
        id = id,
        groupId = groupId,
        payerId = payerId,
        amountPaise = amountPaise,
        description = description,
        category = category,
        splitType = SplitType.valueOf(splitType),
        createdAt = createdAt
    )

    private fun Expense.toEntity(): ExpenseEntity = ExpenseEntity(
        id = id,
        groupId = groupId,
        payerId = payerId,
        amountPaise = amountPaise,
        description = description,
        category = category,
        splitType = splitType.name,
        createdAt = createdAt
    )

    private fun ExpenseSplitEntity.toDomain(): ExpenseSplit = ExpenseSplit(
        expenseId = expenseId,
        userId = userId,
        amountPaise = amountPaise,
        percentageBasisPoints = percentageBasisPoints,
        shares = shares
    )

    private fun ExpenseSplit.toEntity(): ExpenseSplitEntity = ExpenseSplitEntity(
        expenseId = expenseId,
        userId = userId,
        amountPaise = amountPaise,
        percentageBasisPoints = percentageBasisPoints,
        shares = shares
    )

    private fun ExpenseActionEntity.toDomain(): ExpenseAction = ExpenseAction(
        id = id,
        groupId = groupId,
        expenseId = expenseId,
        actionType = ActionType.valueOf(actionType),
        createdAt = createdAt
    )

    private fun ExpenseAction.toEntity(): ExpenseActionEntity = ExpenseActionEntity(
        id = id,
        groupId = groupId,
        expenseId = expenseId,
        actionType = actionType.name,
        createdAt = createdAt
    )
}
