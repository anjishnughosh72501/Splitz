package com.paisede.app.domain.algorithm

import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.ExpenseSplit
import kotlin.math.min

data class DebtEdge(
    val fromUserId: String,
    val toUserId: String,
    val amountPaise: Long
)

/**
 * Directed Graph representing debts between members.
 * Adjacency map structure: Map<FromUserId, MutableMap<ToUserId, AmountPaise>>
 * Edge (u -> v with weight W) means 'u owes v amount W'.
 */
class DebtGraph {

    private val adjacencyMap: MutableMap<String, MutableMap<String, Long>> = mutableMapOf()

    enum class NodeState {
        UNVISITED,
        VISITING,
        VISITED
    }

    /**
     * Clears all nodes and edges from the graph.
     */
    fun clear() {
        adjacencyMap.clear()
    }

    /**
     * Adds debt: [fromUserId] owes [toUserId] [amountPaise].
     * Avoids self-loops. If edge already exists, accumulates amount.
     */
    fun addDebt(fromUserId: String, toUserId: String, amountPaise: Long) {
        if (fromUserId == toUserId || amountPaise <= 0L) return

        val outgoing = adjacencyMap.getOrPut(fromUserId) { mutableMapOf() }
        val currentDebt = outgoing.getOrDefault(toUserId, 0L)
        outgoing[toUserId] = currentDebt + amountPaise
    }

    /**
     * Removes the debt edge between [fromUserId] and [toUserId].
     */
    fun removeDebt(fromUserId: String, toUserId: String) {
        adjacencyMap[fromUserId]?.remove(toUserId)
        if (adjacencyMap[fromUserId]?.isEmpty() == true) {
            adjacencyMap.remove(fromUserId)
        }
    }

    /**
     * Gets the debt amount from [fromUserId] to [toUserId].
     */
    fun getDebt(fromUserId: String, toUserId: String): Long {
        return adjacencyMap[fromUserId]?.get(toUserId) ?: 0L
    }

    /**
     * Gets all outgoing edges (debts owed to others) for a given user.
     */
    fun getOutgoingEdges(userId: String): Map<String, Long> {
        return adjacencyMap[userId]?.toMap() ?: emptyMap()
    }

    /**
     * Returns a list of all debt edges currently in the graph.
     */
    fun getAllEdges(): List<DebtEdge> {
        val edges = mutableListOf<DebtEdge>()
        for ((from, toMap) in adjacencyMap) {
            for ((to, amount) in toMap) {
                if (amount > 0L) {
                    edges.add(DebtEdge(from, to, amount))
                }
            }
        }
        return edges
    }

    /**
     * All unique node IDs present in the graph.
     */
    fun getAllNodes(): Set<String> {
        val nodes = mutableSetOf<String>()
        for ((from, toMap) in adjacencyMap) {
            nodes.add(from)
            nodes.addAll(toMap.keys)
        }
        return nodes
    }

    /**
     * Builds the raw debt graph from a list of expenses and splits.
     * For each expense: for each split participant (who isn't the payer),
     * participant owes payer their split amount.
     */
    fun buildFromExpenses(expenses: List<Pair<Expense, List<ExpenseSplit>>>) {
        clear()
        for ((expense, splits) in expenses) {
            val payerId = expense.payerId
            for (split in splits) {
                if (split.userId != payerId && split.amountPaise > 0L) {
                    addDebt(split.userId, payerId, split.amountPaise)
                }
            }
        }
    }

    /**
     * Detects a directed cycle using DFS with 3 states (UNVISITED, VISITING, VISITED).
     * Returns the cycle path (e.g. [A, B, C, A]) or null if graph is a DAG (acyclic).
     */
    fun detectCycle(): List<String>? {
        val state = mutableMapOf<String, NodeState>()
        val parent = mutableMapOf<String, String?>()
        val allNodes = getAllNodes()

        for (node in allNodes) {
            state[node] = NodeState.UNVISITED
        }

        for (node in allNodes) {
            if (state[node] == NodeState.UNVISITED) {
                val cycle = dfsFindCycle(node, state, parent)
                if (cycle != null) return cycle
            }
        }
        return null
    }

    private fun dfsFindCycle(
        current: String,
        state: MutableMap<String, NodeState>,
        parent: MutableMap<String, String?>
    ): List<String>? {
        state[current] = NodeState.VISITING

        val neighbors = adjacencyMap[current]?.keys ?: emptySet()
        for (neighbor in neighbors) {
            if ((adjacencyMap[current]?.get(neighbor) ?: 0L) <= 0L) continue

            when (state[neighbor]) {
                NodeState.VISITING -> {
                    // Back-edge found! Reconstruct cycle from current back to neighbor
                    val cycle = mutableListOf<String>()
                    cycle.add(neighbor)
                    var curr: String? = current
                    while (curr != null && curr != neighbor) {
                        cycle.add(curr)
                        curr = parent[curr]
                    }
                    cycle.add(neighbor)
                    cycle.reverse()
                    return cycle
                }
                NodeState.UNVISITED -> {
                    parent[neighbor] = current
                    val result = dfsFindCycle(neighbor, state, parent)
                    if (result != null) return result
                }
                NodeState.VISITED -> {
                    // Cross / forward edge, no cycle in this branch
                }
                null -> {}
            }
        }

        state[current] = NodeState.VISITED
        return null
    }

    /**
     * Cancels out a detected cycle:
     * 1. Finds the minimum debt edge in the cycle.
     * 2. Subtracts that minimum amount from each edge in the cycle.
     * 3. Removes any edges that become zero.
     *
     * Returns the amount canceled.
     */
    fun cancelCycle(cycle: List<String>): Long {
        if (cycle.size < 2) return 0L

        // Find min edge weight along cycle edges
        var minAmount = Long.MAX_VALUE
        for (i in 0 until cycle.size - 1) {
            val from = cycle[i]
            val to = cycle[i + 1]
            val debt = getDebt(from, to)
            if (debt <= 0L) return 0L // Edge no longer valid
            minAmount = min(minAmount, debt)
        }

        if (minAmount == Long.MAX_VALUE || minAmount <= 0L) return 0L

        // Subtract minAmount from each edge
        for (i in 0 until cycle.size - 1) {
            val from = cycle[i]
            val to = cycle[i + 1]
            val currentDebt = getDebt(from, to)
            val newDebt = currentDebt - minAmount
            if (newDebt > 0L) {
                adjacencyMap[from]?.put(to, newDebt)
            } else {
                removeDebt(from, to)
            }
        }

        return minAmount
    }

    /**
     * Repeatedly detects and cancels all cycles until the graph becomes a DAG.
     * Returns the total amount eliminated across all cycles.
     */
    fun cancelAllCycles(): Long {
        var totalCanceled = 0L
        while (true) {
            val cycle = detectCycle() ?: break
            val canceled = cancelCycle(cycle)
            if (canceled <= 0L) break
            totalCanceled += canceled
        }
        return totalCanceled
    }
}
