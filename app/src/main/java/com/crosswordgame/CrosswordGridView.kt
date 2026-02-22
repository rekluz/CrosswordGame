package com.crosswordgame

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class CrosswordGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var engine: PuzzleEngine? = null
        set(value) {
            field = value
            invalidate()
        }

    var onCellSelected: ((Int, Int) -> Unit)? = null

    private val blockedPaint = Paint().apply {
        color = Color.parseColor("#1A1A2E")
        style = Paint.Style.FILL
    }

    private val cellPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val highlightPaint = Paint().apply {
        color = Color.parseColor("#C8E6FF")
        style = Paint.Style.FILL
    }

    private val selectedPaint = Paint().apply {
        color = Color.parseColor("#4FC3F7")
        style = Paint.Style.FILL
    }

    private val revealedPaint = Paint().apply {
        color = Color.parseColor("#E8F5E9")
        style = Paint.Style.FILL
    }

    private val borderPaint = Paint().apply {
        color = Color.parseColor("#37474F")
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    private val gridBorderPaint = Paint().apply {
        color = Color.parseColor("#1A1A2E")
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val letterPaint = Paint().apply {
        color = Color.parseColor("#1A1A2E")
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    private val numberPaint = Paint().apply {
        color = Color.parseColor("#455A64")
        textAlign = Paint.Align.LEFT
    }

    private val correctPaint = Paint().apply {
        color = Color.parseColor("#2E7D32")
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    private var cellSize = 0f
    private var offsetX = 0f
    private var offsetY = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        recalcDimensions()
    }

    private fun recalcDimensions() {
        val eng = engine ?: return
        val size = eng.gridSize
        cellSize = min(width.toFloat(), height.toFloat()) / size
        offsetX = (width - cellSize * size) / 2f
        offsetY = (height - cellSize * size) / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val eng = engine ?: return
        if (cellSize == 0f) recalcDimensions()

        letterPaint.textSize = cellSize * 0.55f
        correctPaint.textSize = cellSize * 0.55f
        numberPaint.textSize = cellSize * 0.22f

        for (r in 0 until eng.gridSize) {
            for (c in 0 until eng.gridSize) {
                val cell = eng.grid[r][c] ?: continue
                val left = offsetX + c * cellSize
                val top = offsetY + r * cellSize
                val right = left + cellSize
                val bottom = top + cellSize

                if (cell.isBlocked) {
                    canvas.drawRect(left, top, right, bottom, blockedPaint)
                } else {
                    val bgPaint = when {
                        cell.isSelected -> selectedPaint
                        cell.isHighlighted -> highlightPaint
                        cell.isRevealed -> revealedPaint
                        else -> cellPaint
                    }
                    canvas.drawRect(left, top, right, bottom, bgPaint)
                    canvas.drawRect(left, top, right, bottom, borderPaint)

                    // Draw number
                    cell.number?.let { num ->
                        canvas.drawText(
                            num.toString(),
                            left + cellSize * 0.06f,
                            top + cellSize * 0.28f,
                            numberPaint
                        )
                    }

                    // Draw letter
                    if (cell.letter != ' ') {
                        val paint = if (cell.isRevealed) correctPaint else letterPaint
                        canvas.drawText(
                            cell.letter.toString(),
                            left + cellSize / 2f,
                            top + cellSize * 0.75f,
                            paint
                        )
                    }
                }
            }
        }

        // Outer border
        canvas.drawRect(
            offsetX, offsetY,
            offsetX + eng.gridSize * cellSize,
            offsetY + eng.gridSize * cellSize,
            gridBorderPaint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN) return true
        val eng = engine ?: return true

        val col = ((event.x - offsetX) / cellSize).toInt()
        val row = ((event.y - offsetY) / cellSize).toInt()

        if (row in 0 until eng.gridSize && col in 0 until eng.gridSize) {
            onCellSelected?.invoke(row, col)
            invalidate()
        }
        return true
    }

    fun refresh() {
        invalidate()
    }
}
