package com.example.e_commerce.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.repository.WorkoutRepository
import com.example.e_commerce.domain.model.Workout
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    // stateIn replays the latest list to new collectors and keeps the flow
    // alive for 5s after the last subscriber — prevents re-querying Room on
    // every configuration change.
    val workouts: StateFlow<List<Workout>> = repository
        .observeAllWorkouts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun delete(workout: Workout) {
        viewModelScope.launch { repository.deleteWorkout(workout) }
    }
}