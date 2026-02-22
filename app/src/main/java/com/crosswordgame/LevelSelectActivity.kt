package com.crosswordgame

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class LevelSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("SlidePuzzlePrefs", MODE_PRIVATE)
        val highestLevel = prefs.getInt("highest_level", 1)

        val scroll = ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
            setBackgroundColor(Color.parseColor("#F5F5F5"))
        }

        val title = TextView(this).apply {
            text = "Select Level"
            textSize = 24f
            setTextColor(Color.parseColor("#1A1A2E"))
            setPadding(0, 0, 0, 24)
        }
        layout.addView(title)

        CrosswordData.puzzles.forEachIndexed { index, puzzle ->
            val levelNum = index + 1
            val isUnlocked = levelNum <= highestLevel
            val btn = Button(this).apply {
                text = if (isUnlocked) "✓ ${puzzle.title}" else "🔒 Level $levelNum"
                textSize = 14f
                isEnabled = isUnlocked
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(0, 8, 0, 8)
                layoutParams = lp
                setBackgroundColor(
                    if (isUnlocked) Color.parseColor("#1565C0") else Color.parseColor("#BDBDBD")
                )
                setTextColor(Color.WHITE)
                setPadding(24, 24, 24, 24)

                setOnClickListener {
                    val intent = Intent(this@LevelSelectActivity, GameActivity::class.java)
                    intent.putExtra("level", levelNum)
                    startActivity(intent)
                }
            }
            layout.addView(btn)
        }

        scroll.addView(layout)
        setContentView(scroll)
    }
}
