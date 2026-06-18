package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.TestResult
import ni.edu.uam.psyconnect.network.RetrofitClient
import ni.edu.uam.psyconnect.ui.adapter.RecentResultAdapter

class History : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history)

        cargarHistorial()

        configurarBottomNav()
    }

    private fun cargarHistorial() {

        val tvAverage =
            findViewById<TextView>(R.id.tvAverage)

        val tvBest =
            findViewById<TextView>(R.id.tvBest)

        val tvTotal =
            findViewById<TextView>(R.id.tvTotal)

        val tvFavorite =
            findViewById<TextView>(R.id.tvFavorite)

        val tvInsight =
            findViewById<TextView>(R.id.tvInsight)

        val chart =
            findViewById<LineChart>(R.id.chartMood)

        val recycler =
            findViewById<RecyclerView>(R.id.recyclerResults)

        lifecycleScope.launch {

            try {

                val sharedPreferences =
                    getSharedPreferences(
                        "psyconnect",
                        MODE_PRIVATE
                    )

                val userId =
                    sharedPreferences.getLong(
                        "userId",
                        1
                    )

                val response =
                    RetrofitClient.apiService
                        .getHistory(userId)

                if (response.isSuccessful) {

                    val results =
                        response.body() ?: emptyList()

                    recycler.layoutManager =
                        LinearLayoutManager(this@History)

                    recycler.adapter =
                        RecentResultAdapter(results)

                    if (results.isNotEmpty()) {

                        val average =
                            results.map { it.percentage }
                                .average()
                                .toInt()

                        val best =
                            results.maxOf {
                                it.percentage
                            }

                        val total =
                            results.size

                        val favorite =
                            results.groupingBy {
                                it.category
                            }
                                .eachCount()
                                .maxByOrNull {
                                    it.value
                                }
                                ?.key ?: "-"

                        tvAverage.text =
                            "$average %"

                        tvBest.text =
                            "$best %"

                        tvTotal.text =
                            total.toString()

                        tvFavorite.text =
                            traducirCategoria(
                                favorite
                            )

                        tvInsight.text =
                            generarInsight(
                                average
                            )

                        configurarGrafico(
                            chart,
                            results
                        )
                    }
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    private fun generarInsight(
        average: Int
    ): String {

        return when {

            average >= 80 ->
                "🌿 Tu bienestar emocional se mantiene en niveles saludables."

            average >= 60 ->
                "✨ Has mostrado avances positivos recientemente."

            else ->
                "💜 Recuerda dedicar tiempo a tu bienestar y autocuidado."
        }
    }

    private fun traducirCategoria(
        category: String
    ): String {

        return when (category) {

            "WELLNESS" ->
                "Bienestar"

            "STRESS" ->
                "Estrés"

            "SLEEP" ->
                "Sueño"

            "MOOD" ->
                "Estado de ánimo"

            else ->
                "Relaciones"
        }
    }

    private fun configurarGrafico(
        chart: LineChart,
        results: List<TestResult>
    ) {

        val entries =
            ArrayList<Entry>()

        results.reversed()
            .forEachIndexed { index, result ->

                entries.add(

                    Entry(
                        (index + 1).toFloat(),
                        result.percentage.toFloat()
                    )
                )
            }

        val dataSet =
            LineDataSet(
                entries,
                "Bienestar"
            )

        dataSet.color =
            Color.parseColor("#14B8A6")

        dataSet.lineWidth = 4f

        dataSet.setDrawCircles(true)

        dataSet.setDrawValues(false)

        dataSet.mode =
            LineDataSet.Mode.CUBIC_BEZIER

        chart.data =
            LineData(dataSet)

        chart.description.isEnabled =
            false

        chart.axisRight.isEnabled =
            false

        chart.legend.isEnabled =
            false

        chart.invalidate()
    }

    private fun configurarBottomNav() {

        val bottomNav =
            findViewById<BottomNavigationView>(
                R.id.bottomNavigation
            )

        bottomNav.selectedItemId =
            R.id.nav_history

        bottomNav.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {

                    startActivity(
                        Intent(
                            this,
                            Home::class.java
                        )
                    )

                    finish()

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

                R.id.nav_history -> true

                else -> false
            }
        }
    }
}