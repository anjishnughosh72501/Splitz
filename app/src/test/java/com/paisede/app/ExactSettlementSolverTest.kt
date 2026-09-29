package com.paisede.app

import com.paisede.app.domain.algorithm.DebtSimplifier
import com.paisede.app.domain.algorithm.ExactSettlementSolver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExactSettlementSolverTest {

    private val exactSolver = ExactSettlementSolver()
    private val greedySimplifier = DebtSimplifier()

    @Test
    fun testExactSolver_CaseWhereGreedyEqualsExact() {
        // A: +50, B: -30, C: -20
        val balances = mapOf("A" to 50L, "B" to -30L, "C" to -20L)
        val exactTxs = exactSolver.findMinimumTransactions(balances)
        val greedyTxs = greedySimplifier.simplifyGreedy(balances)

        assertEquals(2, exactTxs.size)
        assertEquals(2, greedyTxs.size)
    }

    @Test
    fun testExactSolver_ClassicTwoSubsetsWhereGreedyMayDiffer() {
        // Two independent zero-sum groups:
        // Group 1: A (+10), B (-10) -> 1 transaction
        // Group 2: C (+20), D (-20) -> 1 transaction
        // Total exact minimum = 2 transactions.
        val balances = mapOf(
            "A" to 10L,
            "B" to -10L,
            "C" to 20L,
            "D" to -20L
        )

        val exactTxs = exactSolver.findMinimumTransactions(balances)
        val greedyTxs = greedySimplifier.simplifyGreedy(balances)

        // Exact solver should detect the two disjoint subsets and use 2 transactions
        assertEquals(2, exactTxs.size)
        assertTrue(exactTxs.size <= greedyTxs.size)
    }

    @Test
    fun testExactSolver_NeverWorseThanGreedy() {
        // 6 people
        val balances = mapOf(
            "1" to 100L,
            "2" to 50L,
            "3" to -60L,
            "4" to -40L,
            "5" to -50L,
            "6" to 0L
        )

        val exactTxs = exactSolver.findMinimumTransactions(balances)
        val greedyTxs = greedySimplifier.simplifyGreedy(balances)

        assertTrue(exactTxs.size <= greedyTxs.size)

        // Verify all debts settled properly
        val simulatedNet = mutableMapOf<String, Long>()
        for (tx in exactTxs) {
            simulatedNet[tx.fromUserId] = simulatedNet.getOrDefault(tx.fromUserId, 0L) - tx.amountPaise
            simulatedNet[tx.toUserId] = simulatedNet.getOrDefault(tx.toUserId, 0L) + tx.amountPaise
        }
        for ((userId, expected) in balances) {
            if (expected != 0L) {
                assertEquals(expected, simulatedNet[userId])
            }
        }
    }
}
