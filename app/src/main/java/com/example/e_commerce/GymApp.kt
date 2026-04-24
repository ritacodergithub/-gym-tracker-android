package com.example.e_commerce

import android.app.Application
import com.example.e_commerce.data.local.GymDatabase
import com.example.e_commerce.data.auth.AuthRepository
import com.example.e_commerce.data.repository.AiCoachRepository
import com.example.e_commerce.data.repository.BodyWeightRepository
import com.example.e_commerce.data.repository.UserProfileRepository
import com.example.e_commerce.data.repository.WorkoutRepository
import com.example.e_commerce.reminder.ReminderWorker

// Application class. Acts as a tiny manual DI container: expose the DB and
// repositories as `lazy` singletons so ViewModels can grab them via the
// AppViewModelProvider factory. Swap to Hilt later — same shape + annotations.
class GymApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Idempotent — the notification channel must exist before any
        // reminder posts. Doing it at app start avoids a race on first
        // WorkManager run.
        ReminderWorker.ensureChannel(this)
    }

    val database: GymDatabase by lazy { GymDatabase.getInstance(this) }

    val workoutRepository: WorkoutRepository by lazy {
        WorkoutRepository(database.workoutDao())
    }

    val bodyWeightRepository: BodyWeightRepository by lazy {
        BodyWeightRepository(database.bodyWeightDao())
    }

    val userProfileRepository: UserProfileRepository by lazy {
        UserProfileRepository(database.userProfileDao())
    }

    val aiCoachRepository: AiCoachRepository by lazy { AiCoachRepository() }

    val authRepository: AuthRepository by lazy { AuthRepository() }
}