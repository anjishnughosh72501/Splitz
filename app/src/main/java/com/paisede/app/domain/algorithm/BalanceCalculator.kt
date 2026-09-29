package com.paisede.app.domain.algorithm

import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.ExpenseSplit
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.model.SettlementTransaction

/**
 * Calculates net balances for all members in a group.
 *
 * Balance convention:
 * - Positive (> 0): Creditor. The person is owed money and should receive it.
 * - Negative (< 0): Debtor. The person owes money and should pay it.
 * - Zero (= 0): Settled up.
 *
 * Invariant: Sum of all net balances in a closed group must equal 0.
 */
class BalanceCalculator {

    /**
     * Calculates the net balance of each user using an internal [HashMap].
     *
     * @param members Group members to initialize in the balance map
     * @param expenses List of active expenses and their splits
     * @param settlements List of completed settlements
     * @return Map of userId to net balance in paise
     */
    fun calculateNetBalances(
        members: List<Member>,
        expenses: List<Pair<Expense, List<ExpenseSplit>>>,
        settlements: List<SettlementTransaction>
    ): Map<String, Long> {
        val balanceMap = HashMap<String, Long>()

        // Initialize all known members to 0 balance
        for (member in members) {
            balanceMap[member.id] = 0L
        }

        // Process expenses
        for ((expense, splits) in expenses) {
            // Payer paid the total amount upfront -> positive credit
            val currentPayerBalance = balanceMap.getOrDefault(expense.payerId, 0L)
            balanceMap[expense.payerId] = currentPayerBalance + expense.amountPaise

            // Each participant owes their share -> debit
            for (split in splits) {
                val currentPartBalance = balanceMap.getOrDefault(split.userId, 0L)
                balanceMap[split.userId] = currentPartBalance - split.amountPaise
            }
        }

        // Process settlements
        // fromUserId (debtor) paid amountPaise to toUserId (creditor)
        // Debtor's debt is reduced (+amount), Creditor's credit is satisfied (-amount)
        for (settlement in settlements) {
            val fromBal = balanceMap.getOrDefault(settlement.fromUserId, 0L)
            balanceMap[settlement.fromUserId] = fromBal + settlement.amountPaise

            val toBal = balanceMap.getOrDefault(settlement.toUserId, 0L)
            balanceMap[settlement.toUserId] = toBal - settlement.amountPaise
        }

        // Validate the zero-sum invariant
        validateZeroSum(balanceMap)

        return balanceMap
    }

    /**
     * Validates that the sum of all net balances in the group is strictly zero.
     * Throws IllegalStateException if violated.
     */
    fun validateZeroSum(balances: Map<String, Long>) {
        var sum = 0L
        for ((_, bal) in balances) {
            sum += bal
        }
        check(sum == 0L) {
            "Zero-sum invariant violated! Sum of net balances = $sum paise. Balances: $balances"
        }
    }
}
