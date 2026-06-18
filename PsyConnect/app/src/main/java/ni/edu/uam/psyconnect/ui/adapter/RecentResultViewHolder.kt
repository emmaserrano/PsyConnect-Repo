package ni.edu.uam.psyconnect.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.TestResult

class RecentResultViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val tvResult =
        itemView.findViewById<TextView>(
            R.id.tvResult
        )

    fun bind(
        result: TestResult
    ) {

        val categoryName = when(result.category){

            "WELLNESS" ->
                "🌿 Bienestar emocional"

            "STRESS" ->
                "🌊 Estrés"

            "SLEEP" ->
                "🌙 Sueño"

            "MOOD" ->
                "☀ Estado de ánimo"

            "RELATIONSHIPS" ->
                "🤝 Relaciones sociales"

            else ->
                result.category
        }

        tvResult.text =
            """
$categoryName

${result.level}

${result.percentage} %
            """.trimIndent()
    }
}