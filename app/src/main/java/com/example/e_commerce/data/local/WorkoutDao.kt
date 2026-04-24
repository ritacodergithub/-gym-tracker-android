package com.example.e_commerce.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    // --- Reactive reads ---

    @Transaction
    @Query("SELECT * FROM workouts ORDER BY performedAt DESC")
    fun observeAllWorkoutsWithSets(): Flow<List<WorkoutWithSets>>

    @Transaction
    @Query("SELECT * FROM workouts WHERE performedAt >= :sinceMillis ORDER BY performedAt ASC")
    fun observeWorkoutsSince(sinceMillis: Long): Flow<List<WorkoutWithSets>>

    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutWithSets(workoutId: Long): WorkoutWithSets?

    // One-shot snapshot for analytics (streak, PRs). Doesn't emit — just reads.
    @Transaction
    @Query("SELECT * FROM workouts ORDER BY performedAt DESC")
    suspend fun allWorkoutsWithSetsOnce(): List<WorkoutWithSets>

    // Most recent workout for a given exercise name — used to show the
    // "last time you did this" hint on the log screen.
    @Transaction
    @Query(
        "SELECT * FROM workouts WHERE exerciseName = :name " +
            "ORDER BY performedAt DESC LIMIT 1"
    )
    suspend fun lastWorkoutForExercise(name: String): WorkoutWithSets?

    // All distinct training days (unique calendar dates as epoch-day).
    // Used by repository to compute the streak in Kotlin.
    @Query("SELECT DISTINCT performedAt FROM workouts ORDER BY performedAt DESC")
    suspend fun distinctWorkoutTimestamps(): List<Long>

    // --- Writes ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSets(sets: List<ExerciseSetEntity>)

    @Transaction
    suspend fun insertWorkoutWithSets(
        workout: WorkoutEntity,
        sets: List<ExerciseSetEntity>
    ): Long {
        val workoutId = insertWorkout(workout)
        insertSets(sets.map { it.copy(workoutId = workoutId) })
        return workoutId
    }

    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)

    @Query("DELETE FROM workouts")
    suspend fun clearAll()
}