package com.example.e_commerce.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.local.Goal
import com.example.e_commerce.data.local.UserProfileEntity
import com.example.e_commerce.data.local.WeightUnit
import com.example.e_commerce.data.repository.BodyWeightRepository
import com.example.e_commerce.data.repository.UserProfileRepository
import com.example.e_commerce.data.repository.WorkoutRepository
import com.example.e_commerce.reminder.ReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// AndroidViewModel so we can reach Application context to talk to
// WorkManager without leaking Activity.
class SettingsViewModel(
    application: Application,
    private val profileRepository: UserProfileRepository,
    private val workoutRepository: WorkoutRepository,
    private val bodyWeightRepository: BodyWeightRepository
) : AndroidViewModel(application) {

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
        // Sync with WorkManager — enable schedules the job, disable cancels it.
        val ctx = getApplication<Application>()
        if (enabled) ReminderScheduler.schedule(ctx, hour)
        else ReminderScheduler.cancel(ctx)
    }

    fun clearWorkouts() = viewModelScope.launch { workoutRepository.clear() }
    fun clearBodyWeight() = viewModelScope.launch { bodyWeightRepository.clear() }
    fun resetEverything() = viewModelScope.launch {
        workoutRepository.clear()
        bodyWeightRepository.clear()
        profileRepository.clear()
        ReminderScheduler.cancel(getApplication())
    }
}