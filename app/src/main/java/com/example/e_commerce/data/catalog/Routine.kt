package com.example.e_commerce.data.catalog

// A training program: several days, each with a fixed list of exercises.
// Users don't edit these — they pick one and follow it. Log entries still
// flow through WorkoutRepository so no schema change is needed.
data class Routine(
    val id: String,
    val name: String,
    val summary: String,
    val daysPerWeek: Int,
    val level: Difficulty,
    val days: List<RoutineDay>
)

data class RoutineDay(
    val name: String,
    val exercises: List<RoutineExercise>
)

data class RoutineExercise(
    val exerciseId: String,
    val sets: Int,
    val reps: String,   // "8–12" or "5" — free text so we can express ranges
    val notes: String = ""
)