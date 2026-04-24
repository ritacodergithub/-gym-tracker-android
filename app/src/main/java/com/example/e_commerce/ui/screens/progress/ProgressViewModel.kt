package com.example.e_commerce.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ProgressViewModel(
    repository: WorkoutRepository
) : ViewModel() {

    data class DailyVolume(val dayLabel: String, val dayMillis: Long, val volume: Float)

    data class UiState(
        val totalWorkouts: Int = 0,
        val totalVolume: Float = 0f,
        val last7Days: List<DailyVolume> = emptyList()
    )

    private val sevenDaysAgo: Long =
        System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)

    val state: StateFlow<UiState> = repository.observeWorkoutsSince(sevenDaysAgo)
        .map { workouts ->
            // Bucket each workout's volume into its calendar day (midnight).
            val byDay = workouts
                .groupBy { startOfDay(it.performedAtMillis) }
                .mapValues { (_, ws) -> ws.sumOf { it.totalVolume.toDouble() }.toFloat() }

            // Fill in missing days so the chart always has 7 bars, oldest → newest.
            val days = (6 downTo 0).map { offset ->
                val dayMillis = startOfDay(System.currentTimeMillis()) -
                    TimeUnit.DAYS.toMillis(offset.toLong())
                DailyVolume(
                    dayLabel = shortDay(dayMillis),
                    dayMillis = dayMillis,
                    volume = byDay[dayMillis] ?: 0f
                )
            }
            UiState(
                totalWorkouts = workouts.size,
                totalVolume = workouts.sumOf { it.totalVolume.toDouble() }.toFloat(),
                last7Days = days
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState()
        )

    private fun startOfDay(millis: Long): Long = Calendar.getInstance().apply {
        timeInMillis = millis
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    private fun shortDay(millis: Long): String {
        val cal = Calendar.getInstance().apply { timeInMillis = millis }
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            else -> "Sat"
        }
    }
}