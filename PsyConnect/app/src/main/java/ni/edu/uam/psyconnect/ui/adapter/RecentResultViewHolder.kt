package ni.edu.uam.psyconnect.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uam.psyconnect.R

class RecentResultViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val tvResult =
        itemView.findViewById<TextView>(
            R.id.tvResult
        )

    fun bind(
        result: String
    ) {

        tvResult.text =
            result
    }
}