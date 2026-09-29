package com.paisede.app.domain.algorithm

import com.paisede.app.domain.model.Balance
import com.paisede.app.domain.model.SettlementTransaction
import java.util.PriorityQueue
import kotlin.math.abs
import kotlin.math.min

/**
 * Simplifies group debts using a 2-PriorityQueue greedy algorithm.
 *
 * Reduces arbitrary debt graphs to at most (N - 1) settlement transactions,
 * where N is the number of participants with non-zero balances.
 *
 * Complexity: O(N log N)
 * Space: O(N)
 */
class DebtSimplifier {

    /**
     * Simplifies debts greedily using two PriorityQueues:
     * 1. Max-heap for creditors (positive balance, largest first)
     * 2. Max-heap for debtors (negative balance, greatest absolute debt first)
     */
    fun simplifyGreedy(balances: Map<String, Long>): List<SettlementTransaction> {
        val (creditorHeap, debtorHeap) = buildHeaps(balances)
        val transactions = mutableListOf<SettlementTransaction>()

        while (creditorHeap.isNotEmpty() && debtorHeap.isNotEmpty()) {
            val creditor = creditorHeap.poll()!!
            val debtor = debtorHeap.poll()!!

            val payment = min(creditor.amountPaise, abs(debtor.amountPaise))

            if (payment > 0L) {
                transactions.add(
                    SettlementTransaction(
                        fromUserId = debtor.userId,
                        toUserId = creditor.userId,
                        amountPaise = payment
                    )
                )
            }

            val remainingCredit = creditor.amountPaise - payment
            val remainingDebt = abs(debtor.amountPaise) - payment

            if (remainingCredit > 0L) {
                creditorHeap.add(Balance(creditor.userId, remainingCredit))
            }
            if (remainingDebt > 0L) {
                debtorHeap.add(Balance(debtor.userId, -remainingDebt))
            }
        }

        // Post-condition validation
        for (tx in transactions) {
            check(tx.amountPaise > 0L) { "Generated zero or negative settlement transaction!" }
            check(tx.fromUserId != tx.toUserId) { "Generated self-settlement transaction!" }
        }

        return transactions
    }

    /**
     * Builds and returns the two PriorityQueues for demonstration and inspection.
     */
    fun buildHeaps(balances: Map<String, Long>): Pair<PriorityQueue<Balance>, PriorityQueue<Balance>> {
        // Max-heap for creditors: largest balance first
        val creditorHeap = PriorityQueue<Balance> { a, b ->
            b.amountPaise.compareTo(a.amountPaise)
        }

        // Max-heap for debtors: greatest debt (magnitude) first
        val debtorHeap = PriorityQueue<Balance> { a, b ->
            abs(b.amountPaise).compareTo(abs(a.amountPaise))
        }

        for ((userId, amount) in balances) {
            when {
                amount > 0L -> creditorHeap.add(Balance(userId, amount))
                amount < 0L -> debtorHeap.add(Balance(userId, amount))
            }
        }

        return Pair(creditorHeap, debtorHeap)
    }
}
