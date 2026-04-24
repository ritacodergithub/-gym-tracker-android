package com.example.e_commerce.data.repository

import com.example.e_commerce.data.local.ExerciseCategory
import com.example.e_commerce.data.local.ExerciseSetEntity
import com.example.e_commerce.data.local.WorkoutDao
import com.example.e_commerce.data.local.WorkoutEntity
import com.example.e_commerce.data.local.WorkoutWithSets
import com.example.e_commerce.domain.model.ExerciseSet
import com.example.e_commerce.domain.model.PersonalRecord
import com.example.e_commerce.domain.model.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.concurrent.TimeUnit

// Single source of truth for workout data. ViewModels only talk to this —
// they never see Room entities, which keeps the DB swappable (Firestore sync
// can be layered in here later without touching the UI).
class WorkoutRepository(private val dao: WorkoutDao) {

    fun observeAllWorkouts(): Flow<List<Workout>> =
        dao.observeAllWorkoutsWithSets().map { list -> list.map { it.toDomain() } }

    fun observeWorkoutsSince(sinceMillis: Long): Flow<List<Workout>> =
        dao.observeWorkoutsSince(sinceMillis).map { list -> list.map { it.toDomain() } }

    suspend fun lastWorkoutFor(exerciseName: String): Workout? =
        dao.lastWorkoutForExercise(exerciseName)?.toDomain()

    // --- Analytics: streak, PRs ---

    // Current streak = number of consecutive calendar days ending today
    // where at least one workout was logged. Today doesn't have to be hit
    // yet — we allow a one-day grace for "come back tomorrow".
    suspend fun currentStreak(today: Long = System.currentTimeMillis()): Int {
        val timestamps = dao.distinctWorkoutTimestamps()
        if (timestamps.isEmpty()) return 0
        val trainedDays = timestamps.map { startOfDay(it) }.toSortedSet().toList().reversed()

        val todayStart = startOfDay(today)
        var streak = 0
        var cursor = todayStart
        // If nothing logged today, roll back to yesterday and count from there.
        if (trainedDays.first() < todayStart) cursor = todayStart - ONE_DAY

        for (day in trainedDays) {
            if (day == cursor) {
                streak++
                cursor -= ONE_DAY
            } else if (day < cursor) {
                break
            }
        }
        return streak
    }

    // Top-weight PR per exercise. A proper "1-rep-max estimate" would use
    // the Epley formula; for MVP we just surface the heaviest set.
    suspend fun personalRecords(): List<PersonalRecord> {
        val sessions = dao.allWorkoutsWithSetsOnce().map { it.toDomain() }
        return sessions
            .groupBy { it.exerciseName }
            .mapNotNull { (name, group) ->
                val allSets = group.flatMap { it.sets }
                val heaviest = allSets.maxByOrNull { it.weightKg } ?: return@mapNotNull null
                val latest = group.maxByOrNull { it.performedAtMillis }
                PersonalRecord(
                    exerciseName = name,
                    category = latest?.category ?: group.first().category,
                    bestWeightKg = heaviest.weightKg,
                    bestReps = heaviest.reps,
                    achievedAtMillis = latest?.performedAtMillis ?: 0L
                )
            }
            .sortedByDescending { it.bestWeightKg }
    }

    // --- Writes ---

    suspend fun addWorkout(
        exerciseName: String,
        category: ExerciseCategory,
        notes: String,
        sets: List<ExerciseSet>,
        performedAtMillis: Long = System.currentTimeMillis()
    ): Long {
        val entity = WorkoutEntity(
            exerciseName = exerciseName.trim(),
            category = category,
            notes = notes.trim(),
            performedAt = performedAtMillis
        )
        val setEntities = sets.map {
            ExerciseSetEntity(
                workoutId = 0, // DAO rewrites this with the real id
                setNumber = it.setNumber,
                reps = it.reps,
                weightKg = it.weightKg
            )
        }
        return dao.insertWorkoutWithSets(entity, setEntities)
    }

    suspend fun deleteWorkout(workout: Workout) {
        dao.deleteWorkout(
            WorkoutEntity(
                id = workout.id,
                exerciseName = workout.exerciseName,
                category = workout.category,
                notes = workout.notes,
                performedAt = workout.performedAtMillis
            )
        )
    }

    suspend fun clear() = dao.clearAll()

    // --- helpers ---

    private fun startOfDay(millis: Long): Long = Calendar.getInstance().apply {
        timeInMillis = millis
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    private fun WorkoutWithSets.toDomain(): Workout = Workout(
        id = workout.id,
        exerciseName = workout.exerciseName,
        category = workout.category,
        notes = workout.notes,
        performedAtMillis = workout.performedAt,
        sets = sets.sortedBy { it.setNumber }.map {
            ExerciseSet(setNumber = it.setNumber, reps = it.reps, weightKg = it.weightKg)
        }
    )

    private companion object {
        val ONE_DAY: Long = TimeUnit.DAYS.toMillis(1)
    }
}