package com.paisede.app.domain.algorithm

import com.paisede.app.domain.model.AnalyticsResult
import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.ExpenseSplit

/**
 * Calculates financial analytics strictly using [Long] paise.
 * Never uses floating-point numbers.
 */
class AnalyticsCalculator {

    /**
     * Computes spending metrics from active group expenses.
     */
    fun calculate(
        expenses: List<Pair<Expense, List<ExpenseSplit>>>
    ): AnalyticsResult {
        if (expenses.isEmpty()) {
            return AnalyticsResult()
        }

        var totalSpendingPaise = 0L
        var largestExpensePaise = 0L
        val spendingByPerson = mutableMapOf<String, Long>()
        val spendingByCategory = mutableMapOf<String, Long>()
        val categoryUsageCounts = mutableMapOf<String, Int>()

        for ((expense, splits) in expenses) {
            val amount = expense.amountPaise
            totalSpendingPaise += amount

            if (amount > largestExpensePaise) {
                largestExpensePaise = amount
            }

            // Spending by category
            val category = expense.category ?: "other"
            spendingByCategory[category] = spendingByCategory.getOrDefault(category, 0L) + amount
            categoryUsageCounts[category] = categoryUsageCounts.getOrDefault(category, 0) + 1

            // Spending by person (who actually consumed/incurred the expense via their split share)
            for (split in splits) {
                spendingByPerson[split.userId] =
                    spendingByPerson.getOrDefault(split.userId, 0L) + split.amountPaise
            }
        }

        val expenseCount = expenses.size
        val averageExpensePaise = if (expenseCount > 0) totalSpendingPaise / expenseCount else 0L

        val mostUsedCategory = categoryUsageCounts.maxByOrNull { it.value }?.key
        val largestCategory = spendingByCategory.maxByOrNull { it.value }?.key

        return AnalyticsResult(
            totalSpendingPaise = totalSpendingPaise,
            averageExpensePaise = averageExpensePaise,
            expenseCount = expenseCount,
            largestExpensePaise = largestExpensePaise,
            spendingByPersonPaise = spendingByPerson,
            spendingByCategoryPaise = spendingByCategory,
            mostUsedCategory = mostUsedCategory,
            largestCategory = largestCategory
        )
    }
}
