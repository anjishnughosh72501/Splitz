package com.paisede.app.ui.navigation

import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import com.paisede.app.ui.screens.settings.SettingsScreen
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

import com.paisede.app.ui.screens.startup.StartupScreen

@Composable
fun AppNavigation(
    app: PaiseDeApplication,
    navController: NavHostController = rememberNavController()
) {
    val isStartupCompleted by app.userPreferences.isStartupCompleted.collectAsState()
    val startDestination = if (isStartupCompleted) Screen.Home.route else Screen.Startup.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { SplitzEnterTransition.SlideFade },
        exitTransition = { SplitzExitTransition.SlideFade },
        popEnterTransition = { SplitzEnterTransition.SlideFadeReverse },
        popExitTransition = { SplitzExitTransition.SlideFadeReverse }
    ) {
        // STARTUP SCREEN
        composable(
            route = Screen.Startup.route,
            enterTransition = { SplitzEnterTransition.FadeScale },
            exitTransition = { SplitzExitTransition.FadeScale }
        ) {
            StartupScreen(
                userPreferences = app.userPreferences,
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Startup.route) { inclusive = true }
                    }
                }
            )
        }

        // HOME
        composable(
            route = Screen.Home.route,
            enterTransition = { SplitzEnterTransition.FadeScale },
            exitTransition = { SplitzExitTransition.FadeScale }
        ) {
            val homeVm: HomeViewModel = viewModel(factory = ViewModelFactory(app))
            HomeScreen(
                viewModel = homeVm,
                userPreferences = app.userPreferences,
                onCreateGroupClick = { navController.navigate(Screen.CreateGroup.route) },
                onGroupClick = { groupId ->
                    navController.navigate(Screen.GroupDashboard.createRoute(groupId))
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.createRoute())
                }
            )
        }

        // CREATE GROUP
        composable(
            route = Screen.CreateGroup.route,
            enterTransition = { SplitzEnterTransition.SlideUp },
            exitTransition = { SplitzExitTransition.SlideDown }
        ) {
            CreateGroupScreen(
                groupRepository = app.groupRepository,
                userPreferences = app.userPreferences,
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
            arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
            enterTransition = { SplitzEnterTransition.SlideFade },
            exitTransition = { SplitzExitTransition.SlideFade }
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val groupVm: GroupViewModel = viewModel(factory = ViewModelFactory(app, groupId))

            GroupDashboardScreen(
                viewModel = groupVm,
                userPreferences = app.userPreferences,
                onNavigateBack = { navController.popBackStack() },
                onAddExpenseClick = { navController.navigate(Screen.AddExpense.createRoute(groupId)) },
                onBalancesClick = { navController.navigate(Screen.Balances.createRoute(groupId)) },
                onSimplifyClick = { navController.navigate(Screen.SimplifyDebts.createRoute(groupId)) },
                onSettleUpClick = { navController.navigate(Screen.SettleUp.createRoute(groupId)) },
                onExpensesClick = { navController.navigate(Screen.ExpenseHistory.createRoute(groupId)) },
                onAnalyticsClick = { navController.navigate(Screen.Analytics.createRoute(groupId)) },
                onGraphClick = { navController.navigate(Screen.DebtGraph.createRoute(groupId)) },
                onDebugClick = { navController.navigate(Screen.DebugPanel.createRoute(groupId)) },
                onSettingsClick = { navController.navigate(Screen.Settings.createRoute(groupId)) }
            )
        }

        // ADD EXPENSE
        composable(
            route = Screen.AddExpense.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
            enterTransition = { SplitzEnterTransition.SlideUp },
            exitTransition = { SplitzExitTransition.SlideDown }
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
            arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
            enterTransition = { SplitzEnterTransition.SlideFade },
            exitTransition = { SplitzExitTransition.SlideFade }
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
            arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
            enterTransition = { SplitzEnterTransition.SlideFade },
            exitTransition = { SplitzExitTransition.SlideFade }
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val balanceVm: BalanceViewModel = viewModel(factory = ViewModelFactory(app, groupId))

            BalancesScreen(
                viewModel = balanceVm,
                userPreferences = app.userPreferences,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // SIMPLIFY DEBTS
        composable(
            route = Screen.SimplifyDebts.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
            enterTransition = { SplitzEnterTransition.SlideFade },
            exitTransition = { SplitzExitTransition.SlideFade }
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
            arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
            enterTransition = { SplitzEnterTransition.SlideUp },
            exitTransition = { SplitzExitTransition.SlideDown }
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
            arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
            enterTransition = { SplitzEnterTransition.SlideFade },
            exitTransition = { SplitzExitTransition.SlideFade }
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
            arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
            enterTransition = { SplitzEnterTransition.SlideFade },
            exitTransition = { SplitzExitTransition.SlideFade }
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
            arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
            enterTransition = { SplitzEnterTransition.FadeScale },
            exitTransition = { SplitzExitTransition.FadeScale }
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
            arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
            enterTransition = { SplitzEnterTransition.SlideFade },
            exitTransition = { SplitzExitTransition.SlideFade }
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            val debugVm: DebugViewModel = viewModel(factory = ViewModelFactory(app, groupId))

            DataStructureDebugScreen(
                viewModel = debugVm,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // SETTINGS & HELP
        composable(
            route = Screen.Settings.route,
            arguments = listOf(
                navArgument("groupId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            ),
            enterTransition = { SplitzEnterTransition.FadeScale },
            exitTransition = { SplitzExitTransition.FadeScale }
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")
            SettingsScreen(
                groupId = groupId,
                themePreferences = app.themePreferences,
                userPreferences = app.userPreferences,
                onNavigateBack = { navController.popBackStack() },
                onInspectDataStructures = { gid ->
                    navController.navigate(Screen.DebugPanel.createRoute(gid))
                }
            )
        }
    }
}

// ============================================================
// ============================================================
// NAVIGATION TRANSITIONS
// ============================================================

object SplitzEnterTransition {
    val FadeScale: androidx.compose.animation.EnterTransition =
        androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(250, easing = androidx.compose.animation.core.CubicBezierEasing(0.05f, 0.0f, 0.1f, 1.0f))) +
        androidx.compose.animation.scaleIn(initialScale = 0.95f, animationSpec = androidx.compose.animation.core.tween(250, easing = androidx.compose.animation.core.CubicBezierEasing(0.05f, 0.0f, 0.1f, 1.0f)))

    val SlideFade: androidx.compose.animation.EnterTransition =
        androidx.compose.animation.slideInVertically(initialOffsetY = { 80 }, animationSpec = androidx.compose.animation.core.tween(300, easing = androidx.compose.animation.core.CubicBezierEasing(0.05f, 0.0f, 0.1f, 1.0f))) +
        androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(250, easing = androidx.compose.animation.core.CubicBezierEasing(0.05f, 0.0f, 0.1f, 1.0f)))

    val SlideFadeReverse: androidx.compose.animation.EnterTransition =
        androidx.compose.animation.slideInVertically(initialOffsetY = { -80 }, animationSpec = androidx.compose.animation.core.tween(300, easing = androidx.compose.animation.core.CubicBezierEasing(0.05f, 0.0f, 0.1f, 1.0f))) +
        androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(250, easing = androidx.compose.animation.core.CubicBezierEasing(0.05f, 0.0f, 0.1f, 1.0f)))

    val SlideUp: androidx.compose.animation.EnterTransition =
        androidx.compose.animation.slideInVertically(initialOffsetY = { it }, animationSpec = androidx.compose.animation.core.tween(400, easing = androidx.compose.animation.core.CubicBezierEasing(0.05f, 0.0f, 0.1f, 1.0f))) +
        androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(300, easing = androidx.compose.animation.core.CubicBezierEasing(0.05f, 0.0f, 0.1f, 1.0f)))

    val SlideDown: androidx.compose.animation.EnterTransition =
        androidx.compose.animation.slideInVertically(initialOffsetY = { -it }, animationSpec = androidx.compose.animation.core.tween(300, easing = androidx.compose.animation.core.CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f))) +
        androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)))
}

