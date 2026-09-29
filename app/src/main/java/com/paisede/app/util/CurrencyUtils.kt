package com.paisede.app.util

import kotlin.math.abs

/**
 * Utility for handling Indian Rupee currency operations strictly using [Long] paise.
 * Never uses Float, Double, or BigDecimal for monetary math.
 *
 * 1 Rupee = 100 Paise
 * ₹100 = 10,000 Paise
 */
object CurrencyUtils {

    const val PAISE_PER_RUPEE = 100L

    /**
     * Converts rupees (as integer) to paise.
     */
    fun rupeesToPaise(rupees: Long): Long = rupees * PAISE_PER_RUPEE

    /**
     * Formats an amount in paise to Indian currency string.
     * Examples:
     *  120000L -> "₹1,200"
     *  10000000L -> "₹1,00,000"
     *  125000000L -> "₹12,50,000"
     *  3450L -> "₹34.50"
     *  -45000L -> "-₹450"
     */
    fun formatPaise(
        paise: Long,
        showSign: Boolean = false,
        includeSymbol: Boolean = true,
        alwaysShowDecimals: Boolean = false
    ): String {
        val isNegative = paise < 0
        val absPaise = abs(paise)
        val rupees = absPaise / PAISE_PER_RUPEE
        val remPaise = absPaise % PAISE_PER_RUPEE

        val formattedRupees = formatIndianNumber(rupees)
        val decimalPart = if (alwaysShowDecimals || remPaise != 0L) {
            ".%02d".format(remPaise)
        } else {
            ""
        }

        val prefix = when {
            isNegative -> "-"
            showSign && paise > 0 -> "+"
            else -> ""
        }

        val symbol = if (includeSymbol) "₹" else ""
        return "$prefix$symbol$formattedRupees$decimalPart"
    }

    /**
     * Formats integer number into Indian number grouping format:
     * Last 3 digits grouped together, preceding groups of 2 digits.
     * E.g. 100000 -> 1,00,000
     */
    fun formatIndianNumber(number: Long): String {
        if (number < 1000L) {
            return number.toString()
        }

        val lastThree = (number % 1000L).toString().padStart(3, '0')
        var remaining = number / 1000L
        val parts = mutableListOf<String>()

        while (remaining > 0) {
            val part = remaining % 100L
            remaining /= 100L
            if (remaining > 0) {
                parts.add(part.toString().padStart(2, '0'))
            } else {
                parts.add(part.toString())
            }
        }

        parts.reverse()
        return parts.joinToString(",") + "," + lastThree
    }

    /**
     * Parses user currency string input to paise.
     * Accepts inputs like:
     * "1200", "1200.50", "₹1,200", "1,00,000"
     * Returns null if invalid or negative.
     */
    fun parseCurrencyInput(input: String): Long? {
        val sanitized = input.trim()
            .replace("₹", "")
            .replace(",", "")
            .trim()

        if (sanitized.isEmpty()) return null

        val parts = sanitized.split('.')
        if (parts.size > 2) return null

        val rupeesPart = parts[0]
        if (!rupeesPart.all { it.isDigit() }) return null
        val rupees = if (rupeesPart.isEmpty()) 0L else rupeesPart.toLongOrNull() ?: return null
        if (rupees < 0) return null

        val paise = if (parts.size == 2) {
            val decimalStr = parts[1]
            if (!decimalStr.all { it.isDigit() }) return null
            when (decimalStr.length) {
                0 -> 0L
                1 -> decimalStr.toLong() * 10L
                2 -> decimalStr.toLong()
                else -> return null // More than 2 decimal digits not supported
            }
        } else {
            0L
        }

        return (rupees * PAISE_PER_RUPEE) + paise
    }
}
