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

            val trend =
                calcularTrend(
                    result
                )

            val intent =
                Intent(
                    context,
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
                "level",
                result.level
            )

            intent.putExtra(
                "date",
                result.createdAt
            )

            intent.putExtra(
                "trend",
                trend
            )

            context.startActivity(
                intent
            )
        }
    }

    private fun calcularTrend(
        current: TestResult
    ): Int {

        val sameCategory =
            results
                .filter {
                    it.category ==
                            current.category
                }
                .sortedBy {
                    it.id ?: 0
                }

        val index =
            sameCategory.indexOfFirst {
                it.id == current.id
            }

        if (index <= 0) {
            return 0
        }

        val previous =
            sameCategory[index - 1]

        return current.percentage -
                previous.percentage
    }
}