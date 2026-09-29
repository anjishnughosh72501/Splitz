package com.paisede.app

import android.app.Application
import com.paisede.app.data.db.AppDatabase
import com.paisede.app.data.repository.ExpenseRepositoryImpl
import com.paisede.app.data.repository.GroupRepositoryImpl
import com.paisede.app.data.repository.SettlementRepositoryImpl
import com.paisede.app.domain.algorithm.AnalyticsCalculator
import com.paisede.app.domain.algorithm.BalanceCalculator
import com.paisede.app.domain.algorithm.DebtGraph
import com.paisede.app.domain.algorithm.DebtSimplifier
import com.paisede.app.domain.algorithm.ExactSettlementSolver
import com.paisede.app.domain.algorithm.ExpenseSplitter
import com.paisede.app.domain.algorithm.UndoRedoManager
import com.paisede.app.domain.repository.ExpenseRepository
import com.paisede.app.domain.repository.GroupRepository
import com.paisede.app.domain.repository.SettlementRepository

class PaiseDeApplication : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var groupRepository: GroupRepository
        private set

    lateinit var expenseRepository: ExpenseRepository
        private set

    lateinit var settlementRepository: SettlementRepository
        private set

    // Shared algorithm instances
    val splitter = ExpenseSplitter()
    val balanceCalculator = BalanceCalculator()
    val debtGraph = DebtGraph()
    val debtSimplifier = DebtSimplifier()
    val exactSolver = ExactSettlementSolver(debtSimplifier)
    val undoRedoManager = UndoRedoManager()
    val analyticsCalculator = AnalyticsCalculator()

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        groupRepository = GroupRepositoryImpl(database)
        expenseRepository = ExpenseRepositoryImpl(database, undoRedoManager)
        settlementRepository = SettlementRepositoryImpl(database)
    }
}
