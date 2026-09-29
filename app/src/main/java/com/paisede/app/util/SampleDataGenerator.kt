package com.paisede.app.util

import com.paisede.app.domain.algorithm.ExpenseSplitter
import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.SplitType
import com.paisede.app.domain.repository.ExpenseRepository
import com.paisede.app.domain.repository.GroupRepository
import java.util.UUID

object SampleDataGenerator {

    suspend fun populateSampleData(
        groupRepo: GroupRepository,
        expenseRepo: ExpenseRepository
    ): String {
        // 1. Create Group "Goa Trip"
        val group = groupRepo.createGroup(
            name = "Goa Trip (Sample Data)",
            memberNames = listOf("Ali", "Maya", "Rahul", "Sara")
        )

        val members = groupRepo.getMembersSync(group.id)
        val ali = members.first { it.name == "Ali" }
        val maya = members.first { it.name == "Maya" }
        val rahul = members.first { it.name == "Rahul" }
        val sara = members.first { it.name == "Sara" }
        val allMemberIds = listOf(ali.id, maya.id, rahul.id, sara.id)

        val splitter = ExpenseSplitter()

        // Expense 1: Hotel ₹4,000 paid by Ali (Equal)
        val exp1Id = UUID.randomUUID().toString()
        val exp1 = Expense(
            id = exp1Id,
            groupId = group.id,
            payerId = ali.id,
            amountPaise = 400000L,
            description = "Hotel Stay",
            category = "accommodation",
            splitType = SplitType.EQUAL,
            createdAt = System.currentTimeMillis() - 86400000L * 3
        )
        val splits1 = splitter.splitEqual(exp1Id, 400000L, allMemberIds)
        expenseRepo.addExpense(exp1, splits1)

        // Expense 2: Dinner ₹1,200 paid by Maya (Equal)
        val exp2Id = UUID.randomUUID().toString()
        val exp2 = Expense(
            id = exp2Id,
            groupId = group.id,
            payerId = maya.id,
            amountPaise = 120000L,
            description = "Seafood Dinner",
            category = "food",
            splitType = SplitType.EQUAL,
            createdAt = System.currentTimeMillis() - 86400000L * 2
        )
        val splits2 = splitter.splitEqual(exp2Id, 120000L, allMemberIds)
        expenseRepo.addExpense(exp2, splits2)

        // Expense 3: Taxi ₹600 paid by Rahul (Equal)
        val exp3Id = UUID.randomUUID().toString()
        val exp3 = Expense(
            id = exp3Id,
            groupId = group.id,
            payerId = rahul.id,
            amountPaise = 60000L,
            description = "Airport Taxi",
            category = "travel",
            splitType = SplitType.EQUAL,
            createdAt = System.currentTimeMillis() - 86400000L * 1
        )
        val splits3 = splitter.splitEqual(exp3Id, 60000L, allMemberIds)
        expenseRepo.addExpense(exp3, splits3)

        // Expense 4: Tickets ₹2,000 paid by Sara (Percentage: Ali 40%, Maya 30%, Rahul 20%, Sara 10%)
        val exp4Id = UUID.randomUUID().toString()
        val exp4 = Expense(
            id = exp4Id,
            groupId = group.id,
            payerId = sara.id,
            amountPaise = 200000L,
            description = "Water Sports & Ferry",
            category = "entertainment",
            splitType = SplitType.PERCENTAGE,
            createdAt = System.currentTimeMillis() - 3600000L * 4
        )
        val splits4 = splitter.splitPercentage(
            exp4Id,
            200000L,
            mapOf(
                ali.id to 4000,
                maya.id to 3000,
                rahul.id to 2000,
                sara.id to 1000
            )
        )
        expenseRepo.addExpense(exp4, splits4)

        return group.id
    }
}
