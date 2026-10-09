package com.paisede.app

import com.paisede.app.domain.algorithm.UndoRedoManager
import com.paisede.app.domain.model.ActionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UndoRedoManagerTest {

    @Test
    fun testUndoRedoWorkflow() {
        val manager = UndoRedoManager()
        val groupId = "group_1"

        assertFalse(manager.canUndo(groupId))
        assertFalse(manager.canRedo(groupId))

        // 1. Add expense 1
        manager.recordExpenseAddition(groupId, "exp_1")
        assertTrue(manager.canUndo(groupId))
        assertFalse(manager.canRedo(groupId))
        assertEquals(setOf("exp_1"), manager.getActiveExpenseIds(groupId))

        // 2. Add expense 2
        manager.recordExpenseAddition(groupId, "exp_2")
        assertEquals(setOf("exp_1", "exp_2"), manager.getActiveExpenseIds(groupId))

        // 3. Undo expense 2
        val undoAction = manager.undo(groupId)
        assertNotNull(undoAction)
        assertEquals("exp_2", undoAction!!.expenseId)
        assertEquals(ActionType.UNDO, undoAction.actionType)
        assertTrue(manager.canUndo(groupId))
        assertTrue(manager.canRedo(groupId))
        assertEquals(setOf("exp_1"), manager.getActiveExpenseIds(groupId))

        // 4. Undo expense 1
        val undoAction2 = manager.undo(groupId)
        assertNotNull(undoAction2)
        assertEquals("exp_1", undoAction2!!.expenseId)
        assertFalse(manager.canUndo(groupId))
        assertTrue(manager.canRedo(groupId))
        assertEquals(emptySet<String>(), manager.getActiveExpenseIds(groupId))

        // 5. Redo expense 1
        val redoAction1 = manager.redo(groupId)
        assertNotNull(redoAction1)
        assertEquals("exp_1", redoAction1!!.expenseId)
        assertEquals(ActionType.REDO, redoAction1.actionType)
        assertTrue(manager.canUndo(groupId))
        assertTrue(manager.canRedo(groupId))
        assertEquals(setOf("exp_1"), manager.getActiveExpenseIds(groupId))

        // 6. New expense clears redo stack
        manager.recordExpenseAddition(groupId, "exp_3")
        assertFalse(manager.canRedo(groupId))
        assertEquals(setOf("exp_1", "exp_3"), manager.getActiveExpenseIds(groupId))
    }

    @Test
    fun testAuditSpecifiedUndoRedoPersistenceSequence() {
        // Sequence from Section 1.3 D:
        // Add A -> Add B -> Undo B -> Redo B -> Undo B -> Add C -> Attempt Redo -> Restart app -> Verify active expenses
        val manager = UndoRedoManager()
        val groupId = "group_audit"
        val persistedActionLog = mutableListOf<com.paisede.app.domain.model.ExpenseAction>()

        // 1. Add A
        val addA = manager.recordExpenseAddition(groupId, "exp_A")
        persistedActionLog.add(addA)
        assertEquals(setOf("exp_A"), manager.getActiveExpenseIds(groupId))

        // 2. Add B
        val addB = manager.recordExpenseAddition(groupId, "exp_B")
        persistedActionLog.add(addB)
        assertEquals(setOf("exp_A", "exp_B"), manager.getActiveExpenseIds(groupId))

        // 3. Undo B
        val undoB1 = manager.undo(groupId)
        assertNotNull(undoB1)
        persistedActionLog.add(undoB1!!)
        assertEquals(setOf("exp_A"), manager.getActiveExpenseIds(groupId))

        // 4. Redo B
        val redoB = manager.redo(groupId)
        assertNotNull(redoB)
        persistedActionLog.add(redoB!!)
        assertEquals(setOf("exp_A", "exp_B"), manager.getActiveExpenseIds(groupId))

        // 5. Undo B again
        val undoB2 = manager.undo(groupId)
        assertNotNull(undoB2)
        persistedActionLog.add(undoB2!!)
        assertEquals(setOf("exp_A"), manager.getActiveExpenseIds(groupId))

        // 6. Add C (this invalidates / clears the redo stack)
        val addC = manager.recordExpenseAddition(groupId, "exp_C")
        persistedActionLog.add(addC)
        assertEquals(setOf("exp_A", "exp_C"), manager.getActiveExpenseIds(groupId))

        // 7. Attempt Redo (should fail / return null)
        val attemptRedo = manager.redo(groupId)
        assertNull(attemptRedo)
        assertFalse(manager.canRedo(groupId))
        assertEquals(setOf("exp_A", "exp_C"), manager.getActiveExpenseIds(groupId))

        // 8. Simulate App Restart: Fresh manager, rehydrated exclusively from persistent action history
        val freshManager = UndoRedoManager()
        freshManager.loadFromHistory(groupId, persistedActionLog)

        // 9. Verify active expenses and stack capability match exactly
        assertEquals(setOf("exp_A", "exp_C"), freshManager.getActiveExpenseIds(groupId))
        assertTrue(freshManager.canUndo(groupId))
        assertFalse(freshManager.canRedo(groupId))

        // 10. Undo C on fresh manager
        val undoC = freshManager.undo(groupId)
        assertNotNull(undoC)
        assertEquals("exp_C", undoC!!.expenseId)
        assertEquals(setOf("exp_A"), freshManager.getActiveExpenseIds(groupId))
    }
}

