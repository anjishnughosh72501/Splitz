package com.paisede.app

import com.paisede.app.domain.algorithm.DebtSimplifier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class DebtSimplifierTest {

    private val simplifier = DebtSimplifier()

    @Test
    fun testTwoPriorityQueuesOrdering() {
        val balances = mapOf(
            "A" to 50000L,  // Creditor 500
            "B" to -30000L, // Debtor -300
            "C" to 20000L,  // Creditor 200
            "D" to -40000L  // Debtor -400
        )

        val (creditors, debtors) = simplifier.buildHeaps(balances)

        // Creditor heap should poll largest first
        assertEquals("A", creditors.poll()!!.userId)
        assertEquals("C", creditors.poll()!!.userId)

        // Debtor heap should poll largest absolute debt first
        assertEquals("D", debtors.poll()!!.userId)
        assertEquals("B", debtors.poll()!!.userId)
    }

    @Test
    fun testGreedySimplification_Simple3Person() {
        // A = +500, B = -300, C = -200
        val balances = mapOf(
            "A" to 50000L,
            "B" to -30000L,
            "C" to -20000L
        )

        val txs = simplifier.simplifyGreedy(balances)

        // Should produce 2 transactions:
        // Debtor with largest debt (B: 300) pays A: 300
        // Debtor with remaining debt (C: 200) pays A: 200
        assertEquals(2, txs.size)

        for (tx in txs) {
            assertTrue(tx.amountPaise > 0L)
            assertEquals("A", tx.toUserId)
        }

        val totalSettled = txs.sumOf { it.amountPaise }
        assertEquals(50000L, totalSettled)
    }

    @Test
    fun testGreedySimplification_ComplexGroup() {
        // 5 members with zero-sum balances
        val balances = mapOf(
            "1" to 100000L,  // +1000
            "2" to -40000L,  // -400
            "3" to -35000L,  // -350
            "4" to -25000L,  // -250
            "5" to 0L        // Settled
        )

        val txs = simplifier.simplifyGreedy(balances)

        // Number of transactions should be <= non-zero count - 1 = 4 - 1 = 3
        assertTrue(txs.size <= 3)

        // Verify net results of transactions matches original balances
        val simulatedNet = mutableMapOf<String, Long>()
        for (tx in txs) {
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
