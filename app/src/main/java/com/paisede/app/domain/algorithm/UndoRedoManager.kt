package com.paisede.app.domain.algorithm

import com.paisede.app.domain.model.ActionType
import com.paisede.app.domain.model.ExpenseAction
import java.util.Stack

/**
 * Manages group-level undo and redo operations using two [java.util.Stack] instances per group.
 * Maintains an append-oriented immutable ledger where undo/redo actions toggle active states
 * rather than physically destroying original records.
 */
class UndoRedoManager {

    // Maps groupId -> undo stack of actions
    private val undoStacks = mutableMapOf<String, Stack<ExpenseAction>>()

    // Maps groupId -> redo stack of actions
    private val redoStacks = mutableMapOf<String, Stack<ExpenseAction>>()

    private fun getUndoStack(groupId: String): Stack<ExpenseAction> {
        return undoStacks.getOrPut(groupId) { Stack() }
    }

    private fun getRedoStack(groupId: String): Stack<ExpenseAction> {
        return redoStacks.getOrPut(groupId) { Stack() }
    }

    /**
     * Initializes stacks from persistent database action entities.
     */
    fun loadFromHistory(groupId: String, actions: List<ExpenseAction>) {
        val undo = Stack<ExpenseAction>()
        val redo = Stack<ExpenseAction>()

        for (action in actions) {
            when (action.actionType) {
                ActionType.ADD, ActionType.REDO -> undo.push(action)
                ActionType.UNDO -> {
                    if (undo.isNotEmpty()) {
                        val undone = undo.pop()
                        redo.push(undone)
                    }
                }
            }
        }

        undoStacks[groupId] = undo
        redoStacks[groupId] = redo
    }

    /**
     * Records a newly added expense.
     * Pushes to undoStack and clears redoStack.
     */
    fun recordExpenseAddition(groupId: String, expenseId: String): ExpenseAction {
        val action = ExpenseAction(
            id = java.util.UUID.randomUUID().toString(),
            groupId = groupId,
            expenseId = expenseId,
            actionType = ActionType.ADD,
            createdAt = System.currentTimeMillis()
        )
        getUndoStack(groupId).push(action)
        getRedoStack(groupId).clear()
        return action
    }

    /**
     * Can the user undo an expense action for the specified group?
     */
    fun canUndo(groupId: String): Boolean {
        return getUndoStack(groupId).isNotEmpty()
    }

    /**
     * Can the user redo an expense action for the specified group?
     */
    fun canRedo(groupId: String): Boolean {
        return getRedoStack(groupId).isNotEmpty()
    }

    /**
     * Undoes the most recent expense addition or redo.
     * Pops from undoStack, pushes to redoStack, and returns the undo action to persist.
     */
    fun undo(groupId: String): ExpenseAction? {
        val undoStack = getUndoStack(groupId)
        if (undoStack.isEmpty()) return null

        val previousAction = undoStack.pop()
        val redoStack = getRedoStack(groupId)
        redoStack.push(previousAction)

        return ExpenseAction(
            id = java.util.UUID.randomUUID().toString(),
            groupId = groupId,
            expenseId = previousAction.expenseId,
            actionType = ActionType.UNDO,
            createdAt = System.currentTimeMillis()
        )
    }

    /**
     * Redoes the most recently undone action.
     * Pops from redoStack, pushes to undoStack, and returns the redo action to persist.
     */
    fun redo(groupId: String): ExpenseAction? {
        val redoStack = getRedoStack(groupId)
        if (redoStack.isEmpty()) return null

        val actionToRedo = redoStack.pop()
        val undoStack = getUndoStack(groupId)
        undoStack.push(actionToRedo)

        return ExpenseAction(
            id = java.util.UUID.randomUUID().toString(),
            groupId = groupId,
            expenseId = actionToRedo.expenseId,
            actionType = ActionType.REDO,
            createdAt = System.currentTimeMillis()
        )
    }

    /**
     * Returns the set of currently active expense IDs for the group.
     */
    fun getActiveExpenseIds(groupId: String): Set<String> {
        val undoStack = getUndoStack(groupId)
        return undoStack.map { it.expenseId }.toSet()
    }

    /**
     * Returns snapshot of undo stack for debug / presentation panel.
     */
    fun getUndoStackSnapshot(groupId: String): List<ExpenseAction> {
        return getUndoStack(groupId).toList()
    }

    /**
     * Returns snapshot of redo stack for debug / presentation panel.
     */
    fun getRedoStackSnapshot(groupId: String): List<ExpenseAction> {
        return getRedoStack(groupId).toList()
    }

    fun clear(groupId: String) {
        undoStacks.remove(groupId)
        redoStacks.remove(groupId)
    }
}
