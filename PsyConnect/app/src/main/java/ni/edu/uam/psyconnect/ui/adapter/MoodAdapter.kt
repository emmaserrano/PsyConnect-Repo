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
        view: View
    ) : RecyclerView.ViewHolder(view) {

        val tvMood: TextView =
            view.findViewById(
                R.id.tvMood
            )

        val tvDate: TextView =
            view.findViewById(
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

                "EXCELENTE" -> "😄 Excelente"

                "BIEN" -> "🙂 Bien"

                "NORMAL" -> "😐 Normal"

                "TRISTE" -> "😔 Triste"

                else -> "😢 Muy mal"
            }

        holder.tvMood.text =
            emoji

        holder.tvDate.text =
            mood.date ?: ""
    }
}