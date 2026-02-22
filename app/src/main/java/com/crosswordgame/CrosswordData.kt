package com.crosswordgame

data class CrosswordCell(
    val row: Int,
    val col: Int,
    val letter: Char,
    val isBlocked: Boolean = false
)

data class CrosswordClue(
    val number: Int,
    val direction: Direction,
    val clue: String,
    val answer: String,
    val startRow: Int,
    val startCol: Int
)

enum class Direction { ACROSS, DOWN }

data class CrosswordPuzzle(
    val title: String,
    val gridSize: Int,
    val clues: List<CrosswordClue>
)

object CrosswordData {

    val puzzles: List<CrosswordPuzzle> = listOf(

        // Level 1 - Simple 5x5
        CrosswordPuzzle(
            title = "Level 1 - Getting Started",
            gridSize = 5,
            clues = listOf(
                CrosswordClue(1, Direction.ACROSS, "Opposite of night", "DAY", 0, 0),
                CrosswordClue(2, Direction.ACROSS, "Feline pet", "CAT", 1, 0),
                CrosswordClue(3, Direction.ACROSS, "Canine pet", "DOG", 2, 0),
                CrosswordClue(4, Direction.ACROSS, "Bright star", "SUN", 3, 0),
                CrosswordClue(5, Direction.ACROSS, "Baked good", "PIE", 4, 0),
                CrosswordClue(1, Direction.DOWN, "Moisture from sky", "DCDSP", 0, 0),
                CrosswordClue(6, Direction.DOWN, "Definite article", "ATOES", 0, 1),
                CrosswordClue(7, Direction.DOWN, "You and I", "YAGUI", 0, 2)
            )
        ),

        // Level 2
        CrosswordPuzzle(
            title = "Level 2 - Animals",
            gridSize = 5,
            clues = listOf(
                CrosswordClue(1, Direction.ACROSS, "King of the jungle", "LION", 0, 0),
                CrosswordClue(2, Direction.ACROSS, "Largest mammal", "WHALE", 2, 0),
                CrosswordClue(3, Direction.ACROSS, "Man's best friend", "DOG", 4, 0),
                CrosswordClue(1, Direction.DOWN, "Large body of water", "LAWD", 0, 0),
                CrosswordClue(4, Direction.DOWN, "Intelligent primate", "IHOLE", 0, 1),
                CrosswordClue(5, Direction.DOWN, "Bird of prey", "OAAG", 0, 2)
            )
        ),

        // Level 3
        CrosswordPuzzle(
            title = "Level 3 - Food",
            gridSize = 5,
            clues = listOf(
                CrosswordClue(1, Direction.ACROSS, "Tropical yellow fruit", "MANGO", 0, 0),
                CrosswordClue(2, Direction.ACROSS, "Italian flat bread", "PIZZA", 2, 0),
                CrosswordClue(3, Direction.ACROSS, "Baked breakfast item", "TOAST", 4, 0),
                CrosswordClue(1, Direction.DOWN, "Dairy product", "MAPIT", 0, 0),
                CrosswordClue(4, Direction.DOWN, "Spicy condiment", "NIZOA", 0, 1),
                CrosswordClue(5, Direction.DOWN, "Sweetener", "GZAS", 0, 2)
            )
        ),

        // Level 4
        CrosswordPuzzle(
            title = "Level 4 - Nature",
            gridSize = 5,
            clues = listOf(
                CrosswordClue(1, Direction.ACROSS, "Tallest tree type", "OAK", 0, 0),
                CrosswordClue(2, Direction.ACROSS, "Flowing water body", "RIVER", 1, 0),
                CrosswordClue(3, Direction.ACROSS, "Frozen water", "ICE", 2, 1),
                CrosswordClue(4, Direction.ACROSS, "Rocky peak", "MOUNT", 3, 0),
                CrosswordClue(5, Direction.ACROSS, "Sandy shore", "BEACH", 4, 0),
                CrosswordClue(1, Direction.DOWN, "Open grassy land", "ORIMB", 0, 0),
                CrosswordClue(6, Direction.DOWN, "Desert feature", "AVCAE", 0, 1),
                CrosswordClue(7, Direction.DOWN, "Woodland creature home", "KIEAC", 0, 2)
            )
        ),

        // Level 5
        CrosswordPuzzle(
            title = "Level 5 - Colors",
            gridSize = 5,
            clues = listOf(
                CrosswordClue(1, Direction.ACROSS, "Color of the sky", "BLUE", 0, 0),
                CrosswordClue(2, Direction.ACROSS, "Color of grass", "GREEN", 1, 0),
                CrosswordClue(3, Direction.ACROSS, "Color of fire", "RED", 2, 0),
                CrosswordClue(4, Direction.ACROSS, "Color of sun", "GOLD", 3, 0),
                CrosswordClue(5, Direction.ACROSS, "Color of snow", "WHITE", 4, 0),
                CrosswordClue(1, Direction.DOWN, "Dark shade", "BGRGW", 0, 0),
                CrosswordClue(6, Direction.DOWN, "Light purple", "LREDO", 0, 1),
                CrosswordClue(7, Direction.DOWN, "Mixed color", "UEEDI", 0, 2)
            )
        ),

        // Level 6
        CrosswordPuzzle(
            title = "Level 6 - Sports",
            gridSize = 6,
            clues = listOf(
                CrosswordClue(1, Direction.ACROSS, "Olympic swimming stroke", "CRAWL", 0, 0),
                CrosswordClue(2, Direction.ACROSS, "Court net sport", "TENNIS", 2, 0),
                CrosswordClue(3, Direction.ACROSS, "Ice rink sport", "HOCKEY", 4, 0),
                CrosswordClue(1, Direction.DOWN, "Track competition", "CTHKR", 0, 0),
                CrosswordClue(4, Direction.DOWN, "Water sport paddle", "REOAO", 0, 1),
                CrosswordClue(5, Direction.DOWN, "Goal scoring sport", "AISNC", 0, 2)
            )
        ),

        // Level 7
        CrosswordPuzzle(
            title = "Level 7 - Geography",
            gridSize = 6,
            clues = listOf(
                CrosswordClue(1, Direction.ACROSS, "Largest continent", "ASIA", 0, 0),
                CrosswordClue(2, Direction.ACROSS, "Amazon country", "BRAZIL", 2, 0),
                CrosswordClue(3, Direction.ACROSS, "Island country", "JAPAN", 4, 0),
                CrosswordClue(1, Direction.DOWN, "African desert", "ABAJ", 0, 0),
                CrosswordClue(4, Direction.DOWN, "European river", "SRBRA", 0, 1),
                CrosswordClue(5, Direction.DOWN, "Highest mountain range", "IIZAN", 0, 2)
            )
        ),

        // Level 8
        CrosswordPuzzle(
            title = "Level 8 - Science",
            gridSize = 6,
            clues = listOf(
                CrosswordClue(1, Direction.ACROSS, "Basic unit of matter", "ATOM", 0, 0),
                CrosswordClue(2, Direction.ACROSS, "Force that pulls objects down", "GRAVITY", 2, 0),
                CrosswordClue(3, Direction.ACROSS, "Speed of light unit", "METER", 4, 0),
                CrosswordClue(1, Direction.DOWN, "Chemical symbol H", "AGM", 0, 0),
                CrosswordClue(4, Direction.DOWN, "Charged particle", "TRIAE", 0, 1),
                CrosswordClue(5, Direction.DOWN, "Scientific test", "OAVEN", 0, 2)
            )
        ),

        // Level 9
        CrosswordPuzzle(
            title = "Level 9 - Technology",
            gridSize = 6,
            clues = listOf(
                CrosswordClue(1, Direction.ACROSS, "Global network", "INTERNET", 0, 0),
                CrosswordClue(2, Direction.ACROSS, "Wireless protocol", "BLUETOOTH", 2, 0),
                CrosswordClue(3, Direction.ACROSS, "Mobile app store platform", "ANDROID", 4, 0),
                CrosswordClue(1, Direction.DOWN, "Processing chip", "IBA", 0, 0),
                CrosswordClue(4, Direction.DOWN, "Data storage", "NLOA", 0, 1),
                CrosswordClue(5, Direction.DOWN, "Screen interface", "TTND", 0, 2)
            )
        ),

        // Level 10
        CrosswordPuzzle(
            title = "Level 10 - Master",
            gridSize = 7,
            clues = listOf(
                CrosswordClue(1, Direction.ACROSS, "Musical instrument with keys", "PIANO", 0, 0),
                CrosswordClue(2, Direction.ACROSS, "Shakespeare's language", "ENGLISH", 2, 0),
                CrosswordClue(3, Direction.ACROSS, "Ancient Egyptian structure", "PYRAMID", 4, 0),
                CrosswordClue(4, Direction.ACROSS, "Space exploration agency", "NASA", 6, 0),
                CrosswordClue(1, Direction.DOWN, "Literary masterpiece", "PEPN", 0, 0),
                CrosswordClue(5, Direction.DOWN, "Artistic expression", "INGYA", 0, 1),
                CrosswordClue(6, Direction.DOWN, "Scientific discovery", "ALSMA", 0, 2),
                CrosswordClue(7, Direction.DOWN, "Historical period", "NGIIDS", 0, 3)
            )
        )
    )
}
