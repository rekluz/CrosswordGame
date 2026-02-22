package com.crosswordgame

data class CellState(
    val row: Int,
    val col: Int,
    var letter: Char = ' ',
    var correctLetter: Char,
    var isBlocked: Boolean = false,
    var isSelected: Boolean = false,
    var isHighlighted: Boolean = false,
    var isRevealed: Boolean = false,
    var number: Int? = null
)

data class ActiveClue(
    val clue: CrosswordClue,
    val cells: List<Pair<Int, Int>>
)

class PuzzleEngine(private val puzzle: CrosswordPuzzle) {

    val gridSize = puzzle.gridSize
    val grid: Array<Array<CellState?>> = Array(gridSize) { arrayOfNulls(gridSize) }
    private val clueWordMap = mutableMapOf<CrosswordClue, List<Pair<Int, Int>>>()

    var selectedRow = -1
    var selectedCol = -1
    var activeClue: ActiveClue? = null
    var score = 0
    var hintsUsed = 0

    init {
        buildGrid()
    }

    private fun buildGrid() {
        // Mark all cells as blocked initially
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                grid[r][c] = CellState(r, c, correctLetter = ' ', isBlocked = true)
            }
        }

        // Place words
        val numberMap = mutableMapOf<Pair<Int, Int>, Int>()

        puzzle.clues.forEach { clue ->
            val cells = mutableListOf<Pair<Int, Int>>()
            clue.answer.forEachIndexed { i, ch ->
                val r = if (clue.direction == Direction.ACROSS) clue.startRow else clue.startRow + i
                val c = if (clue.direction == Direction.ACROSS) clue.startCol + i else clue.startCol

                if (r < gridSize && c < gridSize) {
                    val existing = grid[r][c]
                    if (existing == null || existing.isBlocked) {
                        grid[r][c] = CellState(r, c, correctLetter = ch, isBlocked = false)
                    } else {
                        // Cell already exists from another word – keep it, just verify
                        existing.isBlocked = false
                        if (existing.correctLetter == ' ') existing.correctLetter = ch
                    }
                    cells.add(Pair(r, c))
                }
            }

            // Assign clue number
            val startKey = Pair(clue.startRow, clue.startCol)
            if (!numberMap.containsKey(startKey)) {
                numberMap[startKey] = clue.number
            }
            clueWordMap[clue] = cells
        }

        // Set cell numbers
        numberMap.forEach { (pos, num) ->
            grid[pos.first][pos.second]?.number = num
        }
    }

    fun selectCell(row: Int, col: Int): ActiveClue? {
        val cell = grid[row][col] ?: return null
        if (cell.isBlocked) return null

        // If clicking same cell, toggle direction
        val sameCell = row == selectedRow && col == selectedCol
        val currentDir = activeClue?.clue?.direction

        selectedRow = row
        selectedCol = col

        // Find which clues contain this cell
        val containingClues = clueWordMap.filter { (_, cells) ->
            cells.contains(Pair(row, col))
        }.keys.toList()

        if (containingClues.isEmpty()) return null

        val newClue = when {
            containingClues.size == 1 -> containingClues[0]
            sameCell && currentDir == Direction.ACROSS ->
                containingClues.find { it.direction == Direction.DOWN } ?: containingClues[0]
            sameCell && currentDir == Direction.DOWN ->
                containingClues.find { it.direction == Direction.ACROSS } ?: containingClues[0]
            else -> containingClues.find { it.direction == Direction.ACROSS } ?: containingClues[0]
        }

        clearHighlights()
        val cells = clueWordMap[newClue] ?: emptyList()
        cells.forEach { (r, c) -> grid[r][c]?.isHighlighted = true }
        grid[row][col]?.isSelected = true

        activeClue = ActiveClue(newClue, cells)
        return activeClue
    }

    fun enterLetter(letter: Char): Boolean {
        if (selectedRow < 0 || selectedCol < 0) return false
        val cell = grid[selectedRow][selectedCol] ?: return false
        if (cell.isBlocked) return false

        cell.letter = letter.uppercaseChar()
        moveToNextCell()
        return checkWordComplete()
    }

    fun deleteLetter(): Boolean {
        if (selectedRow < 0 || selectedCol < 0) return false
        val cell = grid[selectedRow][selectedCol] ?: return false

        if (cell.letter != ' ') {
            cell.letter = ' '
        } else {
            moveToPrevCell()
        }
        return false
    }

    private fun moveToNextCell() {
        val clue = activeClue ?: return
        val cells = clue.cells
        val currentIndex = cells.indexOf(Pair(selectedRow, selectedCol))
        if (currentIndex < cells.size - 1) {
            val next = cells[currentIndex + 1]
            selectedRow = next.first
            selectedCol = next.second
            clearSelection()
            grid[selectedRow][selectedCol]?.isSelected = true
        }
    }

    private fun moveToPrevCell() {
        val clue = activeClue ?: return
        val cells = clue.cells
        val currentIndex = cells.indexOf(Pair(selectedRow, selectedCol))
        if (currentIndex > 0) {
            val prev = cells[currentIndex - 1]
            selectedRow = prev.first
            selectedCol = prev.second
            clearSelection()
            grid[selectedRow][selectedCol]?.isSelected = true
        }
    }

    private fun checkWordComplete(): Boolean {
        val clue = activeClue ?: return false
        val cells = clue.cells
        return cells.all { (r, c) ->
            val cell = grid[r][c]
            cell != null && cell.letter == cell.correctLetter
        }
    }

    fun isPuzzleSolved(): Boolean {
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                val cell = grid[r][c] ?: continue
                if (!cell.isBlocked && cell.letter != cell.correctLetter) return false
            }
        }
        return true
    }

    fun revealCurrentCell() {
        if (selectedRow < 0 || selectedCol < 0) return
        val cell = grid[selectedRow][selectedCol] ?: return
        cell.letter = cell.correctLetter
        cell.isRevealed = true
        hintsUsed++
        score -= 50
        if (score < 0) score = 0
        moveToNextCell()
    }

    fun addScore(points: Int) {
        score += points
    }

    fun getProgress(): Pair<Int, Int> {
        var filled = 0
        var total = 0
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                val cell = grid[r][c] ?: continue
                if (!cell.isBlocked) {
                    total++
                    if (cell.letter != ' ') filled++
                }
            }
        }
        return Pair(filled, total)
    }

    fun getAcrossClues(): List<CrosswordClue> =
        puzzle.clues.filter { it.direction == Direction.ACROSS }.sortedBy { it.number }

    fun getDownClues(): List<CrosswordClue> =
        puzzle.clues.filter { it.direction == Direction.DOWN }.sortedBy { it.number }

    private fun clearHighlights() {
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                grid[r][c]?.isHighlighted = false
                grid[r][c]?.isSelected = false
            }
        }
    }

    private fun clearSelection() {
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                grid[r][c]?.isSelected = false
            }
        }
    }
}
