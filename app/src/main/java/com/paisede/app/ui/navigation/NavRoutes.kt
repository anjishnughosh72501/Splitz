package com.paisede.app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CreateGroup : Screen("group/new")
    object GroupDashboard : Screen("group/{groupId}") {
        fun createRoute(groupId: String) = "group/$groupId"
    }
    object AddExpense : Screen("group/{groupId}/expense/new") {
        fun createRoute(groupId: String) = "group/$groupId/expense/new"
    }
    object ExpenseHistory : Screen("group/{groupId}/expenses") {
        fun createRoute(groupId: String) = "group/$groupId/expenses"
    }
    object Balances : Screen("group/{groupId}/balances") {
        fun createRoute(groupId: String) = "group/$groupId/balances"
    }
    object SimplifyDebts : Screen("group/{groupId}/simplify") {
        fun createRoute(groupId: String) = "group/$groupId/simplify"
    }
    object SettleUp : Screen("group/{groupId}/settle") {
        fun createRoute(groupId: String) = "group/$groupId/settle"
    }
    object SettlementHistory : Screen("group/{groupId}/settlements") {
        fun createRoute(groupId: String) = "group/$groupId/settlements"
    }
    object Analytics : Screen("group/{groupId}/analytics") {
        fun createRoute(groupId: String) = "group/$groupId/analytics"
    }
    object DebtGraph : Screen("group/{groupId}/graph") {
        fun createRoute(groupId: String) = "group/$groupId/graph"
    }
    object DebugPanel : Screen("group/{groupId}/debug") {
        fun createRoute(groupId: String) = "group/$groupId/debug"
    }
}
