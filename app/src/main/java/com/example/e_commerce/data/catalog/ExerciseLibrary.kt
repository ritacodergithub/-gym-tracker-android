package com.example.e_commerce.data.catalog

import com.example.e_commerce.data.local.ExerciseCategory
import com.example.e_commerce.data.local.ExerciseCategory.ARMS
import com.example.e_commerce.data.local.ExerciseCategory.BACK
import com.example.e_commerce.data.local.ExerciseCategory.CARDIO
import com.example.e_commerce.data.local.ExerciseCategory.CHEST
import com.example.e_commerce.data.local.ExerciseCategory.CORE
import com.example.e_commerce.data.local.ExerciseCategory.LEGS
import com.example.e_commerce.data.local.ExerciseCategory.SHOULDERS

// Seeded catalog — 40 fundamental exercises covering every muscle group.
// Instructions + tips give new users the "how" so they aren't guessing form
// in the gym. Curated; kept deliberately compact.
object ExerciseLibrary {

    val all: List<Exercise> = listOf(
        // --- CHEST ---
        Exercise(
            id = "bench_press",
            name = "Barbell Bench Press",
            category = CHEST,
            primaryMuscles = listOf("Chest", "Triceps", "Front Delts"),
            equipment = "Barbell, Bench",
            difficulty = Difficulty.INTERMEDIATE,
            instructions = listOf(
                "Lie flat on a bench, feet planted, eyes under the bar.",
                "Grip the bar slightly wider than shoulders, unrack with straight arms.",
                "Lower the bar to mid-chest with control, elbows tucked ~45°.",
                "Press up and slightly back until arms are straight."
            ),
            tips = listOf(
                "Keep shoulder blades pinched down and back the entire set.",
                "Never bounce the bar off your chest.",
                "Use a spotter or safety pins — solo lifting a heavy bench is how people get hurt."
            )
        ),
        Exercise(
            id = "incline_db_press",
            name = "Incline Dumbbell Press",
            category = CHEST,
            primaryMuscles = listOf("Upper Chest", "Front Delts"),
            equipment = "Dumbbells, Incline Bench",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Set bench to 30–45°.",
                "Start with dumbbells at chest level, palms forward.",
                "Press up until arms are straight but not locked.",
                "Lower under control to a slight stretch."
            ),
            tips = listOf(
                "Steeper incline = more shoulder, less chest. 30° hits the upper chest best.",
                "Don't clank the dumbbells together at the top — wastes stability work."
            )
        ),
        Exercise(
            id = "push_up",
            name = "Push-Up",
            category = CHEST,
            primaryMuscles = listOf("Chest", "Triceps", "Core"),
            equipment = "Bodyweight",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Hands slightly wider than shoulders, body in a straight line.",
                "Lower chest to just above the floor.",
                "Push up until arms are straight.",
                "Brace your core the whole time — no sagging hips."
            ),
            tips = listOf(
                "Too hard? Elevate your hands on a bench.",
                "Too easy? Elevate your feet or add a weight plate on your back."
            )
        ),
        Exercise(
            id = "dips",
            name = "Parallel Bar Dips",
            category = CHEST,
            primaryMuscles = listOf("Lower Chest", "Triceps"),
            equipment = "Parallel Bars",
            difficulty = Difficulty.INTERMEDIATE,
            instructions = listOf(
                "Grip parallel bars, arms straight, lean forward slightly.",
                "Lower until upper arms are roughly parallel to the floor.",
                "Press back up to straight arms."
            ),
            tips = listOf(
                "More forward lean = more chest. Upright = more triceps.",
                "If your shoulders pinch, stop before parallel."
            )
        ),
        Exercise(
            id = "cable_fly",
            name = "Cable Chest Fly",
            category = CHEST,
            primaryMuscles = listOf("Chest"),
            equipment = "Cable Machine",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Set cables to chest height, grab handles, step forward.",
                "Soft bend in elbows, arms out wide.",
                "Squeeze handles together in front of chest in an arc.",
                "Control the return — don't let the stack yank you back."
            ),
            tips = listOf("Imagine hugging a tree. No elbow bending mid-rep.")
        ),

        // --- BACK ---
        Exercise(
            id = "deadlift",
            name = "Conventional Deadlift",
            category = BACK,
            primaryMuscles = listOf("Lower Back", "Glutes", "Hamstrings", "Lats"),
            equipment = "Barbell",
            difficulty = Difficulty.ADVANCED,
            instructions = listOf(
                "Stand with feet hip-width, bar over mid-foot.",
                "Hinge at hips, grip bar just outside knees.",
                "Flat back, chest up, bar close to shins.",
                "Drive through heels, stand tall, lock out hips."
            ),
            tips = listOf(
                "This is the most humbling exercise — start light and nail form before adding weight.",
                "Bar should travel in a straight vertical line.",
                "If your lower back rounds, the weight is too heavy."
            )
        ),
        Exercise(
            id = "pull_up",
            name = "Pull-Up",
            category = BACK,
            primaryMuscles = listOf("Lats", "Biceps", "Rear Delts"),
            equipment = "Pull-Up Bar",
            difficulty = Difficulty.INTERMEDIATE,
            instructions = listOf(
                "Grip bar slightly wider than shoulders, palms away.",
                "Start from a dead hang, shoulders active.",
                "Pull chest toward the bar, driving elbows down.",
                "Lower under control to full extension."
            ),
            tips = listOf(
                "Can't do one yet? Use a band under your knees or the assisted-pullup machine.",
                "Think \"elbows to ribs\" rather than \"chin over bar\"."
            )
        ),
        Exercise(
            id = "barbell_row",
            name = "Bent-Over Barbell Row",
            category = BACK,
            primaryMuscles = listOf("Mid-Back", "Lats", "Rear Delts"),
            equipment = "Barbell",
            difficulty = Difficulty.INTERMEDIATE,
            instructions = listOf(
                "Hinge at hips until torso is 45°, flat back.",
                "Grip bar overhand, just wider than shoulders.",
                "Row bar to lower chest / upper belly.",
                "Lower under control, don't bounce off the floor."
            ),
            tips = listOf("Don't stand up as you row — the torso stays locked.")
        ),
        Exercise(
            id = "lat_pulldown",
            name = "Lat Pulldown",
            category = BACK,
            primaryMuscles = listOf("Lats", "Biceps"),
            equipment = "Cable Machine",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Sit, thighs under the pad, grip wide.",
                "Lean back ~10°, chest up.",
                "Pull bar to upper chest, elbows driving down.",
                "Control the bar back up."
            ),
            tips = listOf("Don't pull behind the neck — bad for shoulders.")
        ),
        Exercise(
            id = "seated_row",
            name = "Seated Cable Row",
            category = BACK,
            primaryMuscles = listOf("Mid-Back", "Lats"),
            equipment = "Cable Row",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Sit with feet on the platform, slight bend in knees.",
                "Chest up, shoulders down.",
                "Pull handle to your belly, squeezing shoulder blades.",
                "Extend arms fully between reps."
            ),
            tips = listOf("Don't use your lower back to heave the weight.")
        ),
        Exercise(
            id = "face_pull",
            name = "Face Pull",
            category = BACK,
            primaryMuscles = listOf("Rear Delts", "Upper Back"),
            equipment = "Cable, Rope",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Set cable to forehead height, attach a rope.",
                "Pull rope to your face, hands flaring out.",
                "Pause with elbows high, then return slowly."
            ),
            tips = listOf("Use light weight and high reps — this is a posture fixer.")
        ),

        // --- LEGS ---
        Exercise(
            id = "back_squat",
            name = "Barbell Back Squat",
            category = LEGS,
            primaryMuscles = listOf("Quads", "Glutes", "Hamstrings"),
            equipment = "Barbell, Rack",
            difficulty = Difficulty.INTERMEDIATE,
            instructions = listOf(
                "Bar rests on upper traps, feet shoulder-width.",
                "Brace core, descend by pushing hips back and bending knees.",
                "Drop until thighs are at least parallel.",
                "Drive through whole foot to stand."
            ),
            tips = listOf(
                "Knees track over toes — don't collapse inward.",
                "Squat to depth with lighter weight > half-squatting heavy weight."
            )
        ),
        Exercise(
            id = "front_squat",
            name = "Barbell Front Squat",
            category = LEGS,
            primaryMuscles = listOf("Quads", "Core"),
            equipment = "Barbell, Rack",
            difficulty = Difficulty.ADVANCED,
            instructions = listOf(
                "Bar on front delts, elbows high.",
                "Squat down keeping torso upright.",
                "Drive up through heels."
            ),
            tips = listOf("If your elbows drop, the bar rolls off — keep them pointed forward.")
        ),
        Exercise(
            id = "leg_press",
            name = "Leg Press",
            category = LEGS,
            primaryMuscles = listOf("Quads", "Glutes"),
            equipment = "Leg Press Machine",
            difficulty = Difficulty.
            BEGINNER,
            instructions = listOf(
                "Seat yourself, feet shoulder-width on platform.",
                "Unrack, lower platform until knees reach ~90°.",
                "Press back up, don't lock knees."
            ),
            tips = listOf(
                "Higher foot position = more glute/hamstring.",
                "Don't let lower back round off the seat."
            )
        ),
        Exercise(
            id = "romanian_deadlift",
            name = "Romanian Deadlift",
            category = LEGS,
            primaryMuscles = listOf("Hamstrings", "Glutes"),
            equipment = "Barbell",
            difficulty = Difficulty.INTERMEDIATE,
            instructions = listOf(
                "Start standing, bar at hips.",
                "Soft knees, push hips back, bar slides down thighs.",
                "Stop when you feel a strong hamstring stretch.",
                "Drive hips forward to stand."
            ),
            tips = listOf("This is a HIP HINGE, not a squat. Knees barely bend.")
        ),
        Exercise(
            id = "lunge",
            name = "Walking Lunge",
            category = LEGS,
            primaryMuscles = listOf("Quads", "Glutes"),
            equipment = "Dumbbells",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Hold dumbbells at sides, step forward.",
                "Lower until back knee nearly touches the floor.",
                "Drive up and step forward with the other leg."
            ),
            tips = listOf("Long steps hit glutes more, short steps hit quads more.")
        ),
        Exercise(
            id = "leg_extension",
            name = "Leg Extension",
            category = LEGS,
            primaryMuscles = listOf("Quads"),
            equipment = "Machine",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Seat yourself, pad rests on shins.",
                "Extend legs to full lockout, squeeze.",
                "Lower with control."
            ),
            tips = listOf("Slow negative reps make this way more effective.")
        ),
        Exercise(
            id = "leg_curl",
            name = "Lying Leg Curl",
            category = LEGS,
            primaryMuscles = listOf("Hamstrings"),
            equipment = "Machine",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Lie face down, pad just above ankles.",
                "Curl heels to glutes.",
                "Lower slowly."
            ),
            tips = listOf("Keep hips pressed into the pad.")
        ),
        Exercise(
            id = "calf_raise",
            name = "Standing Calf Raise",
            category = LEGS,
            primaryMuscles = listOf("Calves"),
            equipment = "Machine or Dumbbell",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Balls of feet on platform, heels hanging off.",
                "Rise to full tiptoe, hold 1 second.",
                "Drop into a deep stretch."
            ),
            tips = listOf("Full range matters more than heavy weight.")
        ),

        // --- SHOULDERS ---
        Exercise(
            id = "overhead_press",
            name = "Overhead Press",
            category = SHOULDERS,
            primaryMuscles = listOf("Front Delts", "Triceps", "Upper Chest"),
            equipment = "Barbell",
            difficulty = Difficulty.INTERMEDIATE,
            instructions = listOf(
                "Bar at front of shoulders, grip just outside shoulders.",
                "Brace core and glutes, press straight up.",
                "Head through the 'window' at the top.",
                "Lower to front delts under control."
            ),
            tips = listOf("Don't lean back at the hips — that's a back-dominant press.")
        ),
        Exercise(
            id = "lateral_raise",
            name = "Dumbbell Lateral Raise",
            category = SHOULDERS,
            primaryMuscles = listOf("Side Delts"),
            equipment = "Dumbbells",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Stand tall, slight forward lean, dumbbells at sides.",
                "Raise arms out to shoulder height, small elbow bend.",
                "Lead with elbows, not hands.",
                "Lower slowly."
            ),
            tips = listOf("Ego-lifting ruins this move. 6–10 kg is plenty for most people.")
        ),
        Exercise(
            id = "rear_delt_fly",
            name = "Rear Delt Fly",
            category = SHOULDERS,
            primaryMuscles = listOf("Rear Delts", "Upper Back"),
            equipment = "Dumbbells",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Hinge forward ~45°, dumbbells hanging.",
                "Raise arms out wide, squeezing shoulder blades.",
                "Lower slowly."
            ),
            tips = listOf("Light weight, high reps. Most people train rear delts way too rarely.")
        ),
        Exercise(
            id = "arnold_press",
            name = "Arnold Press",
            category = SHOULDERS,
            primaryMuscles = listOf("Front + Side Delts"),
            equipment = "Dumbbells",
            difficulty = Difficulty.INTERMEDIATE,
            instructions = listOf(
                "Start with dumbbells at chin height, palms facing you.",
                "Rotate palms forward while pressing overhead.",
                "Reverse the motion on the way down."
            ),
            tips = listOf("Go lighter than regular OHP — the rotation is challenging.")
        ),

        // --- ARMS ---
        Exercise(
            id = "barbell_curl",
            name = "Barbell Curl",
            category = ARMS,
            primaryMuscles = listOf("Biceps"),
            equipment = "Barbell",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Stand tall, barbell at thighs, shoulder-width grip.",
                "Curl bar to shoulders, elbows pinned at sides.",
                "Squeeze at the top, lower with control."
            ),
            tips = listOf("If your torso swings, the weight is too heavy.")
        ),
        Exercise(
            id = "hammer_curl",
            name = "Dumbbell Hammer Curl",
            category = ARMS,
            primaryMuscles = listOf("Biceps", "Brachialis", "Forearms"),
            equipment = "Dumbbells",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Stand with dumbbells at sides, palms facing in (neutral).",
                "Curl up without rotating the wrist.",
                "Lower slowly."
            ),
            tips = listOf("Best bang for the buck for thick-looking arms.")
        ),
        Exercise(
            id = "tricep_pushdown",
            name = "Tricep Pushdown",
            category = ARMS,
            primaryMuscles = listOf("Triceps"),
            equipment = "Cable",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Stand close to a high pulley, grip rope or bar.",
                "Elbows pinned at ribs.",
                "Extend down to full lockout, squeeze.",
                "Return to 90° — don't let elbows flare up."
            ),
            tips = listOf("Rope + flare at the bottom = more long-head engagement.")
        ),
        Exercise(
            id = "skullcrusher",
            name = "Skullcrusher",
            category = ARMS,
            primaryMuscles = listOf("Triceps"),
            equipment = "EZ Bar",
            difficulty = Difficulty.INTERMEDIATE,
            instructions = listOf(
                "Lie on bench, EZ bar pressed up.",
                "Hinge elbows, lower bar toward forehead.",
                "Extend back to start."
            ),
            tips = listOf("Elbows stay put — only forearms move. Start light, the name is a warning.")
        ),
        Exercise(
            id = "concentration_curl",
            name = "Concentration Curl",
            category = ARMS,
            primaryMuscles = listOf("Biceps Peak"),
            equipment = "Dumbbell",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Sit, elbow braced against inner thigh.",
                "Curl dumbbell to shoulder, squeeze hard.",
                "Lower with control."
            ),
            tips = listOf("Single best peak exercise. Feel every rep.")
        ),

        // --- CORE ---
        Exercise(
            id = "plank",
            name = "Plank",
            category = CORE,
            primaryMuscles = listOf("Core", "Shoulders"),
            equipment = "Bodyweight",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Forearms and toes on the floor, elbows under shoulders.",
                "Body in a straight line — don't sag or pike.",
                "Hold while breathing normally."
            ),
            tips = listOf("Quality over time. A 30s perfect plank beats a sloppy 2 minutes.")
        ),
        Exercise(
            id = "hanging_leg_raise",
            name = "Hanging Leg Raise",
            category = CORE,
            primaryMuscles = listOf("Lower Abs", "Hip Flexors"),
            equipment = "Pull-Up Bar",
            difficulty = Difficulty.INTERMEDIATE,
            instructions = listOf(
                "Hang from bar, shoulders active.",
                "Raise knees (or straight legs) to chest.",
                "Lower slowly, no swinging."
            ),
            tips = listOf("If you swing, you're using momentum — harder version, smaller reps.")
        ),
        Exercise(
            id = "ab_wheel",
            name = "Ab Wheel Rollout",
            category = CORE,
            primaryMuscles = listOf("Core"),
            equipment = "Ab Wheel",
            difficulty = Difficulty.ADVANCED,
            instructions = listOf(
                "Kneel, grip the wheel, hips over knees.",
                "Roll forward as far as you can control.",
                "Brace hard and pull yourself back."
            ),
            tips = listOf("Start with partial range — full rollouts are genuinely difficult.")
        ),
        Exercise(
            id = "cable_crunch",
            name = "Cable Crunch",
            category = CORE,
            primaryMuscles = listOf("Upper Abs"),
            equipment = "Cable, Rope",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Kneel under a high pulley, rope at sides of head.",
                "Crunch down, elbows toward thighs.",
                "Keep hips still."
            ),
            tips = listOf("It's a spine curl, not a hip fold.")
        ),

        // --- CARDIO ---
        Exercise(
            id = "run",
            name = "Running",
            category = CARDIO,
            primaryMuscles = listOf("Full Body"),
            equipment = "None",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Log as sets × minutes. Reps = minutes, weight = 0.",
                "Warm up 5 min, hold pace, cool down 5 min."
            ),
            tips = listOf("Build volume gradually — no more than 10% weekly mileage increase.")
        ),
        Exercise(
            id = "cycling",
            name = "Cycling",
            category = CARDIO,
            primaryMuscles = listOf("Legs", "Heart"),
            equipment = "Bike",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Steady-state or intervals. Log minutes as reps.",
                "Adjust seat so knee is slightly bent at the pedal's bottom."
            ),
            tips = listOf("Low-impact — great when lower-body lifts are beating you up.")
        ),
        Exercise(
            id = "rowing",
            name = "Rowing Machine",
            category = CARDIO,
            primaryMuscles = listOf("Back", "Legs", "Core"),
            equipment = "Erg",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Legs push first, then hips open, then arms pull.",
                "Reverse on the return: arms, hips, legs."
            ),
            tips = listOf("If your back is burning but legs aren't, your stroke is arms-dominant.")
        ),
        Exercise(
            id = "jump_rope",
            name = "Jump Rope",
            category = CARDIO,
            primaryMuscles = listOf("Calves", "Shoulders"),
            equipment = "Rope",
            difficulty = Difficulty.BEGINNER,
            instructions = listOf(
                "Small bounces off the balls of your feet.",
                "Wrists do the work, not the shoulders."
            ),
            tips = listOf("Amazing warm-up; 2–3 minutes gets the whole body primed.")
        )
    )

    private val byId: Map<String, Exercise> = all.associateBy { it.id }
    fun find(id: String): Exercise? = byId[id]

    fun byCategory(category: ExerciseCategory): List<Exercise> =
        all.filter { it.category == category }

    fun search(query: String): List<Exercise> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return all
        return all.filter {
            it.name.lowercase().contains(q) ||
                it.primaryMuscles.any { m -> m.lowercase().contains(q) }
        }
    }
}