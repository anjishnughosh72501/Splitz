package com.paisede.app.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val id: String,
    val name: String,
    val createdAt: Long,
    val currencyCode: String = "INR"
)

@Entity(
    tableName = "members",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("groupId")]
)
data class MemberEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val name: String,
    val createdAt: Long
)

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("groupId"), Index("payerId")]
)
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val payerId: String,
    val amountPaise: Long,
    val description: String,
    val category: String?,
    val splitType: String,
    val createdAt: Long
)

@Entity(
    tableName = "expense_splits",
    foreignKeys = [
        ForeignKey(
            entity = ExpenseEntity::class,
            parentColumns = ["id"],
            childColumns = ["expenseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("expenseId"), Index("userId")]
)
data class ExpenseSplitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val expenseId: String,
    val userId: String,
    val amountPaise: Long,
    val percentageBasisPoints: Int?,
    val shares: Int?
)

@Entity(
    tableName = "settlements",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("groupId"), Index("fromUserId"), Index("toUserId")]
)
data class SettlementEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val fromUserId: String,
    val toUserId: String,
    val amountPaise: Long,
    val createdAt: Long
)

@Entity(
    tableName = "expense_actions",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("groupId"), Index("expenseId")]
)
data class ExpenseActionEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val expenseId: String,
    val actionType: String, // "ADD", "UNDO", "REDO"
    val createdAt: Long
)
