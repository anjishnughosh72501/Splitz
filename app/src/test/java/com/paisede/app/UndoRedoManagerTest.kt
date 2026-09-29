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
}
