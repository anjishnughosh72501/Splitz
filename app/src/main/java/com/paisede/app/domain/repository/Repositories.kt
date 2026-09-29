package com.paisede.app.domain.repository

import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.ExpenseAction
import com.paisede.app.domain.model.ExpenseSplit
import com.paisede.app.domain.model.Group
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.model.SettlementTransaction
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun getAllGroups(): Flow<List<Group>>
    suspend fun getGroupById(groupId: String): Group?
    suspend fun createGroup(name: String, memberNames: List<String>): Group
    suspend fun deleteGroup(groupId: String)
    fun getMembers(groupId: String): Flow<List<Member>>
    suspend fun getMembersSync(groupId: String): List<Member>
    suspend fun addMember(groupId: String, name: String): Member
}

interface ExpenseRepository {
    fun getExpenses(groupId: String): Flow<List<Pair<Expense, List<ExpenseSplit>>>>
    fun getActiveExpenses(groupId: String): Flow<List<Pair<Expense, List<ExpenseSplit>>>>
    suspend fun getActiveExpensesSync(groupId: String): List<Pair<Expense, List<ExpenseSplit>>>
    suspend fun addExpense(expense: Expense, splits: List<ExpenseSplit>)
    suspend fun undoExpense(groupId: String): Boolean
    suspend fun redoExpense(groupId: String): Boolean
    fun canUndo(groupId: String): Flow<Boolean>
    fun canRedo(groupId: String): Flow<Boolean>
    fun getActions(groupId: String): Flow<List<ExpenseAction>>
}

interface SettlementRepository {
    fun getSettlements(groupId: String): Flow<List<SettlementTransaction>>
    suspend fun getSettlementsSync(groupId: String): List<SettlementTransaction>
    suspend fun recordSettlement(groupId: String, fromUserId: String, toUserId: String, amountPaise: Long)
}
