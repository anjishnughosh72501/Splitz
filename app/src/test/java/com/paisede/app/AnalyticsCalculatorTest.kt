package com.paisede.app

import com.paisede.app.domain.algorithm.AnalyticsCalculator
import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.ExpenseSplit
import com.paisede.app.domain.model.SplitType
import org.junit.Assert.assertEquals
import org.junit.Test

class AnalyticsCalculatorTest {

    private val calculator = AnalyticsCalculator()

    @Test
    fun testAnalyticsCalculation() {
        val exp1 = Expense("e1", "g1", "1", 120000L, "Dinner", "food", SplitType.EQUAL)
        val splits1 = listOf(
            ExpenseSplit("e1", "1", 60000L),
            ExpenseSplit("e1", "2", 60000L)
        )

        val exp2 = Expense("e2", "g1", "2", 40000L, "Cab", "travel", SplitType.EQUAL)
        val splits2 = listOf(
            ExpenseSplit("e2", "1", 10000L),
            ExpenseSplit("e2", "2", 30000L)
        )

        val result = calculator.calculate(listOf(Pair(exp1, splits1), Pair(exp2, splits2)))

        assertEquals(160000L, result.totalSpendingPaise)
        assertEquals(80000L, result.averageExpensePaise)
        assertEquals(2, result.expenseCount)
        assertEquals(120000L, result.largestExpensePaise)

        // Spending by person
        // 1: 60000 + 10000 = 70000
        // 2: 60000 + 30000 = 90000
        assertEquals(70000L, result.spendingByPersonPaise["1"])
        assertEquals(90000L, result.spendingByPersonPaise["2"])

        // Categories
        assertEquals(120000L, result.spendingByCategoryPaise["food"])
        assertEquals(40000L, result.spendingByCategoryPaise["travel"])
        assertEquals("food", result.largestCategory)
    }
}
