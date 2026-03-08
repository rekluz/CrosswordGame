# Pandacrostic - Android App

A crossword puzzle game for Android inspired by Netflix Puzzled, built in Kotlin. This is my very first attempt at creating an android app so it's very much a work in progress. 
The main structure is laid out but puzzles are not working properly. Experimental builds for now.

## 

## How to Build & Run

### Requirements
- Android Studio (Hedgehog or newer)
- Android SDK 34
- Kotlin 1.9+
- A physical Android device or emulator (API 24+)

### Steps
1. Open **Android Studio**
2. Choose **"Open an Existing Project"**
3. Navigate to the `CrosswordGame` folder and open it
4. Wait for Gradle to sync (this may take a minute)
5. Connect your Android device via USB (enable USB debugging in Developer Options)
   OR launch an emulator from the AVD Manager
6. Click the green **▶ Run** button

### Build APK (for sideloading)
1. In Android Studio: **Build → Build Bundle(s)/APK(s) → Build APK(s)**
2. The APK will be at: `app/build/outputs/apk/debug/app-debug.apk`
3. Transfer to your phone and install (enable "Install from unknown sources")



## Project Structure
```
CrosswordGame/
├── app/src/main/
│   ├── java/com/crosswordgame/
│   │   ├── MainActivity.kt          # Main menu
│   │   ├── GameActivity.kt          # Game screen
│   │   ├── LevelSelectActivity.kt   # Level picker
│   │   ├── CrosswordGridView.kt     # Custom grid drawing
│   │   ├── PuzzleEngine.kt          # Game logic
│   │   ├── ClueAdapter.kt           # Clue list RecyclerView
│   │   └── CrosswordData.kt         # All 10 puzzle definitions
│   └── res/
│       ├── layout/                  # XML layouts
│       ├── values/                  # Colors, strings, themes
│       └── drawable/                # Key background, icons
```

<img src="[https://user-images.githubusercontent.com" width](https://github.com/user-attachments/assets/27bd32ff-03fc-4b44-9005-040085150b11)="200" height="150" />

