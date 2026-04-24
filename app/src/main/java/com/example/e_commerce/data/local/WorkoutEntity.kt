package com.example.e_commerce.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// One row per logged workout session / exercise.
// A workout has many ExerciseSetEntity rows linked by workoutId.
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exerciseName: String,
    val category: ExerciseCategory,
    val notes: String = "",
    val performedAt: Long = System.currentTimeMillis() // epoch millis
)