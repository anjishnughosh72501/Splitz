package com.paisede.app

import com.paisede.app.domain.algorithm.ExpenseSplitter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class ExpenseSplitterTest {

    private val splitter = ExpenseSplitter()

    @Test
    fun testSplitEqual_100PaiseAcross3Participants() {
        val splits = splitter.splitEqual("e1", 100L, listOf("A", "B", "C"))
        assertEquals(3, splits.size)
        // Expected remainder of 1 assigned to first participant
        assertEquals(34L, splits[0].amountPaise)
        assertEquals(33L, splits[1].amountPaise)
        assertEquals(33L, splits[2].amountPaise)
        assertEquals(100L, splits.sumOf { it.amountPaise })
    }

    @Test
    fun testSplitEqual_100PaiseAcross4Participants() {
        val splits = splitter.splitEqual("e1", 100L, listOf("A", "B", "C", "D"))
        assertEquals(4, splits.size)
        for (split in splits) {
            assertEquals(25L, split.amountPaise)
        }
        assertEquals(100L, splits.sumOf { it.amountPaise })
    }

    @Test
    fun testSplitEqual_1PaiseAcross3Participants() {
        val splits = splitter.splitEqual("e1", 1L, listOf("A", "B", "C"))
        assertEquals(1L, splits[0].amountPaise)
        assertEquals(0L, splits[1].amountPaise)
        assertEquals(0L, splits[2].amountPaise)
        assertEquals(1L, splits.sumOf { it.amountPaise })
    }

    @Test
    fun testSplitEqual_ZeroAmount() {
        val splits = splitter.splitEqual("e1", 0L, listOf("A", "B", "C"))
        for (split in splits) {
            assertEquals(0L, split.amountPaise)
        }
        assertEquals(0L, splits.sumOf { it.amountPaise })
    }

    @Test
    fun testSplitExact_Valid() {
        val exactMap = mapOf("A" to 50000L, "B" to 30000L, "C" to 20000L)
        val splits = splitter.splitExact("e1", 100000L, exactMap)
        assertEquals(3, splits.size)
        assertEquals(50000L, splits.first { it.userId == "A" }.amountPaise)
        assertEquals(30000L, splits.first { it.userId == "B" }.amountPaise)
        assertEquals(20000L, splits.first { it.userId == "C" }.amountPaise)
        assertEquals(100000L, splits.sumOf { it.amountPaise })
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSplitExact_InvalidSum() {
        val exactMap = mapOf("A" to 50000L, "B" to 30000L, "C" to 10000L) // Sum 90000 != 100000
        splitter.splitExact("e1", 100000L, exactMap)
    }

    @Test
    fun testSplitPercentage_Valid() {
        // 50% = 5000 bp, 25% = 2500 bp, 25% = 2500 bp
        val percentages = mapOf("A" to 5000, "B" to 2500, "C" to 2500)
        val splits = splitter.splitPercentage("e1", 100000L, percentages)
        assertEquals(50000L, splits.first { it.userId == "A" }.amountPaise)
        assertEquals(25000L, splits.first { it.userId == "B" }.amountPaise)
        assertEquals(25000L, splits.first { it.userId == "C" }.amountPaise)
        assertEquals(100000L, splits.sumOf { it.amountPaise })
    }

    @Test
    fun testSplitPercentage_WithRemainderRounding() {
        // 33.33% + 33.33% + 33.34% = 3333 + 3333 + 3334 = 10000 bp
        val percentages = mapOf("A" to 3334, "B" to 3333, "C" to 3333)
        val splits = splitter.splitPercentage("e1", 100L, percentages)
        assertEquals(100L, splits.sumOf { it.amountPaise })
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSplitPercentage_InvalidTotal() {
        val percentages = mapOf("A" to 5000, "B" to 3000) // 8000 bp != 10000 bp
        splitter.splitPercentage("e1", 100000L, percentages)
    }

    @Test
    fun testSplitShares_Valid() {
        // A: 2 shares, B: 1 share, C: 1 share -> total 4 shares
        val shares = mapOf("A" to 2, "B" to 1, "C" to 1)
        val splits = splitter.splitShares("e1", 100000L, shares)
        assertEquals(50000L, splits.first { it.userId == "A" }.amountPaise)
        assertEquals(25000L, splits.first { it.userId == "B" }.amountPaise)
        assertEquals(25000L, splits.first { it.userId == "C" }.amountPaise)
        assertEquals(100000L, splits.sumOf { it.amountPaise })
    }

    @Test
    fun testSplitShares_LargeValue() {
        val shares = mapOf("A" to 3, "B" to 7)
        val amount = 1250000000L // ₹1.25 Crore
        val splits = splitter.splitShares("e1", amount, shares)
        assertEquals(amount, splits.sumOf { it.amountPaise })
    }
}
