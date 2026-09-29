package com.paisede.app.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.paisede.app.data.db.entities.ExpenseActionEntity
import com.paisede.app.data.db.entities.ExpenseEntity
import com.paisede.app.data.db.entities.ExpenseSplitEntity
import com.paisede.app.data.db.entities.GroupEntity
import com.paisede.app.data.db.entities.MemberEntity
import com.paisede.app.data.db.entities.SettlementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM groups ORDER BY createdAt DESC")
    fun getAllGroups(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM groups WHERE id = :groupId")
    suspend fun getGroupById(groupId: String): GroupEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity)

    @Delete
    suspend fun deleteGroup(group: GroupEntity)
}

@Dao
interface MemberDao {
    @Query("SELECT * FROM members WHERE groupId = :groupId ORDER BY createdAt ASC")
    fun getMembersForGroup(groupId: String): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE groupId = :groupId ORDER BY createdAt ASC")
    suspend fun getMembersForGroupSync(groupId: String): List<MemberEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<MemberEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity)
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE groupId = :groupId ORDER BY createdAt DESC")
    fun getExpensesForGroup(groupId: String): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE groupId = :groupId ORDER BY createdAt DESC")
    suspend fun getExpensesForGroupSync(groupId: String): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: String): ExpenseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenseSplits(splits: List<ExpenseSplitEntity>)

    @Query("SELECT * FROM expense_splits WHERE expenseId = :expenseId")
    suspend fun getSplitsForExpense(expenseId: String): List<ExpenseSplitEntity>

    @Query("SELECT * FROM expense_splits WHERE expenseId IN (:expenseIds)")
    suspend fun getSplitsForExpenses(expenseIds: List<String>): List<ExpenseSplitEntity>
}

@Dao
interface SettlementDao {
    @Query("SELECT * FROM settlements WHERE groupId = :groupId ORDER BY createdAt DESC")
    fun getSettlementsForGroup(groupId: String): Flow<List<SettlementEntity>>

    @Query("SELECT * FROM settlements WHERE groupId = :groupId ORDER BY createdAt DESC")
    suspend fun getSettlementsForGroupSync(groupId: String): List<SettlementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettlement(settlement: SettlementEntity)
}

@Dao
interface ExpenseActionDao {
    @Query("SELECT * FROM expense_actions WHERE groupId = :groupId ORDER BY createdAt ASC")
    fun getActionsForGroup(groupId: String): Flow<List<ExpenseActionEntity>>

    @Query("SELECT * FROM expense_actions WHERE groupId = :groupId ORDER BY createdAt ASC")
    suspend fun getActionsForGroupSync(groupId: String): List<ExpenseActionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: ExpenseActionEntity)
}
