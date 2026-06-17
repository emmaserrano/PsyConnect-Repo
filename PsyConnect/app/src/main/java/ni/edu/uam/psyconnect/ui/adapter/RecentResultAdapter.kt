package ni.edu.uam.psyconnect.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.RecentResult

class RecentResultAdapter(
    private val results: List<RecentResult>
) : RecyclerView.Adapter<RecentResultAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val tvTitle =
            itemView.findViewById<TextView>(
                R.id.tvTitle
            )

        val tvScore =
            itemView.findViewById<TextView>(
                R.id.tvScore
            )

        val tvDate =
            itemView.findViewById<TextView>(
                R.id.tvDate
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_recent_result,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return results.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val result =
            results[position]

        holder.tvTitle.text =
            result.title

        holder.tvScore.text =
            "Puntaje: ${result.score}"

        holder.tvDate.text =
            result.date
    }
}