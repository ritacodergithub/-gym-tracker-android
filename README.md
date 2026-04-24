# Gym Tracker

A modern Android fitness tracker with a smart workout log, built-in exercise library, rest timer, streaks, personal records, body-weight tracking, and pre-built training routines.

Built with **Kotlin**, **Jetpack Compose**, **Room**, and **Material 3** — single-activity, MVVM architecture, dark-first neon UI.

## Features

- **Smart workout logging** — exercise picker from a bundled library, auto "last time you did this" hint, dynamic sets with reps × weight.
- **40+ exercises** with form instructions, coaching tips, difficulty tags, and free YouTube video tutorials (opens in the YouTube app / browser).
- **4 pre-built routines** — Full Body Beginner, Push/Pull/Legs, Upper/Lower, Starting Strength.
- **Rest timer** — bottom sheet with 60/90/120/180s presets, haptic buzz on completion.
- **Body weight tracking** — daily weigh-ins with a trend line chart.
- **Streaks + Personal Records** — auto-computed from your log. Dashboard shows current streak and top PRs.
- **Weekly progress chart** — 7-day volume bar chart.
- **Onboarding** — name, starting weight, training goal.
- **Settings** — units (kg/lb), goal, daily reminder time, reset data.

## Architecture

```
app/src/main/java/com/example/e_commerce/
├── GymApp.kt                   # Application class, manual DI
├── MainActivity.kt             # Single-activity Compose entry point
├── data/
│   ├── catalog/                # Static exercise + routine libraries
│   ├── local/                  # Room: entities, DAOs, database
│   └── repository/             # Workout, BodyWeight, UserProfile repos
├── domain/model/               # UI-facing models (Workout, PersonalRecord)
└── ui/
    ├── components/             # Reusable: GlassCard, GradientButton, StatRing, FloatingPillNav, GlowFab, RestTimerSheet
    ├── navigation/             # Screen routes + NavHost
    ├── theme/                  # Dark neon palette, gradients, typography
    └── screens/
        ├── onboarding/
        ├── dashboard/
        ├── home/               # Bottom-nav shell
        ├── addworkout/
        ├── history/
        ├── progress/
        ├── bodyweight/
        ├── routines/
        ├── exerciselibrary/
        └── settings/
```

## Tech stack

- **Kotlin 2.0** with Jetpack Compose (BOM 2024.12)
- **Room 2.6** for local persistence (offline-first)
- **Navigation Compose** for single-activity routing
- **KSP** for annotation processing
- **Coroutines + Flow** throughout the data layer
- No network dependencies in the MVP

## Getting started

1. Clone the repo.
2. Open in Android Studio (Hedgehog or newer).
3. Let Gradle sync (first sync pulls Compose BOM + Room).
4. Run on a device or emulator (min SDK 24 / Android 7.0).

## Roadmap

- [ ] Firebase Auth (Google Sign-In)
- [ ] Cloud sync via Firestore
- [ ] Gemini-powered AI coach (free text plans)
- [ ] Daily reminder notification via WorkManager
- [ ] Bundled offline exercise GIFs as YouTube fallback
- [ ] Play Store release

## License

Personal project — pick one before publishing (MIT / Apache 2.0 recommended).
