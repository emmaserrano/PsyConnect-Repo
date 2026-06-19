package ni.edu.uam.psyconnect.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.TestResult

class RecentResultViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val tvCategory =
        itemView.findViewById<TextView>(
            R.id.tvCategory
        )

    private val tvPercentage =
        itemView.findViewById<TextView>(
            R.id.tvPercentage
        )

    private val tvLevel =
        itemView.findViewById<TextView>(
            R.id.tvLevel
        )

    private val tvDate =
        itemView.findViewById<TextView>(
            R.id.tvDate
        )

    fun bind(
        result: TestResult
    ) {

        tvCategory.text =
            traducirCategoria(
                result.category
            )

        tvPercentage.text =
            "${result.percentage}%"

        tvLevel.text =
            result.level

        tvDate.text =
            result.createdAt
                ?.substring(0, 10)
                ?: "-"
    }

    private fun traducirCategoria(
        category: String
    ): String {

        return when (category) {

            "WELLNESS" -> "🌿 Bienestar"

            "STRESS" -> "😌 Estrés"

            "SLEEP" -> "😴 Sueño"

            "MOOD" -> "😊 Estado de ánimo"

            "SELF_ESTEEM" -> "💜 Autoestima"

            else -> "🤝 Relaciones"
        }
    }
}