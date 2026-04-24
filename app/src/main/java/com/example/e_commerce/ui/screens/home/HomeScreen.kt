package com.example.e_commerce.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e_commerce.ui.components.FloatingPillNav
import com.example.e_commerce.ui.components.GlowFab
import com.example.e_commerce.ui.components.NavTab
import com.example.e_commerce.ui.components.PageBackground
import com.example.e_commerce.ui.navigation.Screen
import com.example.e_commerce.ui.screens.dashboard.DashboardScreen
import com.example.e_commerce.ui.screens.history.HistoryScreen
import com.example.e_commerce.ui.screens.progress.ProgressScreen
import com.example.e_commerce.ui.screens.settings.SettingsScreen

@Composable
fun HomeScreen(
    onAddWorkoutClick: () -> Unit,
    onOpenRoutines: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenBodyWeight: () -> Unit,
    onResetToOnboarding: () -> Unit
) {
    val tabNavController = rememberNavController()
    val currentRoute = tabNavController.currentBackStackEntryAsState()
        .value?.destination?.hierarchy
        ?.map { it.route }
        ?.firstOrNull()

    val tabs = listOf(
        NavTab(Screen.DashboardTab.route, "Home", Icons.Default.Home),
        NavTab(Screen.HistoryTab.route, "History", Icons.Default.History),
        NavTab(Screen.ProgressTab.route, "Progress", Icons.Default.ShowChart),
        NavTab(Screen.SettingsTab.route, "Settings", Icons.Default.Settings)
    )

    PageBackground {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            bottomBar = {
                FloatingPillNav(
                    tabs = tabs,
                    selected = currentRoute,
                    onSelect = { route ->
                        tabNavController.navigate(route) {
                            popUpTo(tabNavController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { inner ->
            Box(Modifier.fillMaxSize().padding(inner)) {
                NavHost(
                    navController = tabNavController,
                    startDestination = Screen.DashboardTab.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(Screen.DashboardTab.route) {
                        DashboardScreen(
                            onOpenRoutines = onOpenRoutines,
                            onOpenLibrary = onOpenLibrary,
                            onOpenBodyWeight = onOpenBodyWeight
                        )
                    }
                    composable(Screen.HistoryTab.route) { HistoryScreen() }
                    composable(Screen.ProgressTab.route) { ProgressScreen() }
                    composable(Screen.SettingsTab.route) {
                        SettingsScreen(onResetToOnboarding = onResetToOnboarding)
                    }
                }

                // Floating glowing FAB — hidden on Settings. Positioned above
                // the pill nav so it reads as the primary action on Home.
                if (currentRoute != Screen.SettingsTab.route) {
                    GlowFab(
                        onClick = onAddWorkoutClick,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 18.dp)
                    )
                }
            }
        }
    }
}