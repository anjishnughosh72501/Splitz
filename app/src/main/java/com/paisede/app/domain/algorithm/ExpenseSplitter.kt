package com.paisede.app.domain.algorithm

import com.paisede.app.domain.model.ExpenseSplit

/**
 * Handles deterministic, integer-only splitting of expenses across participants.
 * All internal math uses Long paise and integer basis points (100% = 10,000 bp).
 * Guarantees that sum(splitAmounts) == totalAmountPaise at all times.
 */
class ExpenseSplitter {

    companion object {
        const val TOTAL_BASIS_POINTS = 10000
    }

    /**
     * Splits an expense equally among participants.
     * Any remainder paise is distributed deterministically (+1 paise) to the first N participants.
     */
    fun splitEqual(
        expenseId: String,
        totalAmountPaise: Long,
        participantIds: List<String>
    ): List<ExpenseSplit> {
        require(participantIds.isNotEmpty()) { "Participant list cannot be empty" }
        require(totalAmountPaise >= 0) { "Total amount cannot be negative" }

        val n = participantIds.size
        val baseShare = totalAmountPaise / n
        val remainder = (totalAmountPaise % n).toInt()

        return participantIds.mapIndexed { index, userId ->
            val extra = if (index < remainder) 1L else 0L
            ExpenseSplit(
                expenseId = expenseId,
                userId = userId,
                amountPaise = baseShare + extra,
                percentageBasisPoints = null,
                shares = null
            )
        }
    }

    /**
     * Splits an expense by exact specified amounts.
     * Validates that sum(exactAmounts) == totalAmountPaise.
     */
    fun splitExact(
        expenseId: String,
        totalAmountPaise: Long,
        exactAmounts: Map<String, Long>
    ): List<ExpenseSplit> {
        require(exactAmounts.isNotEmpty()) { "Participants map cannot be empty" }
        require(totalAmountPaise >= 0) { "Total amount cannot be negative" }

        var sum = 0L
        for ((userId, amount) in exactAmounts) {
            require(amount >= 0) { "Participant $userId amount cannot be negative: $amount" }
            sum += amount
        }

        require(sum == totalAmountPaise) {
            "Sum of exact splits ($sum paise) does not equal total amount ($totalAmountPaise paise)"
        }

        return exactAmounts.map { (userId, amount) ->
            ExpenseSplit(
                expenseId = expenseId,
                userId = userId,
                amountPaise = amount,
                percentageBasisPoints = null,
                shares = null
            )
        }
    }

    /**
     * Splits an expense by percentage basis points (100% = 10,000 bp).
     * Validates that sum(basisPoints) == 10000.
     * Remainder paise from integer division is distributed deterministically to the first candidates.
     */
    fun splitPercentage(
        expenseId: String,
        totalAmountPaise: Long,
        percentagesBp: Map<String, Int>
    ): List<ExpenseSplit> {
        require(percentagesBp.isNotEmpty()) { "Percentages map cannot be empty" }
        require(totalAmountPaise >= 0) { "Total amount cannot be negative" }

        var totalBp = 0
        for ((userId, bp) in percentagesBp) {
            require(bp >= 0) { "Participant $userId percentage cannot be negative: $bp" }
            totalBp += bp
        }

        require(totalBp == TOTAL_BASIS_POINTS) {
            "Sum of percentage basis points ($totalBp) must equal $TOTAL_BASIS_POINTS (100%)"
        }

        val entries = percentagesBp.entries.toList()
        val calculatedAmounts = LongArray(entries.size)
        var sumCalculated = 0L

        for (i in entries.indices) {
            val bp = entries[i].value
            val share = (totalAmountPaise * bp) / TOTAL_BASIS_POINTS
            calculatedAmounts[i] = share
            sumCalculated += share
        }

        var remainder = (totalAmountPaise - sumCalculated).toInt()
        var idx = 0
        while (remainder > 0 && idx < entries.size) {
            if (entries[idx].value > 0) {
                calculatedAmounts[idx] += 1L
                remainder--
            }
            idx++
        }

        return entries.mapIndexed { i, entry ->
            ExpenseSplit(
                expenseId = expenseId,
                userId = entry.key,
                amountPaise = calculatedAmounts[i],
                percentageBasisPoints = entry.value,
                shares = null
            )
        }
    }

    /**
     * Splits an expense by integer shares.
     * Validates that total shares > 0.
     * Remainder paise from integer division is distributed deterministically.
     */
    fun splitShares(
        expenseId: String,
        totalAmountPaise: Long,
        sharesMap: Map<String, Int>
    ): List<ExpenseSplit> {
        require(sharesMap.isNotEmpty()) { "Shares map cannot be empty" }
        require(totalAmountPaise >= 0) { "Total amount cannot be negative" }

        var totalShares = 0
        for ((userId, shares) in sharesMap) {
            require(shares >= 0) { "Participant $userId shares cannot be negative: $shares" }
            totalShares += shares
        }

        require(totalShares > 0) { "Total shares must be greater than 0" }

        val entries = sharesMap.entries.toList()
        val calculatedAmounts = LongArray(entries.size)
        var sumCalculated = 0L

        for (i in entries.indices) {
            val shares = entries[i].value
            val share = if (totalShares > 0) (totalAmountPaise * shares) / totalShares else 0L
            calculatedAmounts[i] = share
            sumCalculated += share
        }

        var remainder = (totalAmountPaise - sumCalculated).toInt()
        var idx = 0
        while (remainder > 0 && idx < entries.size) {
            if (entries[idx].value > 0) {
                calculatedAmounts[idx] += 1L
                remainder--
            }
            idx++
        }

        return entries.mapIndexed { i, entry ->
            ExpenseSplit(
                expenseId = expenseId,
                userId = entry.key,
                amountPaise = calculatedAmounts[i],
                percentageBasisPoints = null,
                shares = entry.value
            )
        }
    }
}
