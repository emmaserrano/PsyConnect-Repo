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

    private lateinit var tvInsight: TextView
    private lateinit var tvAverage: TextView
    private lateinit var chart: LineChart

    private lateinit var adapter: RecentResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history)

        tvInsight =
            findViewById(R.id.tvInsight)

        tvAverage =
            findViewById(R.id.tvAverage)

        chart =
            findViewById(R.id.chartMood)

        val recycler =
            findViewById<RecyclerView>(
                R.id.recyclerResults
            )

        adapter =
            RecentResultAdapter()

        recycler.layoutManager =
            LinearLayoutManager(this)

        recycler.adapter =
            adapter

        cargarHistorial()

        configurarBottomNav()
    }

    private fun cargarHistorial() {

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

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient
                        .apiService
                        .getHistory(userId)

                if (response.isSuccessful) {

                    val results =
                        response.body()
                            ?: emptyList()

                    adapter.updateData(
                        results
                    )

                    calcularPromedio(
                        results
                    )

                    configurarGrafico(
                        results
                    )
                }

            } catch (e: Exception) {

                tvInsight.text =
                    "No se pudieron cargar tus estadísticas."
            }
        }
    }

    private fun calcularPromedio(
        results: List<TestResult>
    ) {

        if (results.isEmpty()) {

            tvAverage.text = "0 %"

            tvInsight.text =
                "Aún no has realizado evaluaciones."

            return
        }

        val average =
            results.map {
                it.percentage
            }.average()

        tvAverage.text =
            "${average.toInt()} %"

        tvInsight.text =
            when {

                average >= 80 ->

                    "🌿 Tu bienestar general es excelente."

                average >= 60 ->

                    "✨ Mantienes un equilibrio emocional saludable."

                average >= 40 ->

                    "🌸 Existen áreas que podrían fortalecerse."

                else ->

                    "💙 Es recomendable prestar mayor atención a tu bienestar emocional."
            }
    }

    private fun configurarGrafico(
        results: List<TestResult>
    ) {

        val entries =
            ArrayList<Entry>()

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
                "Progreso"
            )

        dataSet.color =
            Color.parseColor(
                "#14B8A6"
            )

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

                R.id.nav_history ->
                    true

                else ->
                    false
            }
        }
    }
}