package ni.edu.uam.psyconnect.ui.adapter

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.TestResult

class RecentResultViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val card =
        itemView.findViewById<CardView>(
            R.id.cardResult
        )

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

    private val tvTrend =
        itemView.findViewById<TextView>(
            R.id.tvTrend
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
            formatearFecha(
                result.createdAt
            )

        when {

            result.trend > 0 -> {

                tvTrend.text =
                    "🟢 Mejorando +${result.trend}%"

                tvTrend.setTextColor(
                    Color.parseColor("#16A34A")
                )
            }

            result.trend < 0 -> {

                tvTrend.text =
                    "🔴 Bajó ${result.trend}%"

                tvTrend.setTextColor(
                    Color.parseColor("#DC2626")
                )
            }

            else -> {

                tvTrend.text =
                    "⚪ Sin cambios"

                tvTrend.setTextColor(
                    Color.parseColor("#6B7280")
                )
            }
        }

        when {

            result.percentage >= 80 -> {

                card.setCardBackgroundColor(
                    Color.parseColor("#ECFDF5")
                )

                tvLevel.setTextColor(
                    Color.parseColor("#059669")
                )
            }

            result.percentage >= 60 -> {

                card.setCardBackgroundColor(
                    Color.parseColor("#FEFCE8")
                )

                tvLevel.setTextColor(
                    Color.parseColor("#CA8A04")
                )
            }

            result.percentage >= 40 -> {

                card.setCardBackgroundColor(
                    Color.parseColor("#FFF7ED")
                )

                tvLevel.setTextColor(
                    Color.parseColor("#EA580C")
                )
            }

            else -> {

                card.setCardBackgroundColor(
                    Color.parseColor("#FEF2F2")
                )

                tvLevel.setTextColor(
                    Color.parseColor("#DC2626")
                )
            }
        }
    }

    private fun traducirCategoria(
        category: String
    ): String {

        return when (category) {

            "WELLNESS" ->
                "🌿 Bienestar"

            "STRESS" ->
                "😌 Manejo del estrés"

            "SLEEP" ->
                "😴 Sueño"

            "MOOD" ->
                "😊 Estado de ánimo"

            "SELF_ESTEEM" ->
                "💜 Autoestima"

            "RELATIONSHIPS" ->
                "🤝 Relaciones"

            else ->
                "📊 General"
        }
    }

    private fun formatearFecha(
        fecha: String?
    ): String {

        return try {

            fecha
                ?.replace("T", " ")
                ?.substring(0, 16)
                ?: "-"

        } catch (
            e: Exception
        ) {

            "-"
        }
    }
}