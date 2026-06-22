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

    override fun getItemCount(): Int =
        results.size

    override fun onBindViewHolder(
        holder: RecentResultViewHolder,
        position: Int
    ) {

        val result =
            results[position]

        holder.bind(
            result = result,
            allResults = results
        )

        holder.itemView.setOnClickListener {

            val context =
                holder.itemView.context

            val detailIntent =
                Intent(
                    context,
                    ResultDetailActivity::class.java
                )

            detailIntent.putExtra(
                "category",
                result.category
            )

            detailIntent.putExtra(
                "percentage",
                result.percentage
            )

            detailIntent.putExtra(
                "level",
                result.level
            )

            detailIntent.putExtra(
                "date",
                result.createdAt
            )

            detailIntent.putExtra(
                "trend",
                result.trend
            )

            context.startActivity(
                detailIntent
            )
        }
    }
}