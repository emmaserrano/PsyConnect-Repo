package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.network.RetrofitClient

class History : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history)

        val chart =
            findViewById<BarChart>(R.id.barChart)

        val tvSummary =
            findViewById<TextView>(R.id.tvSummary)

        val layoutHistory =
            findViewById<LinearLayout>(R.id.layoutHistory)

        val sharedPreferences =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
            )

        val userId =
            sharedPreferences.getLong(
                "userId",
                -1
            )

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient
                        .apiService
                        .getHistory(userId)

                if (response.isSuccessful) {

                    val results =
                        response.body() ?: emptyList()

                    if (results.isNotEmpty()) {

                        val average =
                            results.map {
                                it.percentage
                            }.average()

                        tvSummary.text =
                            """
                            Total de tests: ${results.size}
                            Promedio general: ${average.toInt()}%
                            Último resultado: ${results[0].percentage}%
                            """.trimIndent()

                        val entries =
                            mutableListOf<BarEntry>()

                        results
                            .reversed()
                            .forEachIndexed { index, result ->

                                entries.add(
                                    BarEntry(
                                        (index + 1).toFloat(),
                                        result.percentage.toFloat()
                                    )
                                )
                            }

                        val dataSet =
                            BarDataSet(
                                entries,
                                "Resultados"
                            )

                        chart.data =
                            BarData(dataSet)

                        chart.description.isEnabled = false

                        chart.animateY(1000)

                        chart.invalidate()

                        layoutHistory.removeAllViews()

                        results.forEachIndexed { index, result ->

                            val card =
                                CardView(this@History)

                            val params =
                                LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )

                            params.bottomMargin = 24

                            card.layoutParams = params

                            card.radius = 20f

                            card.cardElevation = 8f

                            val content =
                                TextView(this@History)

                            content.setPadding(
                                32,
                                32,
                                32,
                                32
                            )

                            content.text =
                                """
                                Test #${results.size - index}

                                Fecha:
                                ${result.createdAt ?: "No disponible"}

                                Resultado:
                                ${result.percentage}%

                                Nivel:
                                ${result.level}
                                """.trimIndent()

                            content.textSize = 16f

                            card.addView(content)

                            layoutHistory.addView(card)
                        }
                    }
                }

            } catch (e: Exception) {

                tvSummary.text =
                    "Error al cargar historial"
            }
        }
    }
}