package ni.edu.uam.psyconnect.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.TestResult
import ni.edu.uam.psyconnect.ui.screens.ResultDetailActivity

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

    override fun getItemCount() =
        results.size

    override fun onBindViewHolder(
        holder: RecentResultViewHolder,
        position: Int
    ) {

        val result =
            results[position]

        holder.bind(
            result,
            results
        )

        holder.itemView.setOnClickListener {

            val intent =
                Intent(
                    holder.itemView.context,
                    ResultDetailActivity::class.java
                )

            intent.putExtra(
                "category",
                result.category
            )

            intent.putExtra(
                "percentage",
                result.percentage
            )

            intent.putExtra(
                "trend",
                result.trend
            )

            intent.putExtra(
                "date",
                result.createdAt
            )

            holder.itemView.context
                .startActivity(intent)
        }
    }
}