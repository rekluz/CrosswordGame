package com.crosswordgame

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crosswordgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        refreshStats()

        binding.btnPlay.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("level", 1)
            startActivity(intent)
        }

        binding.btnContinue.setOnClickListener {
            val prefs = getSharedPreferences("SlidePuzzlePrefs", MODE_PRIVATE)
            val highestLevel = prefs.getInt("highest_level", 1)
            if (highestLevel > 1) {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("level", highestLevel)
                startActivity(intent)
            } else {
                Toast.makeText(this, "No saved progress yet! Start a new game.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLevelSelect.setOnClickListener {
            startActivity(Intent(this, LevelSelectActivity::class.java))
        }

        binding.btnReset.setOnClickListener {
            getSharedPreferences("SlidePuzzlePrefs", MODE_PRIVATE).edit().clear().apply()
            refreshStats()
            Toast.makeText(this, "Progress reset!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshStats()
    }

    private fun refreshStats() {
        val prefs = getSharedPreferences("SlidePuzzlePrefs", MODE_PRIVATE)
        val highScore = prefs.getInt("high_score", 0)
        val highestLevel = prefs.getInt("highest_level", 1)
        binding.tvHighScore.text = "Best Score: $highScore"
        binding.tvProgress.text = "Progress: Level $highestLevel / ${CrosswordData.puzzles.size}"
    }
}
