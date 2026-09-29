package com.paisede.app.domain.algorithm

import com.paisede.app.domain.model.SettlementTransaction

/**
 * Computes the exact minimum number of transactions needed to settle all debts.
 *
 * Background:
 * The optimal debt simplification problem is equivalent to partitioning the set
 * of balances into the maximum number of disjoint subsets that each sum to zero.
 * If N participants with non-zero balances can be partitioned into S disjoint
 * zero-sum subsets, the exact minimum number of transactions is:
 *
 *     Min Transactions = N - S
 *
 * Since the subset-sum problem is NP-hard, this solver is restricted to groups
 * with at most [MAX_EXACT_PARTICIPANTS] (8) non-zero balances.
 */
class ExactSettlementSolver(
    private val greedySimplifier: DebtSimplifier = DebtSimplifier()
) {

    companion object {
        const val MAX_EXACT_PARTICIPANTS = 8
    }

    /**
     * Solves for the exact minimum transactions.
     *
     * @param balances Map of user ID to net balance in paise
     * @return List of settlement transactions achieving the exact minimum count
     * @throws IllegalArgumentException if non-zero balances count exceeds [MAX_EXACT_PARTICIPANTS]
     */
    fun findMinimumTransactions(balances: Map<String, Long>): List<SettlementTransaction> {
        // Filter out zero balances
        val activeEntries = balances.filterValues { it != 0L }.toList()
        val n = activeEntries.size

        if (n == 0) return emptyList()

        if (n > MAX_EXACT_PARTICIPANTS) {
            throw IllegalArgumentException(
                "Exact solver cannot be run on $n non-zero balances (maximum allowed: $MAX_EXACT_PARTICIPANTS)"
            )
        }

        // 1. Compute zero-sum mask table for all 2^N subsets
        val numSubsets = 1 shl n
        val isZeroSum = BooleanArray(numSubsets)

        for (mask in 1 until numSubsets) {
            var sum = 0L
            for (i in 0 until n) {
                if ((mask and (1 shl i)) != 0) {
                    sum += activeEntries[i].second
                }
            }
            if (sum == 0L) {
                isZeroSum[mask] = true
            }
        }

        // 2. Dynamic programming with branch-and-bound to find maximum number of disjoint zero-sum subsets
        // dp[mask] = max number of disjoint zero-sum subsets that partition mask
        // parent[mask] = the sub-mask that was peeled off to achieve dp[mask]
        val dp = IntArray(numSubsets) { -1 }
        val parent = IntArray(numSubsets) { 0 }
        dp[0] = 0

        for (mask in 1 until numSubsets) {
            // If the whole mask itself cannot sum to 0 and has no zero-sum sub-components,
            // we search over its zero-sum submasks
            var submask = (mask - 1) and mask
            while (submask > 0) {
                if (isZeroSum[submask] && dp[mask xor submask] != -1) {
                    val candidate = dp[mask xor submask] + 1
                    if (candidate > dp[mask]) {
                        dp[mask] = candidate
                        parent[mask] = submask
                    }
                }
                submask = (submask - 1) and mask
            }

            if (isZeroSum[mask] && dp[mask] < 1) {
                dp[mask] = 1
                parent[mask] = mask
            }
        }

        val fullMask = (1 shl n) - 1
        val maxZeroSumSubsets = if (dp[fullMask] > 0) dp[fullMask] else 1

        // 3. Extract the partitioned subsets
        val partitionedSubsets = mutableListOf<List<Pair<String, Long>>>()
        var currentMask = fullMask

        while (currentMask > 0 && parent[currentMask] > 0) {
            val sub = parent[currentMask]
            val subsetMembers = mutableListOf<Pair<String, Long>>()
            for (i in 0 until n) {
                if ((sub and (1 shl i)) != 0) {
                    subsetMembers.add(activeEntries[i])
                }
            }
            partitionedSubsets.add(subsetMembers)
            currentMask = currentMask xor sub
        }

        if (currentMask > 0) {
            // Remaining elements form the last component
            val remaining = mutableListOf<Pair<String, Long>>()
            for (i in 0 until n) {
                if ((currentMask and (1 shl i)) != 0) {
                    remaining.add(activeEntries[i])
                }
            }
            partitionedSubsets.add(remaining)
        }

        // 4. Solve each independent zero-sum subset greedily
        val exactTransactions = mutableListOf<SettlementTransaction>()
        for (subset in partitionedSubsets) {
            val subsetMap = subset.toMap()
            exactTransactions.addAll(greedySimplifier.simplifyGreedy(subsetMap))
        }

        // Safety check: exact solution should never be worse than direct greedy
        val directGreedy = greedySimplifier.simplifyGreedy(balances)
        return if (exactTransactions.size <= directGreedy.size) {
            exactTransactions
        } else {
            directGreedy
        }
    }
}
