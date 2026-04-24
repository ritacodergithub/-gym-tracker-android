package com.example.e_commerce.data.local

// Stored as a String in Room via Converters. Add a new value here and Room
// will simply persist the new name — no migration needed.
enum class ExerciseCategory(val displayName: String) {
    CHEST("Chest"),
    BACK("Back"),
    LEGS("Legs"),
    SHOULDERS("Shoulders"),
    ARMS("Arms"),
    CORE("Core"),
    CARDIO("Cardio");

    companion object {
        fun fromName(name: String): ExerciseCategory =
            entries.firstOrNull { it.name == name } ?: CHEST
    }
}