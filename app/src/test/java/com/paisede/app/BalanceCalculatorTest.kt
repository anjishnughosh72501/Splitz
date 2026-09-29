package com.paisede.app

import com.paisede.app.domain.algorithm.BalanceCalculator
import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.ExpenseSplit
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.model.SettlementTransaction
import com.paisede.app.domain.model.SplitType
import org.junit.Assert.assertEquals
import org.junit.Test

class BalanceCalculatorTest {

    private val calculator = BalanceCalculator()
    private val members = listOf(
        Member("1", "g1", "Ali"),
        Member("2", "g1", "Maya"),
        Member("3", "g1", "Rahul"),
        Member("4", "g1", "Sara")
    )

    @Test
    fun testSingleExpense_EqualSplit() {
        // Ali pays 120000 paise (₹1200) for all 4 (30000 paise each)
        val expense = Expense("e1", "g1", "1", 120000L, "Dinner", "food", SplitType.EQUAL)
        val splits = listOf(
            ExpenseSplit("e1", "1", 30000L),
            ExpenseSplit("e1", "2", 30000L),
            ExpenseSplit("e1", "3", 30000L),
            ExpenseSplit("e1", "4", 30000L)
        )

        val balances = calculator.calculateNetBalances(members, listOf(Pair(expense, splits)), emptyList())

        // Ali: +120000 - 30000 = +90000
        assertEquals(90000L, balances["1"])
        assertEquals(-30000L, balances["2"])
        assertEquals(-30000L, balances["3"])
        assertEquals(-30000L, balances["4"])

        // Invariant check
        assertEquals(0L, balances.values.sum())
    }

    @Test
    fun testMultipleExpensesAndMultiplePayers() {
        // Expense 1: Ali pays ₹1200 (120000) for all 4 (₹300 each)
        val exp1 = Expense("e1", "g1", "1", 120000L, "Dinner", "food", SplitType.EQUAL)
        val splits1 = listOf(
            ExpenseSplit("e1", "1", 30000L),
            ExpenseSplit("e1", "2", 30000L),
            ExpenseSplit("e1", "3", 30000L),
            ExpenseSplit("e1", "4", 30000L)
        )

        // Expense 2: Maya pays ₹400 (40000) for Ali and Maya (₹200 each)
        val exp2 = Expense("e2", "g1", "2", 40000L, "Taxi", "travel", SplitType.EQUAL)
        val splits2 = listOf(
            ExpenseSplit("e2", "1", 20000L),
            ExpenseSplit("e2", "2", 20000L)
        )

        val balances = calculator.calculateNetBalances(
            members,
            listOf(Pair(exp1, splits1), Pair(exp2, splits2)),
            emptyList()
        )

        // Ali: +90000 - 20000 = +70000
        // Maya: -30000 + 40000 - 20000 = -10000
        // Rahul: -30000
        // Sara: -30000
        assertEquals(70000L, balances["1"])
        assertEquals(-10000L, balances["2"])
        assertEquals(-30000L, balances["3"])
        assertEquals(-30000L, balances["4"])
        assertEquals(0L, balances.values.sum())
    }

    @Test
    fun testSettlementPayment() {
        // Maya owes 30000, Ali is owed 30000
        val exp1 = Expense("e1", "g1", "1", 60000L, "Movie", "entertainment", SplitType.EQUAL)
        val splits1 = listOf(
            ExpenseSplit("e1", "1", 30000L),
            ExpenseSplit("e1", "2", 30000L)
        )

        // Maya pays Ali 30000
        val settlement = SettlementTransaction("2", "1", 30000L)

        val balances = calculator.calculateNetBalances(
            members.take(2),
            listOf(Pair(exp1, splits1)),
            listOf(settlement)
        )

        // Both should now be exactly 0
        assertEquals(0L, balances["1"])
        assertEquals(0L, balances["2"])
        assertEquals(0L, balances.values.sum())
    }
}
