package com.example.e_commerce.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// There's exactly one profile per install — row id is always 1. Using a
// fixed id means upsert-by-id is an idempotent insert-or-replace.
const val USER_PROFILE_ID: Long = 1L

enum class Goal(val label: String) {
    BUILD_MUSCLE("Build muscle"),
    LOSE_FAT("Lose fat"),
    GAIN_STRENGTH("Gain strength"),
    STAY_HEALTHY("Stay healthy")
}

enum class WeightUnit(val label: String) {
    KG("kg"), LB("lb");

    fun format(kg: Float): String {
        val value = if (this == KG) kg else kg * 2.2046f
        val rendered = if (value % 1f == 0f) value.toInt().toString() else "%.1f".format(value)
        return "$rendered $label"
    }
}

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Long = USER_PROFILE_ID,
    val name: String,
    val email: String = "",
    val startingWeightKg: Float,
    val goal: Goal = Goal.STAY_HEALTHY,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val reminderHour: Int = 18,   // 0–23, default 6 PM
    val reminderEnabled: Boolean = false,
    val onboardedAt: Long = System.currentTimeMillis()
)