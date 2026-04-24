package com.example.e_commerce.ui.screens.aicoach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.local.Goal
import com.example.e_commerce.data.repository.AiCoachRepository
import com.example.e_commerce.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AiCoachViewModel(
    private val coachRepository: AiCoachRepository,
    private val profileRepository: UserProfileRepository
) : ViewModel() {

    data class UiState(
        val goal: Goal = Goal.BUILD_MUSCLE,
        val loading: Boolean = false,
        val result: String? = null,
        val error: String? = null,
        val needsApiKey: Boolean = false
    )

    private val localState = MutableStateFlow(UiState())

    val state: StateFlow<UiState> = combine(
        profileRepository.observeProfile(),
        localState
    ) { profile, local ->
        local.copy(goal = profile?.goal ?: local.goal)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState())

    fun generateTodaysPlan() = viewModelScope.launch {
        localState.value = localState.value.copy(loading = true, result = null, error = null)
        val outcome = coachRepository.generateDailyPlan(goal = state.value.goal)
        handle(outcome)
    }

    fun generateMotivation(streak: Int, name: String) = viewModelScope.launch {
        localState.value = localState.value.copy(loading = true, result = null, error = null)
        val outcome = coachRepository.motivationalNudge(streak = streak, name = name)
        handle(outcome)
    }

    private fun handle(outcome: AiCoachRepository.Outcome) {
        localState.value = when (outcome) {
            is AiCoachRepository.Outcome.Success ->
                localState.value.copy(loading = false, result = outcome.text, error = null)
            is AiCoachRepository.Outcome.Error ->
                localState.value.copy(loading = false, error = outcome.message)
            AiCoachRepository.Outcome.NoApiKey ->
                localState.value.copy(loading = false, needsApiKey = true)
        }
    }

    fun dismissApiKeyHint() {
        localState.value = localState.value.copy(needsApiKey = false)
    }

    // Exposed so the screen can seed the motivation prompt with current data.
    val profile = profileRepository.observeProfile()
}