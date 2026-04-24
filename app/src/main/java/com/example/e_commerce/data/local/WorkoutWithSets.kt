package com.example.e_commerce.data.local

import androidx.room.Embedded
import androidx.room.Relation

// Room @Relation result: a workout plus all its sets in one query.
// Used by the DAO to return a joined view to the UI.
data class WorkoutWithSets(
    @Embedded val workout: WorkoutEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val sets: List<ExerciseSetEntity>
) {
    // Total volume for this workout = Σ reps × weight. Handy for progress charts.
    val totalVolume: Float
        get() = sets.sumOf { (it.reps * it.weightKg).toDouble() }.toFloat()
}