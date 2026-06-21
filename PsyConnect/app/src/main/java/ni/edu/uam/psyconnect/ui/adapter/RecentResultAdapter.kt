package ni.edu.uam.psyconnect.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.TestResult

class RecentResultAdapter(
    private val results: List<TestResult>
) : RecyclerView.Adapter<RecentResultViewHolder>() {

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

    override fun getItemCount(): Int =
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