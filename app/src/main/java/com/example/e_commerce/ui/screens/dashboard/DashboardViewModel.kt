package com.example.e_commerce.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.local.BodyWeightEntity
import com.example.e_commerce.data.local.UserProfileEntity
import com.example.e_commerce.data.repository.BodyWeightRepository
import com.example.e_commerce.data.repository.UserProfileRepository
import com.example.e_commerce.data.repository.WorkoutRepository
import com.example.e_commerce.domain.model.PersonalRecord
import com.example.e_commerce.domain.model.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Single ViewModel that merges the three flows the dashboard needs so the UI
// can read one UiState and avoid "three-flow spaghetti" inside the screen.
class DashboardViewModel(
    private val workoutRepository: WorkoutRepository,
    bodyWeightRepository: BodyWeightRepository,
    userProfileRepository: UserProfileRepository
) : ViewModel() {

    data class UiState(
        val profile: UserProfileEntity? = null,
        val latestWeight: BodyWeightEntity? = null,
        val recentWorkouts: List<Workout> = emptyList(),
        val todayWorkouts: List<Workout> = emptyList(),
        val streak: Int = 0,
        val personalRecords: List<PersonalRecord> = emptyList()
    )

    // streak + PRs are suspend calls → recompute when workouts change.
    private val streak = MutableStateFlow(0)
    private val prs = MutableStateFlow<List<PersonalRecord>>(emptyList())

    init {
        workoutRepository.observeAllWorkouts()
            .onEach {
                streak.value = workoutRepository.currentStreak()
                prs.value = workoutRepository.personalRecords().take(5)
            }
            .launchIn(viewModelScope)
    }

    val state: StateFlow<UiState> = combine(
        userProfileRepository.observeProfile(),
        bodyWeightRepository.observeLatest(),
        workoutRepository.observeAllWorkouts(),
        streak,
        prs
    ) { profile, weight, workouts, streakValue, prList ->
        val startToday = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }.timeInMillis
        UiState(
            profile = profile,
            latestWeight = weight,
            recentWorkouts = workouts.take(5),
            todayWorkouts = workouts.filter { it.performedAtMillis >= startToday },
            streak = streakValue,
            personalRecords = prList
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState())
}