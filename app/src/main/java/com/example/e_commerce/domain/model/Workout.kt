package com.example.e_commerce.domain.model

import com.example.e_commerce.data.local.ExerciseCategory

// UI-facing model. Kept separate from Room entities so we can evolve the DB
// schema without breaking screens, and hide `id = 0` + timestamp fiddliness.
data class Workout(
    val id: Long,
    val exerciseName: String,
    val category: ExerciseCategory,
    val notes: String,
    val performedAtMillis: Long,
    val sets: List<ExerciseSet>
) {
    val totalVolume: Float get() = sets.sumOf { (it.reps * it.weightKg).toDouble() }.toFloat()
}

data class ExerciseSet(
    val setNumber: Int,
    val reps: Int,
    val weightKg: Float
)