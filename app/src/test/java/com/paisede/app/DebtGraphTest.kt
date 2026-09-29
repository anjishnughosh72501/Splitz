package com.paisede.app

import com.paisede.app.domain.algorithm.DebtGraph
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DebtGraphTest {

    @Test
    fun testAddAndRemoveDebt() {
        val graph = DebtGraph()
        graph.addDebt("A", "B", 50000L)
        assertEquals(50000L, graph.getDebt("A", "B"))

        graph.addDebt("A", "B", 20000L) // Accumulate
        assertEquals(70000L, graph.getDebt("A", "B"))

        graph.removeDebt("A", "B")
        assertEquals(0L, graph.getDebt("A", "B"))
    }

    @Test
    fun testCycleDetection_NoCycle() {
        val graph = DebtGraph()
        // DAG: A -> B -> C
        graph.addDebt("A", "B", 1000L)
        graph.addDebt("B", "C", 2000L)

        assertNull(graph.detectCycle())
    }

    @Test
    fun testCycleDetection_SimpleCycle() {
        val graph = DebtGraph()
        // Cycle: A -> B -> C -> A
        graph.addDebt("A", "B", 50000L)
        graph.addDebt("B", "C", 30000L)
        graph.addDebt("C", "A", 20000L)

        val cycle = graph.detectCycle()
        assertNotNull(cycle)
        assertTrue(cycle!!.size >= 4)
        assertEquals(cycle.first(), cycle.last())
    }

    @Test
    fun testCycleCancellation() {
        val graph = DebtGraph()
        // A -> B ₹500 (50000 paise)
        // B -> C ₹300 (30000 paise)
        // C -> A ₹200 (20000 paise)
        // Min cycle weight = ₹200
        graph.addDebt("A", "B", 50000L)
        graph.addDebt("B", "C", 30000L)
        graph.addDebt("C", "A", 20000L)

        val canceled = graph.cancelAllCycles()
        assertEquals(20000L, canceled)

        // After cancelling ₹200:
        // A -> B should be ₹300 (30000)
        // B -> C should be ₹100 (10000)
        // C -> A should be 0 (edge removed)
        assertEquals(30000L, graph.getDebt("A", "B"))
        assertEquals(10000L, graph.getDebt("B", "C"))
        assertEquals(0L, graph.getDebt("C", "A"))

        // Graph should now be acyclic
        assertNull(graph.detectCycle())
    }
}
