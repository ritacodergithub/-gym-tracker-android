package com.example.e_commerce

import android.app.Application
import com.example.e_commerce.data.local.GymDatabase
import com.example.e_commerce.data.repository.BodyWeightRepository
import com.example.e_commerce.data.repository.UserProfileRepository
import com.example.e_commerce.data.repository.WorkoutRepository

// Application class. Acts as a tiny manual DI container: expose the DB and
// repositories as `lazy` singletons so ViewModels can grab them via the
// AppViewModelProvider factory. Swap to Hilt later — same shape + annotations.
class GymApp : Application() {

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
}