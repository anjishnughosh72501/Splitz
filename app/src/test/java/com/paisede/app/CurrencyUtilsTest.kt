package com.paisede.app

import com.paisede.app.util.CurrencyUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CurrencyUtilsTest {

    @Test
    fun testFormatPaise_IndianFormat() {
        assertEquals("₹1,200", CurrencyUtils.formatPaise(120000L))
        assertEquals("₹100", CurrencyUtils.formatPaise(10000L))
        assertEquals("₹1,00,000", CurrencyUtils.formatPaise(10000000L))
        assertEquals("₹12,50,000", CurrencyUtils.formatPaise(125000000L))
        assertEquals("₹0", CurrencyUtils.formatPaise(0L))
        assertEquals("-₹450", CurrencyUtils.formatPaise(-45000L))
        assertEquals("+₹850", CurrencyUtils.formatPaise(85000L, showSign = true))
        assertEquals("₹34.50", CurrencyUtils.formatPaise(3450L))
    }

    @Test
    fun testParseCurrencyInput() {
        assertEquals(120000L, CurrencyUtils.parseCurrencyInput("1200"))
        assertEquals(120000L, CurrencyUtils.parseCurrencyInput("₹1,200"))
        assertEquals(120050L, CurrencyUtils.parseCurrencyInput("1200.50"))
        assertEquals(120050L, CurrencyUtils.parseCurrencyInput("1,200.5"))
        assertEquals(10000000L, CurrencyUtils.parseCurrencyInput("1,00,000"))
        assertEquals(0L, CurrencyUtils.parseCurrencyInput("0"))
        assertNull(CurrencyUtils.parseCurrencyInput(""))
        assertNull(CurrencyUtils.parseCurrencyInput("abc"))
        assertNull(CurrencyUtils.parseCurrencyInput("100.999")) // >2 decimal digits
    }
}
