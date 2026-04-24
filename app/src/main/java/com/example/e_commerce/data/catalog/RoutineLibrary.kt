package com.example.e_commerce.data.catalog

// Four proven programs. Keeps users from staring at a blank screen wondering
// "what do I do today?" — they pick a routine and follow it day-by-day.
object RoutineLibrary {

    val all: List<Routine> = listOf(

        Routine(
            id = "full_body_3d",
            name = "Full Body — Beginner",
            summary = "Three full-body sessions per week. The fastest way to build a base.",
            daysPerWeek = 3,
            level = Difficulty.BEGINNER,
            days = listOf(
                RoutineDay(
                    name = "Day A",
                    exercises = listOf(
                        RoutineExercise("back_squat", sets = 3, reps = "5"),
                        RoutineExercise("bench_press", sets = 3, reps = "5"),
                        RoutineExercise("barbell_row", sets = 3, reps = "5"),
                        RoutineExercise("plank", sets = 3, reps = "30s")
                    )
                ),
                RoutineDay(
                    name = "Day B",
                    exercises = listOf(
                        RoutineExercise("back_squat", sets = 3, reps = "5"),
                        RoutineExercise("overhead_press", sets = 3, reps = "5"),
                        RoutineExercise("deadlift", sets = 1, reps = "5", notes = "One heavy top set"),
                        RoutineExercise("hanging_leg_raise", sets = 3, reps = "8–12")
                    )
                )
            )
        ),

        Routine(
            id = "ppl_6d",
            name = "Push / Pull / Legs",
            summary = "6-day split. Hits every muscle twice a week. For people who love the gym.",
            daysPerWeek = 6,
            level = Difficulty.INTERMEDIATE,
            days = listOf(
                RoutineDay(
                    name = "Push",
                    exercises = listOf(
                        RoutineExercise("bench_press", sets = 4, reps = "6–8"),
                        RoutineExercise("incline_db_press", sets = 3, reps = "8–10"),
                        RoutineExercise("overhead_press", sets = 3, reps = "6–8"),
                        RoutineExercise("lateral_raise", sets = 4, reps = "12–15"),
                        RoutineExercise("tricep_pushdown", sets = 3, reps = "10–12"),
                        RoutineExercise("skullcrusher", sets = 3, reps = "8–10")
                    )
                ),
                RoutineDay(
                    name = "Pull",
                    exercises = listOf(
                        RoutineExercise("deadlift", sets = 3, reps = "5"),
                        RoutineExercise("pull_up", sets = 4, reps = "6–10"),
                        RoutineExercise("barbell_row", sets = 3, reps = "8"),
                        RoutineExercise("face_pull", sets = 3, reps = "15"),
                        RoutineExercise("barbell_curl", sets = 3, reps = "8–10"),
                        RoutineExercise("hammer_curl", sets = 3, reps = "10–12")
                    )
                ),
                RoutineDay(
                    name = "Legs",
                    exercises = listOf(
                        RoutineExercise("back_squat", sets = 4, reps = "6–8"),
                        RoutineExercise("romanian_deadlift", sets = 3, reps = "8"),
                        RoutineExercise("leg_press", sets = 3, reps = "10–12"),
                        RoutineExercise("leg_curl", sets = 3, reps = "10–12"),
                        RoutineExercise("leg_extension", sets = 3, reps = "12–15"),
                        RoutineExercise("calf_raise", sets = 4, reps = "12–15")
                    )
                )
            )
        ),

        Routine(
            id = "upper_lower_4d",
            name = "Upper / Lower",
            summary = "4 days. Balanced split for most people with a job.",
            daysPerWeek = 4,
            level = Difficulty.INTERMEDIATE,
            days = listOf(
                RoutineDay(
                    name = "Upper",
                    exercises = listOf(
                        RoutineExercise("bench_press", sets = 4, reps = "6–8"),
                        RoutineExercise("barbell_row", sets = 4, reps = "6–8"),
                        RoutineExercise("overhead_press", sets = 3, reps = "8"),
                        RoutineExercise("lat_pulldown", sets = 3, reps = "10"),
                        RoutineExercise("barbell_curl", sets = 3, reps = "10"),
                        RoutineExercise("tricep_pushdown", sets = 3, reps = "10")
                    )
                ),
                RoutineDay(
                    name = "Lower",
                    exercises = listOf(
                        RoutineExercise("back_squat", sets = 4, reps = "6–8"),
                        RoutineExercise("romanian_deadlift", sets = 3, reps = "8"),
                        RoutineExercise("leg_press", sets = 3, reps = "10–12"),
                        RoutineExercise("leg_curl", sets = 3, reps = "10"),
                        RoutineExercise("calf_raise", sets = 4, reps = "15"),
                        RoutineExercise("plank", sets = 3, reps = "45s")
                    )
                )
            )
        ),

        Routine(
            id = "starting_strength",
            name = "Starting Strength",
            summary = "Barbell-only, 3 days, linear progression. Legendary for raw strength.",
            daysPerWeek = 3,
            level = Difficulty.BEGINNER,
            days = listOf(
                RoutineDay(
                    name = "Workout A",
                    exercises = listOf(
                        RoutineExercise("back_squat", sets = 3, reps = "5"),
                        RoutineExercise("bench_press", sets = 3, reps = "5"),
                        RoutineExercise("deadlift", sets = 1, reps = "5")
                    )
                ),
                RoutineDay(
                    name = "Workout B",
                    exercises = listOf(
                        RoutineExercise("back_squat", sets = 3, reps = "5"),
                        RoutineExercise("overhead_press", sets = 3, reps = "5"),
                        RoutineExercise("barbell_row", sets = 3, reps = "5")
                    )
                )
            )
        )
    )

    fun find(id: String): Routine? = all.firstOrNull { it.id == id }
}