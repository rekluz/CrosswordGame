# 🧩 Crossword Game - Android App

A crossword puzzle game for Android inspired by Netflix Puzzled, built in Kotlin. This is a work in progress.
The main structure is laid out but puzzles are not working properly. Experimental builds for now.

## Features
- 10 levels of crossword puzzles (mixed topics)
- Tap-to-select cells on the crossword grid
- Full on-screen keyboard (no system keyboard needed)
- ACROSS / DOWN clue panel with tabs
- Hint system (reveals a letter, costs 50 points)
- Score tracking with time bonus
- Level progression & unlock system
- Best score saved across sessions

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

## How to Play
1. Tap **▶ New Game** from the main menu
2. Tap any white cell on the crossword grid to select it
3. The active clue shows at the top
4. Tap letters on the keyboard to fill in your answer
5. Tap a cell again to toggle ACROSS ↔ DOWN direction
6. Use the **ACROSS / DOWN** tabs to browse and jump to clues
7. Tap **💡 Hint** to reveal the current letter (-50 points)
8. Complete all words to finish the level!

## Scoring
- +100 points per completed word
- +500 points for completing the puzzle
- Time bonus: up to +500 (faster = more points)
- -50 points per hint used

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

## Adding More Levels
Edit `CrosswordData.kt` and add more `CrosswordPuzzle` entries to the `puzzles` list.
Each puzzle needs: a title, grid size, and a list of `CrosswordClue` objects with:
- Clue number, direction (ACROSS/DOWN), clue text, answer, start row, start col
