package com.paisede.app.util

import com.paisede.app.data.preferences.AppCurrency
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

/**
 * Utility for handling multi-currency operations strictly using [Long] subunit/paise values.
 * Never uses Float, Double, or BigDecimal for monetary math.
 *
 * 1 Major Unit = 100 Minor Units (Paise / Cents / Pence)
 * E.g., ₹100 = 10,000 Paise; $100 = 10,000 Cents
 */
object CurrencyUtils {

    const val PAISE_PER_RUPEE = 100L
    const val SUBUNITS_PER_UNIT = 100L

    /**
     * Converts major units (as integer) to subunits (paise/cents).
     */
    fun rupeesToPaise(rupees: Long): Long = rupees * SUBUNITS_PER_UNIT

    /**
     * Formats an amount in subunits (paise/cents) to localized currency string.
     * By default uses Indian Rupee format if no currency is passed (backward compatible).
     * Examples:
     *  120000L -> "₹1,200" / "$1,200"
     *  10000000L -> "₹1,00,000" / "$100,000"
     *  125000000L -> "₹12,50,000" / "$1,250,000"
     *  3450L -> "₹34.50" / "$34.50"
     *  -45000L -> "-₹450" / "-$450"
     */
    fun formatPaise(
        paise: Long,
        showSign: Boolean = false,
        includeSymbol: Boolean = true,
        alwaysShowDecimals: Boolean = false,
        currency: AppCurrency = AppCurrency.INR
    ): String {
        val isNegative = paise < 0
        val absPaise = abs(paise)
        val units = absPaise / SUBUNITS_PER_UNIT
        val remSubunits = absPaise % SUBUNITS_PER_UNIT

        val formattedUnits = if (currency.isIndianNumbering) {
            formatIndianNumber(units)
        } else {
            formatInternationalNumber(units)
        }

        val decimalPart = if (alwaysShowDecimals || remSubunits != 0L) {
            ".%02d".format(remSubunits)
        } else {
            ""
        }

        val prefix = when {
            isNegative -> "-"
            showSign && paise > 0 -> "+"
            else -> ""
        }

        val symbol = if (includeSymbol) currency.symbol else ""
        return "$prefix$symbol$formattedUnits$decimalPart"
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
     * Standard 3-digit comma grouping (US/International standard)
     * E.g. 100000 -> 100,000
     */
    fun formatInternationalNumber(number: Long): String {
        return NumberFormat.getIntegerInstance(Locale.US).format(number)
    }

    /**
     * Parses user currency string input to subunits (paise).
     * Accepts inputs like:
     * "1200", "1200.50", "₹1,200", "$1,200", "1,00,000"
     * Returns null if invalid or negative.
     */
    fun parseCurrencyInput(input: String, currency: AppCurrency? = null): Long? {
        var sanitized = input.trim()
        if (currency != null) {
            sanitized = sanitized.replace(currency.symbol, "")
        }
        // Also strip common symbols defensively
        sanitized = sanitized
            .replace("₹", "")
            .replace("$", "")
            .replace("€", "")
            .replace("£", "")
            .replace("¥", "")
            .replace("د.إ", "")
            .replace(",", "")
            .trim()

        if (sanitized.isEmpty()) return null

        val parts = sanitized.split('.')
        if (parts.size > 2) return null

        val unitsPart = parts[0]
        if (!unitsPart.all { it.isDigit() }) return null
        val units = if (unitsPart.isEmpty()) 0L else unitsPart.toLongOrNull() ?: return null
        if (units < 0) return null

        val subunits = if (parts.size == 2) {
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

        return (units * SUBUNITS_PER_UNIT) + subunits
    }
}
