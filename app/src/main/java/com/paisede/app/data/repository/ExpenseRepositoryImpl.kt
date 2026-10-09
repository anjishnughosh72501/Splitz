package com.paisede.app.data.repository

import androidx.room.withTransaction
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
import java.util.UUID

class ExpenseRepositoryImpl(
    private val database: AppDatabase,
    private val undoRedoManager: UndoRedoManager = UndoRedoManager()
) : ExpenseRepository {

    private val expenseDao = database.expenseDao()
    private val actionDao = database.expenseActionDao()

    private suspend fun mapExpensesWithSplits(
        expenses: List<ExpenseEntity>
    ): List<Pair<Expense, List<ExpenseSplit>>> {
        if (expenses.isEmpty()) return emptyList()
        val expenseIds = expenses.map { it.id }
        val splitsByExpense = expenseDao.getSplitsForExpenses(expenseIds).groupBy { it.expenseId }
        return expenses.map { expEntity ->
            val splits = splitsByExpense[expEntity.id].orEmpty().map { it.toDomain() }
            Pair(expEntity.toDomain(), splits)
        }
    }

    override fun getExpenses(groupId: String): Flow<List<Pair<Expense, List<ExpenseSplit>>>> {
        return expenseDao.getExpensesForGroup(groupId).map { expenses ->
            withContext(Dispatchers.IO) {
                mapExpensesWithSplits(expenses)
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
                mapExpensesWithSplits(activeEntities)
            }
        }
    }

    override suspend fun getActiveExpensesSync(groupId: String): List<Pair<Expense, List<ExpenseSplit>>> =
        withContext(Dispatchers.IO) {
            val actions = actionDao.getActionsForGroupSync(groupId).map { it.toDomain() }
            undoRedoManager.loadFromHistory(groupId, actions)
            val activeIds = undoRedoManager.getActiveExpenseIds(groupId)
            val allExpenses = expenseDao.getExpensesForGroupSync(groupId)
            val activeEntities = allExpenses.filter { it.id in activeIds }
            mapExpensesWithSplits(activeEntities)
        }

    override suspend fun addExpense(expense: Expense, splits: List<ExpenseSplit>): Unit = withContext(Dispatchers.IO) {
        val expEntity = expense.toEntity()
        val splitEntities = splits.map { it.toEntity() }
        val action = ExpenseAction(
            id = UUID.randomUUID().toString(),
            groupId = expense.groupId,
            expenseId = expense.id,
            actionType = ActionType.ADD,
            createdAt = System.currentTimeMillis()
        )
        val actionEntity = action.toEntity()

        database.withTransaction {
            expenseDao.insertExpense(expEntity)
            expenseDao.insertExpenseSplits(splitEntities)
            actionDao.insertAction(actionEntity)
        }

        // Update in-memory stack only after successful database transaction
        undoRedoManager.recordExpenseAddition(expense.groupId, expense.id)
        Unit
    }

    override suspend fun undoExpense(groupId: String): Boolean = withContext(Dispatchers.IO) {
        database.withTransaction {
            val actions = actionDao.getActionsForGroupSync(groupId).map { it.toDomain() }
            undoRedoManager.loadFromHistory(groupId, actions)

            val undoAction = undoRedoManager.undo(groupId) ?: return@withTransaction false
            actionDao.insertAction(undoAction.toEntity())
            true
        }
    }

    override suspend fun redoExpense(groupId: String): Boolean = withContext(Dispatchers.IO) {
        database.withTransaction {
            val actions = actionDao.getActionsForGroupSync(groupId).map { it.toDomain() }
            undoRedoManager.loadFromHistory(groupId, actions)

            val redoAction = undoRedoManager.redo(groupId) ?: return@withTransaction false
            actionDao.insertAction(redoAction.toEntity())
            true
        }
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
