package com.paisede.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.paisede.app.PaiseDeApplication
import com.paisede.app.domain.algorithm.AnalyticsCalculator
import com.paisede.app.domain.algorithm.BalanceCalculator
import com.paisede.app.domain.algorithm.DebtEdge
import com.paisede.app.domain.algorithm.DebtGraph
import com.paisede.app.domain.algorithm.DebtSimplifier
import com.paisede.app.domain.algorithm.ExactSettlementSolver
import com.paisede.app.domain.algorithm.ExpenseSplitter
import com.paisede.app.domain.algorithm.UndoRedoManager
import com.paisede.app.domain.model.AnalyticsResult
import com.paisede.app.domain.model.Balance
import com.paisede.app.domain.model.Expense
import com.paisede.app.domain.model.ExpenseSplit
import com.paisede.app.domain.model.Group
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.model.SettlementTransaction
import com.paisede.app.domain.model.SimplificationResult
import com.paisede.app.domain.model.SplitType
import com.paisede.app.domain.repository.ExpenseRepository
import com.paisede.app.domain.repository.GroupRepository
import com.paisede.app.domain.repository.SettlementRepository
import com.paisede.app.util.SampleDataGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

// ======================== HOME VIEW MODEL ========================

data class GroupCardData(
    val group: Group,
    val memberCount: Int,
    val totalSpendingPaise: Long,
    val userNetBalancePaise: Long
)

