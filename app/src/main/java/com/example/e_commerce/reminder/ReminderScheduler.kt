package com.example.e_commerce.reminder

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

// Schedules / cancels the once-a-day reminder. WorkManager handles
// persistence across reboots and app kills, so we just push the request
// and forget.
object ReminderScheduler {

    private const val WORK_NAME = "daily_reminder_work"

    fun schedule(context: Context, hourOfDay: Int) {
        val initialDelay = computeInitialDelayMs(hourOfDay)

        val request = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            // REPLACE so the cadence updates when the user changes the hour.
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }

    // Delay until the next occurrence of `hourOfDay:00` in local time.
    // If the target has already passed today, schedules for tomorrow.
    private fun computeInitialDelayMs(hourOfDay: Int): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= now.timeInMillis) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        return target.timeInMillis - now.timeInMillis
    }
}