package com.example.e_commerce.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.e_commerce.ui.screens.addworkout.AddWorkoutScreen
import com.example.e_commerce.ui.screens.bodyweight.BodyWeightScreen
import com.example.e_commerce.ui.screens.exerciselibrary.ExerciseLibraryScreen
import com.example.e_commerce.ui.screens.home.HomeScreen
import com.example.e_commerce.ui.screens.onboarding.OnboardingScreen
import com.example.e_commerce.ui.screens.routines.RoutinesScreen

// Top-level nav graph. Start destination is decided by MainActivity based on
// whether a user profile exists (onboarded or not).
@Composable
fun GymNavHost(
    startDestination: String,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onAddWorkoutClick = { navController.navigate(Screen.AddWorkout.with()) },
                onOpenRoutines = { navController.navigate(Screen.Routines.route) },
                onOpenLibrary = { navController.navigate(Screen.Library.route) },
                onOpenBodyWeight = { navController.navigate(Screen.BodyWeight.route) },
                onResetToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.AddWorkout.route,
            arguments = listOf(
                navArgument(Screen.AddWorkout.ARG_EXERCISE_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStack ->
            val exerciseId = backStack.arguments?.getString(Screen.AddWorkout.ARG_EXERCISE_ID)
            AddWorkoutScreen(
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
                prefilledExerciseId = exerciseId
            )
        }

        composable(Screen.Library.route) {
            ExerciseLibraryScreen(
                onBack = { navController.popBackStack() },
                onPick = null
            )
        }

        composable(Screen.BodyWeight.route) {
            BodyWeightScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Routines.route) {
            RoutinesScreen()
        }
    }
}