package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        val tvTrend =
            findViewById<TextView>(R.id.tvTrend)

        val layoutHistory =
            findViewById<LinearLayout>(R.id.layoutHistory)

        val bottomNav =
            findViewById<BottomNavigationView>(R.id.bottomNavigation)

        /*
         * MENÚ INFERIOR
         */
        bottomNav.selectedItemId =
            R.id.nav_history

        bottomNav.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_test -> {

                    startActivity(
                        Intent(
                            this,
                            Test::class.java
                        )
                    )

                    finish()

                    true
                }

                R.id.nav_history -> {

                    true
                }

                R.id.nav_profile -> {

                    startActivity(
                        Intent(
                            this,
                            Profile::class.java
                        )
                    )

                    finish()

                    true
                }

                else -> false
            }
        }

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

                        /*
                         * RESUMEN
                         */
                        val average =
                            results.map {
                                it.percentage
                            }.average()

                        tvSummary.text =
                            """
                            Total de tests: ${results.size}
                            Promedio general: ${average.toInt()}%
                            Último resultado: ${results.first().percentage}%
                            """.trimIndent()

                        /*
                         * TENDENCIA
                         */
                        if (results.size > 1) {

                            val ultimo =
                                results.first().percentage

                            val primero =
                                results.last().percentage

                            tvTrend.text =
                                when {

                                    ultimo < primero ->
                                        "📉 Tu nivel de ansiedad ha disminuido."

                                    ultimo > primero ->
                                        "📈 Tu nivel de ansiedad ha aumentado."

                                    else ->
                                        "➡️ Tu nivel se mantiene estable."
                                }

                        } else {

                            tvTrend.text =
                                "Realiza más tests para analizar tu tendencia."
                        }

                        /*
                         * GRÁFICO
                         */
                        val entries =
                            mutableListOf<BarEntry>()

                        val labels =
                            mutableListOf<String>()

                        results
                            .reversed()
                            .forEachIndexed { index, result ->

                                entries.add(
                                    BarEntry(
                                        index.toFloat(),
                                        result.percentage.toFloat()
                                    )
                                )

                                try {

                                    val fecha =
                                        result.createdAt
                                            ?.split("T")
                                            ?.get(0)
                                            ?: ""

                                    val partes =
                                        fecha.split("-")

                                    labels.add(
                                        "${partes[2]}/${partes[1]}"
                                    )

                                } catch (_: Exception) {

                                    labels.add(
                                        "T${index + 1}"
                                    )
                                }
                            }

                        val dataSet =
                            BarDataSet(
                                entries,
                                "Resultados"
                            )

                        val data =
                            BarData(dataSet)

                        data.barWidth = 0.6f

                        chart.data =
                            data

                        chart.description.isEnabled = false

                        chart.legend.isEnabled = false

                        chart.axisRight.isEnabled = false

                        chart.setFitBars(true)

                        chart.extraBottomOffset = 40f

                        chart.animateY(1000)

                        chart.xAxis.position =
                            XAxis.XAxisPosition.BOTTOM

                        chart.xAxis.granularity = 1f

                        chart.xAxis.labelCount =
                            labels.size

                        chart.xAxis.setDrawGridLines(false)

                        chart.xAxis.labelRotationAngle = -30f

                        chart.xAxis.textSize = 10f

                        chart.xAxis.yOffset = 10f

                        chart.xAxis.valueFormatter =
                            IndexAxisValueFormatter(labels)

                        chart.invalidate()

                        /*
                         * HISTORIAL
                         */
                        layoutHistory.removeAllViews()

                        results.forEachIndexed { index, result ->

                            var fecha =
                                "No disponible"

                            var hora =
                                "No disponible"

                            try {

                                val createdAt =
                                    result.createdAt ?: ""

                                val partes =
                                    createdAt.split("T")
                                if (partes.size == 2) {

                                    val fechaOriginal =
                                        partes[0]

                                    val fechaPartes =
                                        fechaOriginal.split("-")

                                    fecha =
                                        "${fechaPartes[2]}/${fechaPartes[1]}/${fechaPartes[0]}"

                                    val hora24 =
                                        partes[1].substring(0, 5)

                                    val h =
                                        hora24.substring(0, 2).toInt()

                                    val minutos =
                                        hora24.substring(3, 5)

                                    hora =
                                        when {

                                            h == 0 ->
                                                "12:$minutos AM"

                                            h < 12 ->
                                                "$h:$minutos AM"

                                            h == 12 ->
                                                "12:$minutos PM"

                                            else ->
                                                "${h - 12}:$minutos PM"
                                        }
                                }

                            } catch (_: Exception) {
                            }

                            val card =
                                CardView(this@History)

                            val params =
                                LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )

                            params.bottomMargin = 24

                            card.layoutParams =
                                params

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

                                📅 Fecha: $fecha
                                🕒 Hora: $hora

                                📊 Resultado: ${result.percentage}%

                                🧠 Nivel: ${result.level}
                                """.trimIndent()

                            content.textSize = 16f

                            card.addView(content)

                            layoutHistory.addView(card)
                        }
                    }
                }

            } catch (_: Exception) {

                tvSummary.text =
                    "Error al cargar historial"
            }
        }
    }
}