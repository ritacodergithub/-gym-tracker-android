package com.example.e_commerce.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.local.Goal
import com.example.e_commerce.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Collects the bare minimum to personalize the app: name, email (optional),
// starting weight, and a goal. Saving populates the one-row UserProfileEntity
// and that flips MainActivity out of onboarding.
class OnboardingViewModel(
    private val profileRepository: UserProfileRepository
) : ViewModel() {

    data class UiState(
        val name: String = "",
        val email: String = "",
        val weight: String = "",
        val goal: Goal = Goal.BUILD_MUSCLE,
        val errors: Map<Field, String> = emptyMap(),
        val isSaving: Boolean = false,
        val done: Boolean = false
    )

    enum class Field { NAME, WEIGHT }

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun onNameChange(v: String) = set { it.copy(name = v, errors = it.errors - Field.NAME) }
    fun onEmailChange(v: String) = set { it.copy(email = v) }
    fun onWeightChange(v: String) = set { it.copy(weight = v, errors = it.errors - Field.WEIGHT) }
    fun onGoalChange(v: Goal) = set { it.copy(goal = v) }

    fun finish() {
        val s = _state.value
        val errors = buildMap<Field, String> {
            if (s.name.isBlank()) put(Field.NAME, "Name is required")
            val w = s.weight.toFloatOrNull()
            if (w == null || w <= 0f) put(Field.WEIGHT, "Enter a valid weight in kg")
        }
        if (errors.isNotEmpty()) {
            set { it.copy(errors = errors) }
            return
        }

        set { it.copy(isSaving = true) }
        viewModelScope.launch {
            profileRepository.saveOnboarding(
                name = s.name,
                email = s.email,
                startingWeightKg = s.weight.toFloat(),
                goal = s.goal
            )
            set { it.copy(isSaving = false, done = true) }
        }
    }

    private inline fun set(block: (UiState) -> UiState) {
        _state.value = block(_state.value)
    }
}