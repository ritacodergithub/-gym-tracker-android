package com.example.e_commerce.ui.navigation

// String-route navigation (simple and plenty for MVP). If routes grow,
// swap to type-safe destinations (androidx.navigation Kotlin serialization).
sealed class Screen(val route: String) {

    data object Onboarding : Screen("onboarding")
    data object Home : Screen("home")               // scaffold with bottom nav

    data object AddWorkout : Screen("add_workout?exerciseId={exerciseId}") {
        const val ARG_EXERCISE_ID = "exerciseId"
        fun with(exerciseId: String? = null): String =
            if (exerciseId == null) "add_workout" else "add_workout?exerciseId=$exerciseId"
    }

    data object Library : Screen("library")
    data object BodyWeight : Screen("body_weight")
    data object Routines : Screen("routines")

    // Bottom-nav tab routes — inside HomeScreen's inner NavHost.
    data object DashboardTab : Screen("tab_dashboard")
    data object HistoryTab : Screen("tab_history")
    data object ProgressTab : Screen("tab_progress")
    data object SettingsTab : Screen("tab_settings")
}