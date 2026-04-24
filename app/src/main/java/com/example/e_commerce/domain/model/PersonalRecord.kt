package com.example.e_commerce.domain.model

import com.example.e_commerce.data.local.ExerciseCategory

data class PersonalRecord(
    val exerciseName: String,
    val category: ExerciseCategory,
    val bestWeightKg: Float,
    val bestReps: Int,
    val achievedAtMillis: Long
)