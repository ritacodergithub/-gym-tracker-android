package com.example.e_commerce.data.repository

import com.example.e_commerce.data.local.Goal
import com.example.e_commerce.data.local.UserProfileDao
import com.example.e_commerce.data.local.UserProfileEntity
import com.example.e_commerce.data.local.WeightUnit
import kotlinx.coroutines.flow.Flow

class UserProfileRepository(private val dao: UserProfileDao) {

    fun observeProfile(): Flow<UserProfileEntity?> = dao.observe()

    suspend fun getProfile(): UserProfileEntity? = dao.get()

    suspend fun saveOnboarding(
        name: String,
        email: String,
        startingWeightKg: Float,
        goal: Goal
    ) {
        dao.upsert(
            UserProfileEntity(
                name = name.trim(),
                email = email.trim(),
                startingWeightKg = startingWeightKg,
                goal = goal
            )
        )
    }

    suspend fun updateUnit(unit: WeightUnit) {
        dao.get()?.let { dao.update(it.copy(weightUnit = unit)) }
    }

    suspend fun updateReminder(enabled: Boolean, hour: Int) {
        dao.get()?.let {
            dao.update(it.copy(reminderEnabled = enabled, reminderHour = hour))
        }
    }

    suspend fun updateGoal(goal: Goal) {
        dao.get()?.let { dao.update(it.copy(goal = goal)) }
    }

    suspend fun clear() = dao.clearAll()
}