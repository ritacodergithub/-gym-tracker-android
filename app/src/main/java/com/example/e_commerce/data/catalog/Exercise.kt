package com.example.e_commerce.data.catalog

import com.example.e_commerce.data.local.ExerciseCategory

enum class Difficulty(val label: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced")
}

// Static, read-only description of an exercise. Not a Room entity — this is
// guidance content shipped with the app. User-logged workouts reference
// exercises by name so the catalog can evolve without DB migrations.
data class Exercise(
    val id: String,
    val name: String,
    val category: ExerciseCategory,
    val primaryMuscles: List<String>,
    val equipment: String,
    val difficulty: Difficulty,
    val instructions: List<String>, // numbered steps
    val tips: List<String>,         // form cues / common mistakes
    // Optional curated YouTube video ID (the 11-char string after `v=` in a
    // YouTube URL). When set, VideoPlayerSheet plays it inline. When null, it
    // falls back to a YouTube search for "<name> proper form". You can curate
    // these over time — the catalog keeps working without any IDs filled in.
    val videoId: String? = null
)