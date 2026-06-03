package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.network.RetrofitClient

class History : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history)

        val chart =
            findViewById<LineChart>(R.id.lineChart)

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
                            results.map { it.percentage }
                                .average()

                        tvSummary.text =
                            """
                            Total de tests: ${results.size}
                            Promedio: ${average.toInt()}%
                            Último resultado: ${results[0].percentage}%
                            """.trimIndent()

                        val entries =
                            mutableListOf<Entry>()

                        results.reversed()
                            .forEachIndexed { index, result ->

                                entries.add(
                                    Entry(
                                        index.toFloat(),
                                        result.percentage.toFloat()
                                    )
                                )
                            }

                        val dataSet =
                            LineDataSet(
                                entries,
                                "Nivel emocional"
                            )

                        chart.data =
                            LineData(dataSet)

                        chart.invalidate()

                        results.forEach {

                            val tv =
                                TextView(this@History)

                            tv.text =
                                """
                                Fecha: ${it.createdAt}
                                Resultado: ${it.percentage}%
                                Nivel: ${it.level}
                                """.trimIndent()

                            tv.textSize = 16f

                            layoutHistory.addView(tv)
                        }
                    }
                }

            } catch (_: Exception) {
            }
        }
    }
}