data class HomeUiState(
    val groups: List<GroupCardData> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

class HomeViewModel(
    private val groupRepository: GroupRepository,
    private val expenseRepository: ExpenseRepository,
    private val settlementRepository: SettlementRepository,
    private val balanceCalculator: BalanceCalculator
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadGroups()
    }

    fun loadGroups() {
        viewModelScope.launch {
            groupRepository.getAllGroups().collect { groups ->
                val cards = groups.map { group ->
                    val members = groupRepository.getMembersSync(group.id)
                    val expenses = expenseRepository.getActiveExpensesSync(group.id)
                    val settlements = settlementRepository.getSettlementsSync(group.id)

                    val balances = balanceCalculator.calculateNetBalances(members, expenses, settlements)
                    val totalSpending = expenses.sumOf { it.first.amountPaise }
                    val userBal = members.firstOrNull()?.let { balances[it.id] } ?: 0L

                    GroupCardData(
                        group = group,
                        memberCount = members.size,
                        totalSpendingPaise = totalSpending,
                        userNetBalancePaise = userBal
                    )
                }
                _uiState.update { it.copy(groups = cards, isLoading = false) }
            }
        }
    }

    fun loadSampleData(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val groupId = SampleDataGenerator.populateSampleData(groupRepository, expenseRepository)
                _uiState.update { it.copy(isLoading = false, userMessage = "Sample 'Goa Trip' loaded!") }
                onSuccess(groupId)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, userMessage = "Error: ${e.message}") }
            }
        }
    }

    fun deleteGroup(groupId: String) {
        viewModelScope.launch {
            try {
                groupRepository.deleteGroup(groupId)
                _uiState.update { it.copy(userMessage = "Group deleted successfully") }
            } catch (e: Exception) {
                _uiState.update { it.copy(userMessage = "Failed to delete group: ${e.message}") }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }
}

// ======================== GROUP DASHBOARD VIEW MODEL ========================

data class GroupUiState(
    val group: Group? = null,
    val members: List<Member> = emptyList(),
    val totalSpendingPaise: Long = 0L,
    val userNetBalancePaise: Long = 0L,
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val isLoading: Boolean = false,
    val message: String? = null
)

class GroupViewModel(
    val groupId: String,
    private val groupRepository: GroupRepository,
    private val expenseRepository: ExpenseRepository,
    private val settlementRepository: SettlementRepository,
    private val balanceCalculator: BalanceCalculator
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupUiState(isLoading = true))
    val uiState: StateFlow<GroupUiState> = _uiState.asStateFlow()

    init {
        observeGroupData()
    }

    private fun observeGroupData() {
        viewModelScope.launch {
            val group = groupRepository.getGroupById(groupId)
            combine(
                groupRepository.getMembers(groupId),
                expenseRepository.getActiveExpenses(groupId),
                settlementRepository.getSettlements(groupId),
                expenseRepository.canUndo(groupId),
                expenseRepository.canRedo(groupId)
            ) { members, expenses, settlements, canUndo, canRedo ->
                val balances = balanceCalculator.calculateNetBalances(members, expenses, settlements)
                val totalSpending = expenses.sumOf { it.first.amountPaise }
                val userBal = members.firstOrNull()?.let { balances[it.id] } ?: 0L

                GroupUiState(
                    group = group,
                    members = members,
                    totalSpendingPaise = totalSpending,
                    userNetBalancePaise = userBal,
                    canUndo = canUndo,
                    canRedo = canRedo,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun undo() {
        viewModelScope.launch {
            val success = expenseRepository.undoExpense(groupId)
            val msg = if (success) "Expense undone" else "Nothing to undo"
            _uiState.update { it.copy(message = msg) }
        }
    }

    fun redo() {
        viewModelScope.launch {
            val success = expenseRepository.redoExpense(groupId)
            val msg = if (success) "Expense restored" else "Nothing to redo"
            _uiState.update { it.copy(message = msg) }
        }
    }

    fun deleteGroup(onDeleted: () -> Unit) {
        viewModelScope.launch {
            try {
                groupRepository.deleteGroup(groupId)
                onDeleted()
            } catch (e: Exception) {
                _uiState.update { it.copy(message = "Failed to delete group: ${e.message}") }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}

// ======================== EXPENSE VIEW MODEL ========================

data class AddExpenseUiState(
    val amountPaise: Long = 0L,
    val payerId: String = "",
    val description: String = "",
    val category: String = "food",
    val splitType: SplitType = SplitType.EQUAL,
    val participantIds: Set<String> = emptySet(),
    val exactAmounts: Map<String, Long> = emptyMap(),
    val percentagesBp: Map<String, Int> = emptyMap(),
    val shares: Map<String, Int> = emptyMap(),
    val validationError: String? = null,
    val isSaving: Boolean = false,
    val isSuccess: Boolean = false
)

data class ExpenseHistoryUiState(
    val expenses: List<Pair<Expense, List<ExpenseSplit>>> = emptyList(),
    val members: Map<String, Member> = emptyMap(),
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val selectedPayerId: String? = null,
    val sortDescending: Boolean = true,
    val canUndo: Boolean = false,
    val canRedo: Boolean = false
)

class ExpenseViewModel(
    val groupId: String,
    private val groupRepository: GroupRepository,
    private val expenseRepository: ExpenseRepository,
    private val splitter: ExpenseSplitter
) : ViewModel() {

    private val _addState = MutableStateFlow(AddExpenseUiState())
    val addState: StateFlow<AddExpenseUiState> = _addState.asStateFlow()

    private val _historyState = MutableStateFlow(ExpenseHistoryUiState())
    val historyState: StateFlow<ExpenseHistoryUiState> = _historyState.asStateFlow()

    init {
        observeHistory()
    }

    private fun observeHistory() {
        viewModelScope.launch {
            combine(
                expenseRepository.getActiveExpenses(groupId),
                groupRepository.getMembers(groupId),
                expenseRepository.canUndo(groupId),
                expenseRepository.canRedo(groupId)
            ) { expenses, members, canUndo, canRedo ->
                val memberMap = members.associateBy { it.id }
                _historyState.update { current ->
                    current.copy(
                        expenses = expenses,
                        members = memberMap,
                        canUndo = canUndo,
                        canRedo = canRedo
                    )
                }
            }.collect {}
        }
    }

    fun setAmount(paise: Long) {
        _addState.update { it.copy(amountPaise = paise, validationError = null) }
    }

    fun setPayer(payerId: String) {
        _addState.update { it.copy(payerId = payerId, validationError = null) }
    }

    fun setDescription(desc: String) {
        _addState.update { it.copy(description = desc, validationError = null) }
    }

    fun setCategory(cat: String) {
        _addState.update { it.copy(category = cat) }
    }

    fun setSplitType(type: SplitType) {
        _addState.update { it.copy(splitType = type, validationError = null) }
    }

    fun toggleParticipant(userId: String) {
        _addState.update { state ->
            val set = state.participantIds.toMutableSet()
            if (set.contains(userId)) set.remove(userId) else set.add(userId)
            state.copy(participantIds = set, validationError = null)
        }
    }

    fun setExactAmount(userId: String, paise: Long) {
        _addState.update { state ->
            val map = state.exactAmounts.toMutableMap()
            map[userId] = paise
            state.copy(exactAmounts = map, validationError = null)
        }
    }

    fun setPercentageBp(userId: String, bp: Int) {
        _addState.update { state ->
            val map = state.percentagesBp.toMutableMap()
            map[userId] = bp
            state.copy(percentagesBp = map, validationError = null)
        }
    }

    fun setShares(userId: String, share: Int) {
        _addState.update { state ->
            val map = state.shares.toMutableMap()
            map[userId] = share
            state.copy(shares = map, validationError = null)
        }
    }

    fun saveExpense(onSuccess: () -> Unit) {
        val state = _addState.value
        if (state.amountPaise <= 0L) {
            _addState.update { it.copy(validationError = "Please enter an amount greater than 0") }
            return
        }
        if (state.payerId.isEmpty()) {
            _addState.update { it.copy(validationError = "Please select who paid") }
            return
        }
        if (state.description.trim().isEmpty()) {
            _addState.update { it.copy(validationError = "Please provide a description") }
            return
        }
        if (state.participantIds.isEmpty()) {
            _addState.update { it.copy(validationError = "Select at least one participant") }
            return
        }

        val expenseId = UUID.randomUUID().toString()
        val splits: List<ExpenseSplit>
        try {
            splits = when (state.splitType) {
                SplitType.EQUAL -> {
                    splitter.splitEqual(expenseId, state.amountPaise, state.participantIds.toList())
                }
                SplitType.EXACT -> {
                    val filtered = state.exactAmounts.filterKeys { it in state.participantIds }
                    splitter.splitExact(expenseId, state.amountPaise, filtered)
                }
                SplitType.PERCENTAGE -> {
                    val filtered = state.percentagesBp.filterKeys { it in state.participantIds }
                    splitter.splitPercentage(expenseId, state.amountPaise, filtered)
                }
                SplitType.SHARES -> {
                    val filtered = state.shares.filterKeys { it in state.participantIds }
                    splitter.splitShares(expenseId, state.amountPaise, filtered)
                }
            }
        } catch (e: Exception) {
            _addState.update { it.copy(validationError = e.message ?: "Invalid split configuration") }
            return
        }

        viewModelScope.launch {
            _addState.update { it.copy(isSaving = true) }
            val expense = Expense(
                id = expenseId,
                groupId = groupId,
                payerId = state.payerId,
                amountPaise = state.amountPaise,
                description = state.description.trim(),
                category = state.category,
                splitType = state.splitType,
                createdAt = System.currentTimeMillis()
            )
            expenseRepository.addExpense(expense, splits)
            _addState.update { it.copy(isSaving = false, isSuccess = true) }
            onSuccess()
        }
    }

    fun updateSearchQuery(q: String) {
        _historyState.update { it.copy(searchQuery = q) }
    }

    fun filterByCategory(cat: String?) {
        _historyState.update { it.copy(selectedCategory = cat) }
    }

    fun filterByPayer(payerId: String?) {
        _historyState.update { it.copy(selectedPayerId = payerId) }
    }

    fun toggleSort() {
        _historyState.update { it.copy(sortDescending = !it.sortDescending) }
    }

    fun undo() {
        viewModelScope.launch { expenseRepository.undoExpense(groupId) }
    }

    fun redo() {
        viewModelScope.launch { expenseRepository.redoExpense(groupId) }
    }
}

// ======================== BALANCES VIEW MODEL ========================

data class MemberBalanceItem(
    val member: Member,
    val balancePaise: Long
)

data class BalancesUiState(
    val items: List<MemberBalanceItem> = emptyList(),
    val totalSpendingPaise: Long = 0L,
    val totalOutstandingDebtPaise: Long = 0L,
    val isLoading: Boolean = false
)

class BalanceViewModel(
    val groupId: String,
    private val groupRepository: GroupRepository,
    private val expenseRepository: ExpenseRepository,
    private val settlementRepository: SettlementRepository,
    private val balanceCalculator: BalanceCalculator
) : ViewModel() {

    private val _uiState = MutableStateFlow(BalancesUiState(isLoading = true))
    val uiState: StateFlow<BalancesUiState> = _uiState.asStateFlow()

    init {
        observeBalances()
    }

    private fun observeBalances() {
        viewModelScope.launch {
            combine(
                groupRepository.getMembers(groupId),
                expenseRepository.getActiveExpenses(groupId),
                settlementRepository.getSettlements(groupId)
            ) { members, expenses, settlements ->
                val netMap = balanceCalculator.calculateNetBalances(members, expenses, settlements)
                val items = members.map { member ->
                    MemberBalanceItem(member, netMap[member.id] ?: 0L)
                }.sortedByDescending { it.balancePaise }

                val totalSpending = expenses.sumOf { it.first.amountPaise }
                val totalDebts = items.filter { it.balancePaise < 0 }.sumOf { kotlin.math.abs(it.balancePaise) }

                BalancesUiState(
                    items = items,
                    totalSpendingPaise = totalSpending,
                    totalOutstandingDebtPaise = totalDebts,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
}

// ======================== SIMPLIFY VIEW MODEL ========================

data class SimplifyUiState(
    val isLoading: Boolean = false,
    val transactions: List<SettlementTransaction> = emptyList(),
    val members: Map<String, Member> = emptyMap(),
    val beforeCount: Int = 0,
    val afterCount: Int = 0,
    val greedyCount: Int = 0,
    val exactCount: Int? = null,
    val algorithmUsed: String = "Greedy",
    val isExactAvailable: Boolean = false,
    val selectedMode: String = "Greedy", // "Greedy" or "Exact"
    val error: String? = null
)

class SimplifyViewModel(
    val groupId: String,
    private val groupRepository: GroupRepository,
    private val expenseRepository: ExpenseRepository,
    private val settlementRepository: SettlementRepository,
    private val balanceCalculator: BalanceCalculator,
    private val debtGraph: DebtGraph,
    private val debtSimplifier: DebtSimplifier,
    private val exactSolver: ExactSettlementSolver
) : ViewModel() {

    private val _uiState = MutableStateFlow(SimplifyUiState(isLoading = true))
    val uiState: StateFlow<SimplifyUiState> = _uiState.asStateFlow()

    private var greedyList: List<SettlementTransaction> = emptyList()
    private var exactList: List<SettlementTransaction> = emptyList()

    init {
        computeSimplification()
    }

    fun computeSimplification() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val members = groupRepository.getMembersSync(groupId)
            val memberMap = members.associateBy { it.id }
            val expenses = expenseRepository.getActiveExpensesSync(groupId)
            val settlements = settlementRepository.getSettlementsSync(groupId)

            val netBalances = balanceCalculator.calculateNetBalances(members, expenses, settlements)

            // Build raw graph to get beforeCount (raw outstanding debt edges)
            debtGraph.buildFromExpenses(expenses)
            val rawEdges = debtGraph.getAllEdges()
            val beforeCount = rawEdges.size

            // 1. Solve greedily
            val greedyTxs = debtSimplifier.simplifyGreedy(netBalances)
            greedyList = greedyTxs

            val nonZeroCount = netBalances.values.count { it != 0L }
            val canRunExact = nonZeroCount in 1..ExactSettlementSolver.MAX_EXACT_PARTICIPANTS

            var exactTxs: List<SettlementTransaction>? = null
            if (canRunExact) {
                // Run exact backtracking solver off UI thread on Dispatchers.Default
                exactTxs = withContext(Dispatchers.Default) {
                    exactSolver.findMinimumTransactions(netBalances)
                }
                exactList = exactTxs
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    transactions = greedyTxs,
                    members = memberMap,
                    beforeCount = beforeCount,
                    afterCount = greedyTxs.size,
                    greedyCount = greedyTxs.size,
                    exactCount = exactTxs?.size,
                    algorithmUsed = "Greedy",
                    isExactAvailable = canRunExact,
                    selectedMode = "Greedy"
                )
            }
        }
    }

    fun selectAlgorithm(mode: String) {
        if (mode == "Exact" && _uiState.value.isExactAvailable && exactList.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    selectedMode = "Exact",
                    algorithmUsed = "Exact Minimum (Branch & Bound)",
                    transactions = exactList,
                    afterCount = exactList.size
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    selectedMode = "Greedy",
                    algorithmUsed = "Greedy (2-Heap)",
                    transactions = greedyList,
                    afterCount = greedyList.size
                )
            }
        }
    }
}

// ======================== SETTLEMENT VIEW MODEL ========================

data class SettleUpUiState(
    val recommendedPayments: List<SettlementTransaction> = emptyList(),
    val members: Map<String, Member> = emptyMap(),
    val pastSettlements: List<SettlementTransaction> = emptyList(),
    val isLoading: Boolean = false,
    val pendingSettlement: SettlementTransaction? = null,
    val message: String? = null
)

class SettlementViewModel(
    val groupId: String,
    private val groupRepository: GroupRepository,
    private val expenseRepository: ExpenseRepository,
    private val settlementRepository: SettlementRepository,
    private val balanceCalculator: BalanceCalculator,
    private val debtSimplifier: DebtSimplifier
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettleUpUiState(isLoading = true))
    val uiState: StateFlow<SettleUpUiState> = _uiState.asStateFlow()

    init {
        observeSettlements()
    }

    private fun observeSettlements() {
        viewModelScope.launch {
            combine(
                groupRepository.getMembers(groupId),
                expenseRepository.getActiveExpenses(groupId),
                settlementRepository.getSettlements(groupId)
            ) { members, expenses, settlements ->
                val memberMap = members.associateBy { it.id }
                val balances = balanceCalculator.calculateNetBalances(members, expenses, settlements)
                val recommended = debtSimplifier.simplifyGreedy(balances)

                SettleUpUiState(
                    recommendedPayments = recommended,
                    members = memberMap,
                    pastSettlements = settlements,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun promptSettlement(tx: SettlementTransaction) {
        _uiState.update { it.copy(pendingSettlement = tx) }
    }

    fun dismissPrompt() {
        _uiState.update { it.copy(pendingSettlement = null) }
    }

    fun confirmSettlement() {
        val tx = _uiState.value.pendingSettlement ?: return
        viewModelScope.launch {
            settlementRepository.recordSettlement(
                groupId = groupId,
                fromUserId = tx.fromUserId,
                toUserId = tx.toUserId,
                amountPaise = tx.amountPaise
            )
            _uiState.update {
                it.copy(
                    pendingSettlement = null,
                    message = "Settlement recorded successfully!"
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}

// ======================== ANALYTICS VIEW MODEL ========================

data class AnalyticsUiState(
    val analytics: AnalyticsResult = AnalyticsResult(),
    val members: Map<String, Member> = emptyMap(),
    val isLoading: Boolean = false
)

class AnalyticsViewModel(
    val groupId: String,
    private val groupRepository: GroupRepository,
    private val expenseRepository: ExpenseRepository,
    private val analyticsCalculator: AnalyticsCalculator
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState(isLoading = true))
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        observeAnalytics()
    }

    private fun observeAnalytics() {
        viewModelScope.launch {
            combine(
                groupRepository.getMembers(groupId),
                expenseRepository.getActiveExpenses(groupId)
            ) { members, expenses ->
                val memberMap = members.associateBy { it.id }
                val result = analyticsCalculator.calculate(expenses)
                AnalyticsUiState(
                    analytics = result,
                    members = memberMap,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
}

// ======================== DEBT GRAPH VIEW MODEL ========================

data class DebtGraphUiState(
    val members: List<Member> = emptyList(),
    val rawEdges: List<DebtEdge> = emptyList(),
    val simplifiedEdges: List<DebtEdge> = emptyList(),
    val detectedCycle: List<String>? = null,
    val cyclesCanceledAmount: Long = 0L,
    val isSimplifiedTab: Boolean = false,
    val isLoading: Boolean = false
)

class DebtGraphViewModel(
    val groupId: String,
    private val groupRepository: GroupRepository,
    private val expenseRepository: ExpenseRepository,
    private val settlementRepository: SettlementRepository,
    private val balanceCalculator: BalanceCalculator,
    private val debtGraph: DebtGraph,
    private val debtSimplifier: DebtSimplifier
) : ViewModel() {

    private val _uiState = MutableStateFlow(DebtGraphUiState(isLoading = true))
    val uiState: StateFlow<DebtGraphUiState> = _uiState.asStateFlow()

    init {
        loadGraphData()
    }

    fun loadGraphData() {
        viewModelScope.launch {
            val members = groupRepository.getMembersSync(groupId)
            val expenses = expenseRepository.getActiveExpensesSync(groupId)
            val settlements = settlementRepository.getSettlementsSync(groupId)

            debtGraph.buildFromExpenses(expenses)
            val rawEdges = debtGraph.getAllEdges()
            val cycle = debtGraph.detectCycle()

            val netBalances = balanceCalculator.calculateNetBalances(members, expenses, settlements)
            val simplifiedTxs = debtSimplifier.simplifyGreedy(netBalances)
            val simplifiedEdges = simplifiedTxs.map {
                DebtEdge(it.fromUserId, it.toUserId, it.amountPaise)
            }

            _uiState.update {
                it.copy(
                    members = members,
                    rawEdges = rawEdges,
                    simplifiedEdges = simplifiedEdges,
                    detectedCycle = cycle,
                    isLoading = false
                )
            }
        }
    }

    fun cancelCycles() {
        viewModelScope.launch {
            val amount = debtGraph.cancelAllCycles()
            val updatedRawEdges = debtGraph.getAllEdges()
            val remainingCycle = debtGraph.detectCycle()

            _uiState.update {
                it.copy(
                    rawEdges = updatedRawEdges,
                    detectedCycle = remainingCycle,
                    cyclesCanceledAmount = it.cyclesCanceledAmount + amount
                )
            }
        }
    }

    fun setTab(isSimplified: Boolean) {
        _uiState.update { it.copy(isSimplifiedTab = isSimplified) }
    }
}

// ======================== DATA-STRUCTURE DEBUG VIEW MODEL ========================

data class DebugPanelUiState(
    val netBalances: Map<String, Long> = emptyMap(),
    val creditorQueue: List<Balance> = emptyList(),
    val debtorQueue: List<Balance> = emptyList(),
    val graphEdges: List<DebtEdge> = emptyList(),
    val detectedCycles: List<String>? = null,
    val greedyTxCount: Int = 0,
    val exactTxCount: Int? = null,
    val undoStack: List<String> = emptyList(),
    val redoStack: List<String> = emptyList(),
    val members: Map<String, Member> = emptyMap(),
    val isLoading: Boolean = false
)

class DebugViewModel(
    val groupId: String,
    private val groupRepository: GroupRepository,
    private val expenseRepository: ExpenseRepository,
    private val settlementRepository: SettlementRepository,
    private val balanceCalculator: BalanceCalculator,
    private val debtGraph: DebtGraph,
    private val debtSimplifier: DebtSimplifier,
    private val exactSolver: ExactSettlementSolver,
    private val undoRedoManager: UndoRedoManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DebugPanelUiState(isLoading = true))
    val uiState: StateFlow<DebugPanelUiState> = _uiState.asStateFlow()

    init {
        refreshDebugData()
    }

    fun refreshDebugData() {
        viewModelScope.launch {
            val members = groupRepository.getMembersSync(groupId)
            val memberMap = members.associateBy { it.id }
            val expenses = expenseRepository.getActiveExpensesSync(groupId)
            val settlements = settlementRepository.getSettlementsSync(groupId)

            val netBalances = balanceCalculator.calculateNetBalances(members, expenses, settlements)
            val (creditorHeap, debtorHeap) = debtSimplifier.buildHeaps(netBalances)

            val credList = mutableListOf<Balance>()
            while (creditorHeap.isNotEmpty()) credList.add(creditorHeap.poll()!!)

            val debList = mutableListOf<Balance>()
            while (debtorHeap.isNotEmpty()) debList.add(debtorHeap.poll()!!)

            debtGraph.buildFromExpenses(expenses)
            val edges = debtGraph.getAllEdges()
            val cycle = debtGraph.detectCycle()

            val greedyTxs = debtSimplifier.simplifyGreedy(netBalances)

            val nonZero = netBalances.values.count { it != 0L }
            val exactTxs = if (nonZero in 1..ExactSettlementSolver.MAX_EXACT_PARTICIPANTS) {
                withContext(Dispatchers.Default) {
                    exactSolver.findMinimumTransactions(netBalances)
                }
            } else null

            val undoList = undoRedoManager.getUndoStackSnapshot(groupId).map { it.expenseId }
            val redoList = undoRedoManager.getRedoStackSnapshot(groupId).map { it.expenseId }

            _uiState.update {
                it.copy(
                    netBalances = netBalances,
                    creditorQueue = credList,
                    debtorQueue = debList,
                    graphEdges = edges,
                    detectedCycles = cycle,
                    greedyTxCount = greedyTxs.size,
                    exactTxCount = exactTxs?.size,
                    undoStack = undoList,
                    redoStack = redoList,
                    members = memberMap,
                    isLoading = false
                )
            }
        }
    }
}

// ======================== VIEW MODEL FACTORY ========================

class ViewModelFactory(
    private val app: PaiseDeApplication,
    private val groupId: String? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(
                    app.groupRepository,
                    app.expenseRepository,
                    app.settlementRepository,
                    app.balanceCalculator
                ) as T
            }
            modelClass.isAssignableFrom(GroupViewModel::class.java) -> {
                GroupViewModel(
                    groupId!!,
                    app.groupRepository,
                    app.expenseRepository,
                    app.settlementRepository,
                    app.balanceCalculator
                ) as T
            }
            modelClass.isAssignableFrom(ExpenseViewModel::class.java) -> {
                ExpenseViewModel(
                    groupId!!,
                    app.groupRepository,
                    app.expenseRepository,
                    app.splitter
                ) as T
            }
            modelClass.isAssignableFrom(BalanceViewModel::class.java) -> {
                BalanceViewModel(
                    groupId!!,
                    app.groupRepository,
                    app.expenseRepository,
                    app.settlementRepository,
                    app.balanceCalculator
                ) as T
            }
            modelClass.isAssignableFrom(SimplifyViewModel::class.java) -> {
                SimplifyViewModel(
                    groupId!!,
                    app.groupRepository,
                    app.expenseRepository,
                    app.settlementRepository,
                    app.balanceCalculator,
                    app.debtGraph,
                    app.debtSimplifier,
                    app.exactSolver
                ) as T
            }
            modelClass.isAssignableFrom(SettlementViewModel::class.java) -> {
                SettlementViewModel(
                    groupId!!,
                    app.groupRepository,
                    app.expenseRepository,
                    app.settlementRepository,
                    app.balanceCalculator,
                    app.debtSimplifier
                ) as T
            }
            modelClass.isAssignableFrom(AnalyticsViewModel::class.java) -> {
                AnalyticsViewModel(
                    groupId!!,
                    app.groupRepository,
                    app.expenseRepository,
                    app.analyticsCalculator
                ) as T
            }
            modelClass.isAssignableFrom(DebtGraphViewModel::class.java) -> {
                DebtGraphViewModel(
                    groupId!!,
                    app.groupRepository,
                    app.expenseRepository,
                    app.settlementRepository,
                    app.balanceCalculator,
                    app.debtGraph,
                    app.debtSimplifier
                ) as T
            }
            modelClass.isAssignableFrom(DebugViewModel::class.java) -> {
                DebugViewModel(
                    groupId!!,
                    app.groupRepository,
                    app.expenseRepository,
                    app.settlementRepository,
                    app.balanceCalculator,
                    app.debtGraph,
                    app.debtSimplifier,
                    app.exactSolver,
                    app.undoRedoManager
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
        }
    }
}
