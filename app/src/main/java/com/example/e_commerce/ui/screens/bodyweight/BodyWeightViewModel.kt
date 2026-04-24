package com.example.e_commerce.ui.screens.bodyweight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.local.BodyWeightEntity
import com.example.e_commerce.data.local.UserProfileEntity
import com.example.e_commerce.data.repository.BodyWeightRepository
import com.example.e_commerce.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BodyWeightViewModel(
    private val repository: BodyWeightRepository,
    profileRepository: UserProfileRepository
) : ViewModel() {

    data class UiState(
        val weightInput: String = "",
        val entries: List<BodyWeightEntity> = emptyList(),
        val profile: UserProfileEntity? = null,
        val error: String? = null
    )

    private val input = MutableStateFlow("")
    private val error = MutableStateFlow<String?>(null)

    val state: StateFlow<UiState> = combine(
        input,
        repository.observeAll(),
        profileRepository.observeProfile(),
        error
    ) { inputValue, entries, profile, err ->
        UiState(
            weightInput = inputValue,
            entries = entries,
            profile = profile,
            error = err
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState())

    fun onInputChange(v: String) {
        input.value = v
        error.value = null
    }

    fun log() {
        val kg = input.value.toFloatOrNull()
        if (kg == null || kg <= 0f) {
            error.value = "Enter a valid weight"
            return
        }
        viewModelScope.launch {
            repository.log(kg)
            input.value = ""
        }
    }

    fun delete(entry: BodyWeightEntity) {
        viewModelScope.launch { repository.delete(entry) }
    }
}