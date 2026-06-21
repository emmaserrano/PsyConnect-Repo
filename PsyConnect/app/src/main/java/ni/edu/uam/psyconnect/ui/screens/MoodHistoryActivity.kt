package ni.edu.uam.psyconnect.ui.screens

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.Mood
import ni.edu.uam.psyconnect.network.RetrofitClient
import ni.edu.uam.psyconnect.ui.adapter.MoodAdapter

class MoodHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_history)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        val recycler = findViewById<RecyclerView>(R.id.recyclerMoods)
        val chart = findViewById<LineChart>(R.id.lineChart)

        recycler.layoutManager = LinearLayoutManager(this)

        val userId = getSharedPreferences("psyconnect", MODE_PRIVATE).getLong("userId", -1)

        setupChartStyle(chart)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getMoodHistory(userId)
                if (response.isSuccessful) {
                    val moods = response.body() ?: emptyList()
                    recycler.adapter = MoodAdapter(moods)
                    updateChart(chart, moods)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupChartStyle(chart: LineChart) {
        chart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(false)
            setDrawGridBackground(false)
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = Color.GRAY
            }
            axisLeft.apply {
                setDrawGridLines(true)
                textColor = Color.GRAY
                axisMinimum = 0f
                axisMaximum = 6f
                labelCount = 6
            }
            axisRight.isEnabled = false
            legend.isEnabled = false
            animateX(1000)
        }
    }

    private fun updateChart(chart: LineChart, moods: List<Mood>) {
        if (moods.isEmpty()) return

        val entries = moods.reversed().mapIndexed { index, moodRecord ->
            val value = when (moodRecord.mood.uppercase()) {
                "EXCELENTE" -> 5f
                "BIEN" -> 4f
                "NORMAL" -> 3f
                "TRISTE" -> 2f
                else -> 1f
            }
            Entry(index.toFloat(), value)
        }

        val dataSet = LineDataSet(entries, "Estado emocional").apply {
            color = Color.parseColor("#14B8A6")
            setCircleColor(Color.parseColor("#0F766E"))
            lineWidth = 3f
            circleRadius = 5f
            setDrawCircleHole(false)
            valueTextSize = 0f
            setDrawFilled(true)
            fillColor = Color.parseColor("#14B8A6")
            fillAlpha = 50
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        chart.data = LineData(dataSet)
        chart.invalidate()
    }
}
