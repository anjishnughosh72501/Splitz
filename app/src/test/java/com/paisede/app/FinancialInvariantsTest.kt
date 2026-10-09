package com.paisede.app

import com.paisede.app.domain.algorithm.BalanceCalculator
import com.paisede.app.domain.algorithm.DebtSimplifier
import com.paisede.app.domain.algorithm.ExactSettlementSolver
import com.paisede.app.domain.algorithm.ExpenseSplitter
import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.ExpenseSplit
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.model.SettlementTransaction
import com.paisede.app.domain.model.SplitType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Validates core financial and accounting invariants defined in the Splitz Senior Engineering Audit.
 */
class FinancialInvariantsTest {

    private val expenseSplitter = ExpenseSplitter()
    private val balanceCalculator = BalanceCalculator()
    private val debtSimplifier = DebtSimplifier()
    private val exactSolver = ExactSettlementSolver()

    private val testMembers = listOf(
        Member("u1", "g1", "Alice"),
        Member("u2", "g1", "Bob"),
        Member("u3", "g1", "Charlie"),
        Member("u4", "g1", "Diana")
    )

    @Test
    fun testEqualSplit_conservesTotalAmountExactlyEvenWithIndivisibleRemainder() {
        // e.g. 100 paise split 3 ways -> 34, 33, 33 (sum must equal exactly 100)
        val amountPaise = 100L
        val splits = expenseSplitter.splitEqual(
            expenseId = "e1",
            totalAmountPaise = amountPaise,
            participantIds = listOf("u1", "u2", "u3")
        )

        assertEquals(3, splits.size)
        val sumSplits = splits.sumOf { it.amountPaise }
        assertEquals("Sum of equal splits must match original amount exactly to the single paise", amountPaise, sumSplits)
    }

    @Test
    fun testExactSplit_conservesTotalAmount() {
        val amountPaise = 25050L // ₹250.50
        val splits = expenseSplitter.splitExact(
            expenseId = "e2",
            totalAmountPaise = amountPaise,
            exactAmounts = mapOf(
                "u1" to 10000L,
                "u2" to 7525L,
                "u3" to 7525L
            )
        )

        val sumSplits = splits.sumOf { it.amountPaise }
        assertEquals(amountPaise, sumSplits)
    }

    @Test
    fun testPercentageSplit_conservesTotalAmountAndRequiresTenThousandBasisPoints() {
        // 33.33%, 33.33%, 33.34% = 3333 + 3333 + 3334 = 10000 bps
        val totalAmount = 100000L // ₹1000.00
        val bps = mapOf(
            "u1" to 3334,
            "u2" to 3333,
            "u3" to 3333
        )
        val splits = expenseSplitter.splitPercentage(
            expenseId = "e3",
            totalAmountPaise = totalAmount,
            percentagesBp = bps
        )

        val sumSplits = splits.sumOf { it.amountPaise }
        assertEquals(totalAmount, sumSplits)
    }

    @Test
    fun testSharesSplit_conservesTotalAmountWithRoundingRemainder() {
        val totalAmount = 10000L // ₹100.00
        val shares = mapOf(
            "u1" to 1,
            "u2" to 2,
            "u3" to 3
        ) // 1/6, 2/6, 3/6
        val splits = expenseSplitter.splitShares(
            expenseId = "e4",
            totalAmountPaise = totalAmount,
            sharesMap = shares
        )

        val sumSplits = splits.sumOf { it.amountPaise }
        assertEquals(totalAmount, sumSplits)
    }

    @Test
    fun testZeroSumBalanceInvariant_acrossDiverseExpensesAndSettlements() {
        val splits1: List<ExpenseSplit> = expenseSplitter.splitEqual("e1", 120000L, listOf("u1", "u2", "u3", "u4"))
        val splits2: List<ExpenseSplit> = expenseSplitter.splitExact("e2", 35000L, mapOf("u1" to 15000L, "u3" to 20000L))
        val splits3: List<ExpenseSplit> = expenseSplitter.splitShares("e3", 45000L, mapOf("u1" to 1, "u2" to 1, "u4" to 2))

        val expenses: List<Pair<Expense, List<ExpenseSplit>>> = listOf(
            Pair(
                Expense("e1", "g1", "u1", 120000L, "Dinner", "Food", SplitType.EQUAL),
                splits1
            ),
            Pair(
                Expense("e2", "g1", "u2", 35000L, "Uber", "Travel", SplitType.EXACT),
                splits2
            ),
            Pair(
                Expense("e3", "g1", "u4", 45000L, "Groceries", "Shopping", SplitType.SHARES),
                splits3
            )
        )

        val initialBalances = balanceCalculator.calculateNetBalances(testMembers, expenses, emptyList())

        // Invariant: Net balance across all participants must sum to 0
        val netSum = initialBalances.values.sum()
        assertEquals("Sum of all group member net balances must be 0", 0L, netSum)

        // Adding a settlement transaction
        val settlements = listOf(
            SettlementTransaction("u3", "u1", 20000L)
        )
        val postSettlementBalances = balanceCalculator.calculateNetBalances(testMembers, expenses, settlements)

        val postNetSum = postSettlementBalances.values.sum()
        assertEquals("Sum of all group member net balances after settlement must remain 0", 0L, postNetSum)
    }

    @Test
    fun testDebtSimplification_conservesNetBalancesAndZeroesLedger() {
        val initialBalances = mapOf(
            "u1" to 50000L,   // is owed 500
            "u2" to -30000L,  // owes 300
            "u3" to -20000L   // owes 200
        )
        assertEquals(0L, initialBalances.values.sum())

        val txs = debtSimplifier.simplifyGreedy(initialBalances)

        // Verify that applying simplified transactions zeroes all balances
        val simulatedBalances = HashMap(initialBalances)
        for (tx in txs) {
            assertTrue("Transaction amount must be positive", tx.amountPaise > 0L)
            simulatedBalances[tx.fromUserId] = (simulatedBalances[tx.fromUserId] ?: 0L) + tx.amountPaise
            simulatedBalances[tx.toUserId] = (simulatedBalances[tx.toUserId] ?: 0L) - tx.amountPaise
        }

        for (entry in simulatedBalances.entries) {
            assertEquals("User ${entry.key} balance after applying simplified payments must be 0", 0L, entry.value)
        }
    }

    @Test
    fun testExactSettlementSolver_conservesNetBalancesAndNeverExceedsGreedyCount() {
        val balances = mapOf(
            "u1" to 4000L,
            "u2" to 6000L,
            "u3" to -4000L,
            "u4" to -6000L
        )

        val exactTxs = exactSolver.findMinimumTransactions(balances)
        val greedyTxs = debtSimplifier.simplifyGreedy(balances)

        // Exact solver should find 2 transactions (u3->u1: 4000, u4->u2: 6000)
        assertEquals(2, exactTxs.size)
        assertTrue(exactTxs.size <= greedyTxs.size)

        // Test conservation of net balances
        val simulated = HashMap(balances)
        for (tx in exactTxs) {
            simulated[tx.fromUserId] = (simulated[tx.fromUserId] ?: 0L) + tx.amountPaise
            simulated[tx.toUserId] = (simulated[tx.toUserId] ?: 0L) - tx.amountPaise
        }

        for (entry in simulated.entries) {
            assertEquals("User ${entry.key} balance after exact settlement must be 0", 0L, entry.value)
        }
    }
}
