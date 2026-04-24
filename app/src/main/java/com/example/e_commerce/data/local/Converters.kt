package com.example.e_commerce.data.local

import androidx.room.TypeConverter

// Room can't persist enums out of the box — convert each to a stable String.
class Converters {
    @TypeConverter
    fun fromCategory(category: ExerciseCategory): String = category.name

    @TypeConverter
    fun toCategory(name: String): ExerciseCategory = ExerciseCategory.fromName(name)

    @TypeConverter
    fun fromGoal(goal: Goal): String = goal.name

    @TypeConverter
    fun toGoal(name: String): Goal = runCatching { Goal.valueOf(name) }.getOrDefault(Goal.STAY_HEALTHY)

    @TypeConverter
    fun fromUnit(unit: WeightUnit): String = unit.name

    @TypeConverter
    fun toUnit(name: String): WeightUnit = runCatching { WeightUnit.valueOf(name) }.getOrDefault(WeightUnit.KG)
}