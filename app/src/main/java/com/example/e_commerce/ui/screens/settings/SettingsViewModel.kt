package com.example.e_commerce.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.local.Goal
import com.example.e_commerce.data.local.UserProfileEntity
import com.example.e_commerce.data.local.WeightUnit
import com.example.e_commerce.data.repository.BodyWeightRepository
import com.example.e_commerce.data.repository.UserProfileRepository
import com.example.e_commerce.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val profileRepository: UserProfileRepository,
    private val workoutRepository: WorkoutRepository,
    private val bodyWeightRepository: BodyWeightRepository
) : ViewModel() {

    val profile: StateFlow<UserProfileEntity?> = profileRepository.observeProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun updateUnit(unit: WeightUnit) = viewModelScope.launch {
        profileRepository.updateUnit(unit)
    }

    fun updateGoal(goal: Goal) = viewModelScope.launch {
        profileRepository.updateGoal(goal)
    }

    fun updateReminder(enabled: Boolean, hour: Int) = viewModelScope.launch {
        profileRepository.updateReminder(enabled, hour)
    }

    fun clearWorkouts() = viewModelScope.launch { workoutRepository.clear() }
    fun clearBodyWeight() = viewModelScope.launch { bodyWeightRepository.clear() }
    fun resetEverything() = viewModelScope.launch {
        workoutRepository.clear()
        bodyWeightRepository.clear()
        profileRepository.clear()
    }
}