package com.crosswordgame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClueAdapter(
    private val clues: List<CrosswordClue>,
    private val onClueClick: (CrosswordClue) -> Unit
) : RecyclerView.Adapter<ClueAdapter.ClueViewHolder>() {

    private var activeClue: CrosswordClue? = null

    inner class ClueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNumber: TextView = view.findViewById(R.id.tvClueNumber)
        val tvClue: TextView = view.findViewById(R.id.tvClueText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClueViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clue, parent, false)
        return ClueViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClueViewHolder, position: Int) {
        val clue = clues[position]
        holder.tvNumber.text = "${clue.number}${if (clue.direction == Direction.ACROSS) "A" else "D"}"
        holder.tvClue.text = clue.clue

        val isActive = clue == activeClue
        holder.itemView.setBackgroundColor(
            if (isActive) 0xFFC8E6FF.toInt() else 0xFFFFFFFF.toInt()
        )
        holder.itemView.setOnClickListener { onClueClick(clue) }
    }

    override fun getItemCount() = clues.size

    fun setActiveClue(clue: CrosswordClue?) {
        activeClue = clue
        notifyDataSetChanged()
    }
}
