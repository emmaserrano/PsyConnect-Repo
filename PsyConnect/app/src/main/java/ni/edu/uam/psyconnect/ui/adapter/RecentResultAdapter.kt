package ni.edu.uam.psyconnect.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uam.psyconnect.R

class RecentResultAdapter :
    RecyclerView.Adapter<RecentResultViewHolder>() {

    private val results =
        listOf(
            "Bienestar emocional • 84 %",
            "Estrés • 78 %",
            "Sueño y descanso • 81 %",
            "Estado de ánimo • 86 %"
        )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentResultViewHolder {

        return RecentResultViewHolder(

            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_recent_result,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount() =
        results.size

    override fun onBindViewHolder(
        holder: RecentResultViewHolder,
        position: Int
    ) {

        holder.bind(
            results[position]
        )
    }
}