# Gym Tracker

A modern Android fitness tracker with a smart workout log, built-in exercise library, rest timer, streaks, personal records, body-weight tracking, pre-built training routines, a Gemini-powered AI coach, and Google Sign-In.

Built with **Kotlin**, **Jetpack Compose**, **Room**, and **Material 3** — single-activity, MVVM architecture, dark-first neon UI.

## Features

### Training
- **Smart workout logging** — exercise picker from a bundled library, auto "last time you did this" hint, dynamic set/rep/weight rows.
- **40+ exercises** with form instructions, coaching tips, difficulty tags.
- **Offline exercise previews** — animated two-frame JPG previews via the MIT-licensed [Free-Exercise-DB](https://github.com/yuhonas/free-exercise-db). Cached by Coil after first view.
- **Free YouTube tutorials** — primary "Watch full tutorial" opens the YouTube app (or browser) to a specific video or search.
- **4 pre-built routines** — Full Body Beginner, Push/Pull/Legs, Upper/Lower, Starting Strength. Expandable per-day view with target sets × reps.
- **Rest timer** — bottom sheet with 60/90/120/180s presets, circular progress, haptic buzz on completion.

### Progress & motivation
- **Streaks + Personal Records** — auto-computed from your log. Dashboard shows current streak ring and top 5 PRs.
- **Weekly progress chart** — 7-day volume bar chart with gradient bars.
- **Body weight tracking** — daily weigh-ins with trend line + history list.
- **AI coach (Gemini)** — generates today's gym plan and motivational nudges tuned to your goal. Gracefully surfaces `NoApiKey`/`429`/`404` states with human-readable hints.
- **Daily reminder** — configurable hour, persists across reboots via WorkManager. Requests `POST_NOTIFICATIONS` on Android 13+.

### Account & auth
- **Onboarding** — name, starting weight, goal. Profile stored locally in Room.
- **Google Sign-In** — Firebase Auth via Credential Manager. Pre-fills name + email on success. Falls back gracefully if Firebase isn't configured.

### UI
- **Dark-first neon theme** — deep cosmos backdrop, electric purple / cyan / lime accents, gradient hero text.
- **Glassmorphism cards** — translucent fills with gradient borders; works on all API levels (no RenderEffect blur required).
- **Floating pill bottom nav** — selected tab expands with a gradient fill.
- **Pulsing glow FAB** — primary "Log workout" action with an animated halo.
- **Animated stat rings** — smooth tweens when streak / PRs update.

## Architecture

```
app/src/main/java/com/example/e_commerce/
├── GymApp.kt                      # Application class, manual DI container
├── MainActivity.kt                # Single-activity Compose entry point
├── data/
│   ├── ai/                        # Retrofit client + models for Gemini
│   ├── auth/                      # Firebase + Credential Manager wrapper
│   ├── catalog/                   # Static exercise + routine + slug maps
│   ├── local/                     # Room: entities, DAOs, database
│   └── repository/                # Workout, BodyWeight, UserProfile, AiCoach
├── domain/model/                  # UI-facing models (Workout, PersonalRecord)
├── reminder/                      # WorkManager: ReminderWorker + Scheduler
└── ui/
    ├── components/                # GlassCard, GradientButton, StatRing,
    │                              # FloatingPillNav, GlowFab, RestTimerSheet,
    │                              # ExercisePreviewSheet, PageBackground
    ├── navigation/                # Screen routes + NavHost
    ├── theme/                     # Dark neon palette, gradients, typography
    └── screens/
        ├── onboarding/            # First-launch profile + Google Sign-In
        ├── dashboard/             # Hero greeting, streak ring, PR glass cards
        ├── home/                  # Bottom-nav shell
        ├── addworkout/            # Log form with library picker + rest timer
        ├── aicoach/               # Gemini plan generator + motivation
        ├── bodyweight/            # Weigh-in + trend chart
        ├── history/               # Past workouts with volume + delete
        ├── progress/              # Stat cards + weekly bar chart
        ├── routines/              # 4 seeded programs
        ├── exerciselibrary/       # Searchable catalog + picker
        └── settings/              # Units, goal, reminder, danger zone
```

## Tech stack

- **Kotlin 2.0** with Jetpack Compose (BOM 2024.12)
- **Room 2.6** for offline-first local persistence
- **Navigation Compose** for single-activity routing
- **KSP** for annotation processing
- **Coroutines + Flow** throughout the data layer
- **Retrofit + Moshi + OkHttp** for the Gemini API
- **WorkManager** for the daily reminder
- **Coil** for cached image loading (exercise previews)
- **Firebase Auth + Credential Manager** for Google Sign-In
- **Material 3** + custom components

## Getting started

1. **Clone** the repo.
2. **Open** in Android Studio (Hedgehog or newer).
3. **Configure optional integrations** (all three are optional — the app runs without them):
   - **Gemini (AI coach)** — get a free key at [aistudio.google.com](https://aistudio.google.com/apikey). Add to `local.properties`:
     ```
     gemini.api.key=AIzaSy...
     ```
   - **Firebase (Google Sign-In)** — create a project at [console.firebase.google.com](https://console.firebase.google.com/), add an Android app (package `com.example.e_commerce`), download `google-services.json` into `app/`, enable Google sign-in under Authentication → Sign-in method, and paste the Web Client ID into `AuthRepository.kt`.
   - **Exercise previews** use raw URLs from Free-Exercise-DB — no setup needed.
4. **Run** on a device or emulator (min SDK 24 / Android 7.0, target 36).

## Gradle + Keys

`local.properties` is gitignored. The build reads `gemini.api.key` from it and exposes it as `BuildConfig.GEMINI_API_KEY`. If the key is absent, the AI coach screen shows a friendly setup hint instead of crashing.

`google-services.json` is committed for convenience. The Firebase API key inside is client-side and safe to publish, but **should be SHA-1 restricted in Google Cloud Console** before a public release to prevent quota abuse.

## Roadmap

- [x] Firebase Auth (Google Sign-In)
- [x] Gemini-powered AI coach (free text plans + motivation)
- [x] Daily reminder notification via WorkManager
- [x] Offline exercise previews (animated frames via Free-Exercise-DB)
- [ ] Cloud sync via Firestore
- [ ] Guided workout session mode (step through a routine)
- [ ] User-created routines
- [ ] Search & filter in history
- [ ] Share workout card (canvas-rendered image)
- [ ] Play Store release (package rename, signing, privacy policy)

## Screenshots

_TODO: add screenshots of Dashboard, Routines, Exercise Library, AI coach, Settings._

## License

Personal project — pick one before publishing (MIT / Apache 2.0 recommended).