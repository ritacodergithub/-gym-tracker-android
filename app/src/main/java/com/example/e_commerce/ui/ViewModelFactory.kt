package com.example.e_commerce.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.e_commerce.GymApp
import com.example.e_commerce.ui.screens.addworkout.AddWorkoutViewModel
import com.example.e_commerce.ui.screens.bodyweight.BodyWeightViewModel
import com.example.e_commerce.ui.screens.dashboard.DashboardViewModel
import com.example.e_commerce.ui.screens.exerciselibrary.ExerciseLibraryViewModel
import com.example.e_commerce.ui.screens.history.HistoryViewModel
import com.example.e_commerce.ui.screens.onboarding.OnboardingViewModel
import com.example.e_commerce.ui.screens.progress.ProgressViewModel
import com.example.e_commerce.ui.screens.routines.RoutinesViewModel
import com.example.e_commerce.ui.screens.settings.SettingsViewModel

// One Factory object with an `initializer` per ViewModel. Call it from
// Compose via `viewModel(factory = AppViewModelProvider.Factory)`.
object AppViewModelProvider {

    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { OnboardingViewModel(app().userProfileRepository) }
        initializer {
            DashboardViewModel(
                workoutRepository = app().workoutRepository,
                bodyWeightRepository = app().bodyWeightRepository,
                userProfileRepository = app().userProfileRepository
            )
        }
        initializer {
            AddWorkoutViewModel(
                repository = app().workoutRepository
            )
        }
        initializer { HistoryViewModel(app().workoutRepository) }
        initializer { ProgressViewModel(app().workoutRepository) }
        initializer { ExerciseLibraryViewModel() }
        initializer {
            BodyWeightViewModel(
                repository = app().bodyWeightRepository,
                profileRepository = app().userProfileRepository
            )
        }
        initializer { RoutinesViewModel() }
        initializer {
            SettingsViewModel(
                application = app(),
                profileRepository = app().userProfileRepository,
                workoutRepository = app().workoutRepository,
                bodyWeightRepository = app().bodyWeightRepository
            )
        }
    }

    private fun CreationExtras.app(): GymApp =
        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GymApp
}