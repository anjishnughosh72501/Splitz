package com.paisede.app.domain.model

enum class SplitType {
    EQUAL,
    EXACT,
    PERCENTAGE,
    SHARES
}

data class Member(
    val id: String,
    val groupId: String,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)

typealias User = Member

data class Group(
    val id: String,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val currencyCode: String = "INR"
)

data class Expense(
    val id: String,
    val groupId: String,
    val payerId: String,
    val amountPaise: Long,
    val description: String,
    val category: String? = null,
    val splitType: SplitType,
    val createdAt: Long = System.currentTimeMillis()
)

data class ExpenseSplit(
    val expenseId: String,
    val userId: String,
    val amountPaise: Long,
    val percentageBasisPoints: Int? = null,
    val shares: Int? = null
)

data class SettlementTransaction(
    val fromUserId: String,
    val toUserId: String,
    val amountPaise: Long
)

data class Balance(
    val userId: String,
    val amountPaise: Long
)

data class Category(
    val id: String,
    val name: String,
    val iconName: String = "category"
) {
    companion object {
        val DEFAULT_CATEGORIES = listOf(
            Category("food", "Food", "restaurant"),
            Category("travel", "Travel", "directions_car"),
            Category("shopping", "Shopping", "shopping_bag"),
            Category("accommodation", "Accommodation", "hotel"),
            Category("entertainment", "Entertainment", "movie"),
            Category("bills", "Bills", "receipt"),
            Category("other", "Other", "more_horiz")
        )
    }
}

enum class ActionType {
    ADD,
    UNDO,
    REDO
}

data class ExpenseAction(
    val id: String,
    val groupId: String,
    val expenseId: String,
    val actionType: ActionType,
    val createdAt: Long = System.currentTimeMillis()
)

data class SimplificationResult(
    val transactions: List<SettlementTransaction>,
    val greedyTransactionCount: Int,
    val exactTransactionCount: Int? = null,
    val algorithmUsed: String = "Greedy",
    val beforeCount: Int = 0
)

data class AnalyticsResult(
    val totalSpendingPaise: Long = 0L,
    val averageExpensePaise: Long = 0L,
    val expenseCount: Int = 0,
    val largestExpensePaise: Long = 0L,
    val spendingByPersonPaise: Map<String, Long> = emptyMap(),
    val spendingByCategoryPaise: Map<String, Long> = emptyMap(),
    val mostUsedCategory: String? = null,
    val largestCategory: String? = null
)
