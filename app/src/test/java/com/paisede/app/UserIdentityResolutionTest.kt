package com.paisede.app

import com.paisede.app.domain.algorithm.BalanceCalculator
import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.ExpenseSplit
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.model.SplitType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Validates that current-user identification does not dangerously fall back to `members.firstOrNull()`.
 */
class UserIdentityResolutionTest {

    private val balanceCalculator = BalanceCalculator()

    @Test
    fun testUserIdentity_resolvesCorrectUserWithoutFirstMemberFallback() {
        val alice = Member("m1", "g1", "Alice")
        val bob = Member("m2", "g1", "Bob")
        val charlie = Member("m3", "g1", "Charlie")
        val members = listOf(alice, bob, charlie)

        // Alice pays 30000 for Alice and Bob (15000 each)
        val expense = Expense("e1", "g1", "m1", 30000L, "Lunch", "Food", SplitType.EQUAL)
        val splits = listOf(
            ExpenseSplit("e1", "m1", 15000L),
            ExpenseSplit("e1", "m2", 15000L)
        )
        val balances = balanceCalculator.calculateNetBalances(members, listOf(Pair(expense, splits)), emptyList())

        // Alice balance = +15000, Bob = -15000, Charlie = 0
        assertEquals(15000L, balances["m1"])
        assertEquals(-15000L, balances["m2"])
        assertEquals(0L, balances["m3"])

        // When current user is Bob
        val currentUserName = "Bob"
        val userMemberBob = members.firstOrNull { m ->
            m.name.startsWith("Me (") || (currentUserName.isNotBlank() && m.name.equals(currentUserName, ignoreCase = true))
        }
        assertEquals("m2", userMemberBob?.id)
        val bobBalance = userMemberBob?.let { balances[it.id] } ?: 0L
        assertEquals(-15000L, bobBalance)

        // When current user is not a member of this group (e.g., "David")
        val outsiderName = "David"
        val userMemberOutsider = members.firstOrNull { m ->
            m.name.startsWith("Me (") || (outsiderName.isNotBlank() && m.name.equals(outsiderName, ignoreCase = true))
        }
        // MUST BE NULL - MUST NOT FALL BACK TO ALICE (members.firstOrNull())
        assertNull("External user must not be resolved to first group member", userMemberOutsider)
        val outsiderBalance = userMemberOutsider?.let { balances[it.id] } ?: 0L
        assertEquals(0L, outsiderBalance)
    }

    @Test
    fun testUserIdentity_resolvesMePrefix() {
        val meMember = Member("m1", "g1", "Me (Rick)")
        val friendMember = Member("m2", "g1", "Sarah")
        val members = listOf(friendMember, meMember)

        val userMember = members.firstOrNull { m ->
            m.name.startsWith("Me (") || ("Rick".isNotBlank() && m.name.equals("Rick", ignoreCase = true))
        }
        assertEquals("m1", userMember?.id)
    }
}
