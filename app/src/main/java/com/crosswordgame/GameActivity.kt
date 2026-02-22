package com.crosswordgame

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.crosswordgame.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var engine: PuzzleEngine
    private lateinit var acrossAdapter: ClueAdapter
    private lateinit var downAdapter: ClueAdapter

    private var level = 1
    private var elapsedSeconds = 0
    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            elapsedSeconds++
            updateTimerDisplay()
            handler.postDelayed(this, 1000)
        }
    }

    private val keyboardLetters = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Z", "X", "C", "V", "B", "N", "M", "⌫")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        level = intent.getIntExtra("level", 1)
        val puzzleIndex = (level - 1).coerceIn(0, CrosswordData.puzzles.size - 1)
        val puzzle = CrosswordData.puzzles[puzzleIndex]

        engine = PuzzleEngine(puzzle)
        binding.gridView.engine = engine

        binding.tvLevelTitle.text = puzzle.title
        updateScore()

        setupClueList()
        setupKeyboard()
        setupButtons()

        binding.gridView.onCellSelected = { row, col ->
            val active = engine.selectCell(row, col)
            binding.gridView.refresh()
            active?.let { updateActiveClue(it.clue) }
        }

        handler.postDelayed(timerRunnable, 1000)

        // Auto-select first cell
        binding.gridView.post {
            for (r in 0 until engine.gridSize) {
                for (c in 0 until engine.gridSize) {
                    val cell = engine.grid[r][c]
                    if (cell != null && !cell.isBlocked) {
                        val active = engine.selectCell(r, c)
                        binding.gridView.refresh()
                        active?.let { updateActiveClue(it.clue) }
                        return@post
                    }
                }
            }
        }
    }

    private fun setupClueList() {
        acrossAdapter = ClueAdapter(engine.getAcrossClues()) { clue ->
            jumpToClue(clue)
        }
        downAdapter = ClueAdapter(engine.getDownClues()) { clue ->
            jumpToClue(clue)
        }

        binding.rvAcross.layoutManager = LinearLayoutManager(this)
        binding.rvAcross.adapter = acrossAdapter

        binding.rvDown.layoutManager = LinearLayoutManager(this)
        binding.rvDown.adapter = downAdapter
    }

    private fun jumpToClue(clue: CrosswordClue) {
        val active = engine.selectCell(clue.startRow, clue.startCol)
        if (active?.clue?.direction != clue.direction) {
            engine.selectCell(clue.startRow, clue.startCol)
        }
        binding.gridView.refresh()
        updateActiveClue(clue)
    }

    private fun setupKeyboard() {
        val rows = listOf(binding.keyRow1, binding.keyRow2, binding.keyRow3)
        keyboardLetters.forEachIndexed { rowIdx, letters ->
            val row = rows[rowIdx]
            row.removeAllViews()
            letters.forEach { letter ->
                val btn = Button(this).apply {
                    text = letter
                    textSize = 13f
                    val lp = android.widget.LinearLayout.LayoutParams(0,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    lp.setMargins(2, 2, 2, 2)
                    layoutParams = lp
                    setBackgroundResource(R.drawable.key_background)
                    setTextColor(0xFF1A1A2E.toInt())
                    setPadding(0, 16, 0, 16)

                    setOnClickListener {
                        if (letter == "⌫") {
                            engine.deleteLetter()
                        } else {
                            val wordDone = engine.enterLetter(letter[0])
                            if (wordDone) {
                                engine.addScore(100)
                                updateScore()
                                Toast.makeText(this@GameActivity, "Word complete! +100", Toast.LENGTH_SHORT).show()
                            }
                            if (engine.isPuzzleSolved()) {
                                onPuzzleSolved()
                            }
                        }
                        binding.gridView.refresh()
                        updateProgress()
                    }
                }
                row.addView(btn)
            }
        }
    }

    private fun setupButtons() {
        binding.btnHint.setOnClickListener {
            engine.revealCurrentCell()
            binding.gridView.refresh()
            updateScore()
            updateProgress()
            Toast.makeText(this, "Letter revealed! -50 points", Toast.LENGTH_SHORT).show()

            if (engine.isPuzzleSolved()) onPuzzleSolved()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.tabAcross.setOnClickListener {
            binding.rvAcross.visibility = View.VISIBLE
            binding.rvDown.visibility = View.GONE
            binding.tabAcross.alpha = 1f
            binding.tabDown.alpha = 0.5f
        }

        binding.tabDown.setOnClickListener {
            binding.rvAcross.visibility = View.GONE
            binding.rvDown.visibility = View.VISIBLE
            binding.tabAcross.alpha = 0.5f
            binding.tabDown.alpha = 1f
        }
    }

    private fun updateActiveClue(clue: CrosswordClue) {
        binding.tvActiveClue.text = "${clue.number} ${clue.direction.name}: ${clue.clue}"
        acrossAdapter.setActiveClue(if (clue.direction == Direction.ACROSS) clue else null)
        downAdapter.setActiveClue(if (clue.direction == Direction.DOWN) clue else null)
    }

    private fun updateScore() {
        binding.tvScore.text = "Score: ${engine.score}"
    }

    private fun updateProgress() {
        val (filled, total) = engine.getProgress()
        binding.progressBar.progress = if (total > 0) (filled * 100 / total) else 0
        binding.tvProgress.text = "$filled / $total"
    }

    private fun updateTimerDisplay() {
        val mins = elapsedSeconds / 60
        val secs = elapsedSeconds % 60
        binding.tvTimer.text = "%02d:%02d".format(mins, secs)
    }

    private fun onPuzzleSolved() {
        handler.removeCallbacks(timerRunnable)

        // Time bonus
        val timeBonus = maxOf(0, 500 - elapsedSeconds * 2)
        engine.addScore(timeBonus + 500)
        updateScore()

        // Save progress
        val prefs = getSharedPreferences("SlidePuzzlePrefs", MODE_PRIVATE)
        val currentHigh = prefs.getInt("high_score", 0)
        if (engine.score > currentHigh) {
            prefs.edit().putInt("high_score", engine.score).apply()
        }
        val currentLevel = prefs.getInt("highest_level", 1)
        if (level + 1 > currentLevel) {
            prefs.edit().putInt("highest_level", level + 1).apply()
        }

        AlertDialog.Builder(this)
            .setTitle("🎉 Puzzle Solved!")
            .setMessage(
                "Level $level Complete!\n\n" +
                "Time: ${binding.tvTimer.text}\n" +
                "Hints Used: ${engine.hintsUsed}\n" +
                "Time Bonus: +$timeBonus\n" +
                "Final Score: ${engine.score}"
            )
            .setPositiveButton("Next Level") { _, _ ->
                if (level < CrosswordData.puzzles.size) {
                    val intent = Intent(this, GameActivity::class.java)
                    intent.putExtra("level", level + 1)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "You completed all levels! 🏆", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            .setNegativeButton("Menu") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunnable)
    }
}