object SplitzExitTransition {
    val FadeScale: androidx.compose.animation.ExitTransition =
        androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f))) +
        androidx.compose.animation.scaleOut(targetScale = 0.95f, animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)))

    val SlideFade: androidx.compose.animation.ExitTransition =
        androidx.compose.animation.slideOutVertically(targetOffsetY = { -80 }, animationSpec = androidx.compose.animation.core.tween(250, easing = androidx.compose.animation.core.CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f))) +
        androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)))

    val SlideFadeReverse: androidx.compose.animation.ExitTransition =
        androidx.compose.animation.slideOutVertically(targetOffsetY = { 80 }, animationSpec = androidx.compose.animation.core.tween(250, easing = androidx.compose.animation.core.CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f))) +
        androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)))

    val SlideUp: androidx.compose.animation.ExitTransition =
        androidx.compose.animation.slideOutVertically(targetOffsetY = { -it }, animationSpec = androidx.compose.animation.core.tween(300, easing = androidx.compose.animation.core.CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f))) +
        androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)))

    val SlideDown: androidx.compose.animation.ExitTransition =
        androidx.compose.animation.slideOutVertically(targetOffsetY = { it }, animationSpec = androidx.compose.animation.core.tween(300, easing = androidx.compose.animation.core.CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f))) +
        androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)))
}