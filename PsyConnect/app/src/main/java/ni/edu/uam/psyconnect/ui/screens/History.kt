package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.ui.adapter.RecentResultAdapter

class History : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history)

        val tvInsight =
            findViewById<TextView>(
                R.id.tvInsight
            )

        val tvAverage =
            findViewById<TextView>(
                R.id.tvAverage
            )

        val chart =
            findViewById<LineChart>(
                R.id.chartMood
            )

        val recycler =
            findViewById<RecyclerView>(
                R.id.recyclerResults
            )

        recycler.layoutManager =
            LinearLayoutManager(this)

        recycler.adapter =
            RecentResultAdapter()

        tvAverage.text = "84 %"

        tvInsight.text =
            "Tu bienestar ha mejorado un 12 % esta semana 🌿"

        configurarGrafico(chart)

        configurarBottomNav()
    }

    private fun configurarGrafico(chart: LineChart) {

        val entries =
            arrayListOf(
                Entry(1f,70f),
                Entry(2f,75f),
                Entry(3f,74f),
                Entry(4f,80f),
                Entry(5f,82f),
                Entry(6f,85f),
                Entry(7f,84f)
            )

        val dataSet =
            LineDataSet(
                entries,
                "Bienestar"
            )

        dataSet.color =
            Color.parseColor(
                "#14B8A6"
            )

        dataSet.lineWidth = 4f

        dataSet.setDrawCircles(false)

        dataSet.setDrawValues(false)

        dataSet.mode =
            LineDataSet.Mode.CUBIC_BEZIER

        chart.data =
            LineData(dataSet)

        chart.description.isEnabled = false

        chart.axisRight.isEnabled = false

        chart.legend.isEnabled = false

        chart.xAxis.setDrawGridLines(false)

        chart.axisLeft.setDrawGridLines(false)

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

            when(it.itemId){

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