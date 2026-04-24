package com.example.e_commerce.ui.screens.addworkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.catalog.Exercise
import com.example.e_commerce.data.catalog.ExerciseLibrary
import com.example.e_commerce.data.local.ExerciseCategory
import com.example.e_commerce.data.repository.WorkoutRepository
import com.example.e_commerce.domain.model.ExerciseSet
import com.example.e_commerce.domain.model.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Owns form state for "Add Workout". Each set has a stable UI id so deleting
// a middle set doesn't reshuffle state of the rest of the rows.
class AddWorkoutViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    data class SetInput(
        val uiId: Long,
        val reps: String = "",
        val weight: String = ""
    )

    data class UiState(
        val exerciseName: String = "",
        val category: ExerciseCategory = ExerciseCategory.CHEST,
        val notes: String = "",
        val sets: List<SetInput> = listOf(SetInput(uiId = 0L)),
        val isSaving: Boolean = false,
        val saved: Boolean = false,
        val error: String? = null,
        // Surfaced to the UI as "Last time: 3×10 @ 60 kg" hint.
        val lastWorkoutHint: Workout? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var nextSetId: Long = 1L

    fun onExerciseNameChange(value: String) {
        _state.value = _state.value.copy(exerciseName = value, error = null)
        // Lookup last-workout hint when the name changes, but only when the
        // name matches an exact past log to avoid thrashing on every keystroke.
        loadLastWorkoutHint(value)
    }

    fun onCategoryChange(value: ExerciseCategory) {
        _state.value = _state.value.copy(category = value)
    }

    fun onNotesChange(value: String) {
        _state.value = _state.value.copy(notes = value)
    }

    // Called from the library picker sheet. Pre-fills name, category, and
    // fetches the user's last session for this exercise.
    fun applyLibraryExercise(exercise: Exercise) {
        _state.value = _state.value.copy(
            exerciseName = exercise.name,
            category = exercise.category,
            error = null
        )
        loadLastWorkoutHint(exercise.name)
    }

    // Alternative entry point: open the screen pre-seeded from an exerciseId
    // (e.g., from a routine day's "log now" tap).
    fun prefillFromExerciseId(exerciseId: String) {
        ExerciseLibrary.find(exerciseId)?.let { applyLibraryExercise(it) }
    }

    fun addSet() {
        val newSet = SetInput(uiId = nextSetId++)
        _state.value = _state.value.copy(sets = _state.value.sets + newSet)
    }

    fun removeSet(uiId: Long) {
        val remaining = _state.value.sets.filterNot { it.uiId == uiId }
        _state.value = _state.value.copy(
            sets = if (remaining.isEmpty()) listOf(SetInput(uiId = nextSetId++)) else remaining
        )
    }

    fun onSetRepsChange(uiId: Long, reps: String) {
        _state.value = _state.value.copy(
            sets = _state.value.sets.map {
                if (it.uiId == uiId) it.copy(reps = reps) else it
            }
        )
    }

    fun onSetWeightChange(uiId: Long, weight: String) {
        _state.value = _state.value.copy(
            sets = _state.value.sets.map {
                if (it.uiId == uiId) it.copy(weight = weight) else it
            }
        )
    }

    fun save() {
        val s = _state.value
        if (s.exerciseName.isBlank()) {
            _state.value = s.copy(error = "Exercise name is required")
            return
        }
        val parsed = s.sets.mapIndexedNotNull { index, input ->
            val reps = input.reps.toIntOrNull()
            val weight = input.weight.toFloatOrNull() ?: 0f
            if (reps == null || reps <= 0) null
            else ExerciseSet(setNumber = index + 1, reps = reps, weightKg = weight)
        }
        if (parsed.isEmpty()) {
            _state.value = s.copy(error = "Add at least one set with reps > 0")
            return
        }

        _state.value = s.copy(isSaving = true, error = null)
        viewModelScope.launch {
            try {
                repository.addWorkout(
                    exerciseName = s.exerciseName,
                    category = s.category,
                    notes = s.notes,
                    sets = parsed
                )
                _state.value = UiState(saved = true)
            } catch (t: Throwable) {
                _state.value = s.copy(isSaving = false, error = t.message ?: "Failed to save")
            }
        }
    }

    fun consumeSaved() {
        _state.value = _state.value.copy(saved = false)
    }

    private fun loadLastWorkoutHint(exerciseName: String) {
        if (exerciseName.isBlank()) {
            _state.value = _state.value.copy(lastWorkoutHint = null)
            return
        }
        viewModelScope.launch {
            val last = repository.lastWorkoutFor(exerciseName)
            _state.value = _state.value.copy(lastWorkoutHint = last)
        }
    }
}