package ni.edu.uam.psyconnect.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.Mood

class MoodAdapter(
    private val moods: List<Mood>
) : RecyclerView.Adapter<MoodAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val tvMood: TextView =
            itemView.findViewById(
                R.id.tvMood
            )

        val tvDate: TextView =
            itemView.findViewById(
                R.id.tvDate
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.item_mood,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun getItemCount() =
        moods.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val mood =
            moods[position]

        val emoji =
            when (mood.mood) {

                "EXCELENTE" -> "😁"

                "BIEN" -> "😊"

                "NORMAL" -> "😐"

                "TRISTE" -> "😔"

                else -> "😭"
            }

        holder.tvMood.text =
            "$emoji ${mood.mood}"

        holder.tvDate.text =
            mood.date ?: ""
    }
}