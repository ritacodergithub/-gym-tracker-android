package com.example.e_commerce.data.catalog

// Mapping from our Exercise.id → yuhonas/free-exercise-db directory slug.
// The DB is MIT-licensed and ships two JPG frames per exercise in
//   exercises/<slug>/images/0.jpg and 1.jpg
// Alternating between them gives an animated preview.
//
// If a slug is missing or the URL 404s, the UI falls back to the YouTube
// button. Safe to leave mappings as null during development — fill in as
// you verify each slug against the upstream repo.
//
// Upstream repo: https://github.com/yuhonas/free-exercise-db
object FreeExerciseDb {

    private const val BASE =
        "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises"

    // Best-guess slugs based on the upstream naming convention (Title_Case
    // with underscores). Verify each when you first launch — uncovered or
    // wrong mappings just cause a graceful YouTube fallback.
    private val SLUG_BY_EXERCISE_ID: Map<String, String> = mapOf(
        // --- CHEST ---
        "bench_press" to "Barbell_Bench_Press_-_Medium_Grip",
        "incline_db_press" to "Dumbbell_Bench_Press",
        "push_up" to "Pushups",
        "dips" to "Dips_-_Chest_Version",
        "cable_fly" to "Cable_Crossover",

        // --- BACK ---
        "deadlift" to "Barbell_Deadlift",
        "pull_up" to "Pullups",
        "barbell_row" to "Bent_Over_Barbell_Row",
        "lat_pulldown" to "Wide-Grip_Lat_Pulldown",
        "seated_row" to "Seated_Cable_Rows",
        "face_pull" to "Face_Pull",

        // --- LEGS ---
        "back_squat" to "Barbell_Squat",
        "front_squat" to "Front_Barbell_Squat",
        "leg_press" to "Leg_Press",
        "romanian_deadlift" to "Romanian_Deadlift",
        "lunge" to "Dumbbell_Lunges",
        "leg_extension" to "Leg_Extensions",
        "leg_curl" to "Lying_Leg_Curls",
        "calf_raise" to "Standing_Calf_Raises",

        // --- SHOULDERS ---
        "overhead_press" to "Standing_Military_Press",
        "lateral_raise" to "Side_Lateral_Raise",
        "rear_delt_fly" to "Bent-Arm_Lateral_Raise",
        "arnold_press" to "Arnold_Dumbbell_Press",

        // --- ARMS ---
        "barbell_curl" to "Barbell_Curl",
        "hammer_curl" to "Hammer_Curls",
        "tricep_pushdown" to "Triceps_Pushdown",
        "skullcrusher" to "EZ-Bar_Skullcrusher",
        "concentration_curl" to "Concentration_Curls",

        // --- CORE ---
        "plank" to "Plank",
        "hanging_leg_raise" to "Hanging_Leg_Raise",
        "ab_wheel" to "Ab_Roller",
        "cable_crunch" to "Cable_Crunch"

        // Cardio entries intentionally omitted — a picture of someone
        // running doesn't add much to "run for 20 minutes".
    )

    data class PreviewFrames(val frame0: String, val frame1: String)

    // Returns the two frame URLs if this exercise is mapped, else null.
    fun previewFramesFor(exerciseId: String): PreviewFrames? {
        val slug = SLUG_BY_EXERCISE_ID[exerciseId] ?: return null
        return PreviewFrames(
            frame0 = "$BASE/$slug/images/0.jpg",
            frame1 = "$BASE/$slug/images/1.jpg"
        )
    }
}