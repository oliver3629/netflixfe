# Netflix Android App

An Android Netflix-style application built with Kotlin and Jetpack Compose. The app connects to the companion Ktor backend to display videos, profiles, favorites, and fullscreen playback.

**Backend:** [oliver3629/netflix](https://github.com/oliver3629/netflix) — the companion Ktor server that provides video, profile, and media data.

## Features

- Browse featured content and categorized video sections
- View movie details and TV show episodes
- Manage favorites with local persistence
- View profile and recently watched content
- Play videos in fullscreen

## Tech Stack

| Area | Technology |
| --- | --- |
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM |
| Dependency injection | Hilt |
| Networking | Retrofit and Gson |
| Local storage | Room |
| Image loading | Coil |
| Video playback | Media3 ExoPlayer |

## Environment Setup

1. Download **Android Studio Panda 2 | 2025.3.2 — March 3, 2026** from the [Android Studio archive](https://developer.android.com/studio/archive). This exact version is required; do not use a different Android Studio version. Install it with the standard setup and use an installation path containing English characters only.
2. Open **SDK Manager** and install:
   - Android SDK Platform 36
   - Android SDK Build-Tools
   - Android SDK Command-line Tools
   - Android SDK Platform-Tools
   - Android Emulator
3. Open **Device Manager**, create a Pixel virtual device, and select an Android system image. API 31 or newer is supported; API 36 is recommended when available.
4. Start the emulator, open the project, wait for Gradle sync to finish, and run the `app` configuration.

## Code Structure

```text
app/src/main/java/com/laioffer/netflix/
├── MainActivity.kt          # Application entry activity
├── NetflixApplication.kt    # Hilt application setup
├── database/                # Room database, DAO, and entities
├── datamodel/               # API request and response models
├── di/                      # Hilt modules for network, database, and player
├── navigation/              # Routes and Compose navigation graph
├── network/                 # Retrofit API definitions
├── player/                  # Media3 player screen and ViewModel
├── repository/              # Network and local-data repositories
└── ui/
    ├── components/          # Reusable Compose UI components
    ├── home/                # Home screen and ViewModel
    ├── profile/             # Profile screen and ViewModel
    ├── theme/               # Colors, typography, and spacing
    └── videodetail/         # Video detail screen and ViewModel
```

Additional project configuration:

```text
app/build.gradle.kts         # Android module and dependencies
gradle/libs.versions.toml    # Centralized dependency versions
app/src/main/AndroidManifest.xml
app/src/main/res/            # Strings, themes, icons, and XML configuration
```

<br><br>
