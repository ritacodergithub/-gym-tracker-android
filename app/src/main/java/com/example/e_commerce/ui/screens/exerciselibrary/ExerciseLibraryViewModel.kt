package com.example.e_commerce.ui.screens.exerciselibrary

import androidx.lifecycle.ViewModel
import com.example.e_commerce.data.catalog.Exercise
import com.example.e_commerce.data.catalog.ExerciseLibrary
import com.example.e_commerce.data.local.ExerciseCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExerciseLibraryViewModel : ViewModel() {

    data class UiState(
        val query: String = "",
        val filter: ExerciseCategory? = null,
        val results: List<Exercise> = ExerciseLibrary.all
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun onQueryChange(value: String) {
        _state.value = _state.value.copy(query = value, results = computeResults(value, _state.value.filter))
    }

    fun onFilterChange(category: ExerciseCategory?) {
        _state.value = _state.value.copy(filter = category, results = computeResults(_state.value.query, category))
    }

    private fun computeResults(query: String, filter: ExerciseCategory?): List<Exercise> {
        var list = ExerciseLibrary.search(query)
        if (filter != null) list = list.filter { it.category == filter }
        return list
    }
}