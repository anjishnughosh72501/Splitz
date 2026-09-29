package com.paisede.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.paisede.app.PaiseDeApplication
import com.paisede.app.ui.screens.analytics.AnalyticsScreen
import com.paisede.app.ui.screens.balance.BalancesScreen
import com.paisede.app.ui.screens.debug.DataStructureDebugScreen
import com.paisede.app.ui.screens.expense.AddExpenseScreen
import com.paisede.app.ui.screens.expense.ExpenseHistoryScreen
import com.paisede.app.ui.screens.graph.DebtGraphScreen
import com.paisede.app.ui.screens.group.CreateGroupScreen
import com.paisede.app.ui.screens.group.GroupDashboardScreen
import com.paisede.app.ui.screens.home.HomeScreen
import com.paisede.app.ui.screens.settle.SettleUpScreen
import com.paisede.app.ui.screens.settle.SettlementHistoryScreen
import com.paisede.app.ui.screens.simplify.SimplifyDebtsScreen
import com.paisede.app.ui.viewmodel.AnalyticsViewModel
import com.paisede.app.ui.viewmodel.BalanceViewModel
import com.paisede.app.ui.viewmodel.DebtGraphViewModel
import com.paisede.app.ui.viewmodel.DebugViewModel
import com.paisede.app.ui.viewmodel.ExpenseViewModel
import com.paisede.app.ui.viewmodel.GroupViewModel
import com.paisede.app.ui.viewmodel.HomeViewModel
import com.paisede.app.ui.viewmodel.SettlementViewModel
import com.paisede.app.ui.viewmodel.SimplifyViewModel
import com.paisede.app.ui.viewmodel.ViewModelFactory

@Composable
fun AppNavigation(
    app: PaiseDeApplication,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // HOME
        composable(Screen.Home.route) {
            val homeVm: HomeViewModel = viewModel(factory = ViewModelFactory(app))
            HomeScreen(
                viewModel = homeVm,
                onCreateGroupClick = { navController.navigate(Screen.CreateGroup.route) },
                onGroupClick = { groupId ->
                    navController.navigate(Screen.GroupDashboard.createRoute(groupId))
                }
            )
        }

        // CREATE GROUP
        composable(Screen.CreateGroup.route) {
            CreateGroupScreen(
                groupRepository = app.groupRepository,
                onNavigateBack = { navController.popBackStack() },
                onGroupCreated = { groupId ->
                    navController.popBackStack()
                    navController.navigate(Screen.GroupDashboard.createRoute(groupId))
                }
            )
        }

        // GROUP DASHBOARD
        composable(
            route = Screen.GroupDashboard.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val groupVm: GroupViewModel = viewModel(factory = ViewModelFactory(app, groupId))

            GroupDashboardScreen(
                viewModel = groupVm,
                onNavigateBack = { navController.popBackStack() },
                onAddExpenseClick = { navController.navigate(Screen.AddExpense.createRoute(groupId)) },
                onBalancesClick = { navController.navigate(Screen.Balances.createRoute(groupId)) },
                onSimplifyClick = { navController.navigate(Screen.SimplifyDebts.createRoute(groupId)) },
                onSettleUpClick = { navController.navigate(Screen.SettleUp.createRoute(groupId)) },
                onExpensesClick = { navController.navigate(Screen.ExpenseHistory.createRoute(groupId)) },
                onAnalyticsClick = { navController.navigate(Screen.Analytics.createRoute(groupId)) },
                onGraphClick = { navController.navigate(Screen.DebtGraph.createRoute(groupId)) },
                onDebugClick = { navController.navigate(Screen.DebugPanel.createRoute(groupId)) }
            )
        }

        // ADD EXPENSE
        composable(
            route = Screen.AddExpense.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val expenseVm: ExpenseViewModel = viewModel(factory = ViewModelFactory(app, groupId))
            val groupVm: GroupViewModel = viewModel(factory = ViewModelFactory(app, groupId))
            val groupState by groupVm.uiState.collectAsState()

            AddExpenseScreen(
                viewModel = expenseVm,
                members = groupState.members,
                onNavigateBack = { navController.popBackStack() },
                onExpenseAdded = { navController.popBackStack() }
            )
        }

        // EXPENSE HISTORY
        composable(
            route = Screen.ExpenseHistory.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val expenseVm: ExpenseViewModel = viewModel(factory = ViewModelFactory(app, groupId))

            ExpenseHistoryScreen(
                viewModel = expenseVm,
                onNavigateBack = { navController.popBackStack() },
                onAddExpenseClick = { navController.navigate(Screen.AddExpense.createRoute(groupId)) }
            )
        }

        // BALANCES
        composable(
            route = Screen.Balances.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val balanceVm: BalanceViewModel = viewModel(factory = ViewModelFactory(app, groupId))

            BalancesScreen(
                viewModel = balanceVm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // SIMPLIFY DEBTS
        composable(
            route = Screen.SimplifyDebts.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val simplifyVm: SimplifyViewModel = viewModel(factory = ViewModelFactory(app, groupId))

            SimplifyDebtsScreen(
                viewModel = simplifyVm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // SETTLE UP
        composable(
            route = Screen.SettleUp.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val settleVm: SettlementViewModel = viewModel(factory = ViewModelFactory(app, groupId))

            SettleUpScreen(
                viewModel = settleVm,
                onNavigateBack = { navController.popBackStack() },
                onViewHistoryClick = { navController.navigate(Screen.SettlementHistory.createRoute(groupId)) }
            )
        }

        // SETTLEMENT HISTORY
        composable(
            route = Screen.SettlementHistory.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val settleVm: SettlementViewModel = viewModel(factory = ViewModelFactory(app, groupId))

            SettlementHistoryScreen(
                viewModel = settleVm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ANALYTICS
        composable(
            route = Screen.Analytics.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val analyticsVm: AnalyticsViewModel = viewModel(factory = ViewModelFactory(app, groupId))

            AnalyticsScreen(
                viewModel = analyticsVm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // DEBT GRAPH
        composable(
            route = Screen.DebtGraph.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val graphVm: DebtGraphViewModel = viewModel(factory = ViewModelFactory(app, groupId))

            DebtGraphScreen(
                viewModel = graphVm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // DATA-STRUCTURE DEBUG / VIVA PANEL
        composable(
            route = Screen.DebugPanel.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val debugVm: DebugViewModel = viewModel(factory = ViewModelFactory(app, groupId))

            DataStructureDebugScreen(
                viewModel = debugVm,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
