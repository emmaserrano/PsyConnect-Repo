package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.network.RetrofitClient
import ni.edu.uam.psyconnect.ui.adapter.MoodAdapter

class MoodHistoryActivity : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_mood_history
        )

        val recycler =
            findViewById<RecyclerView>(
                R.id.recyclerMoods
            )

        val chart =
            findViewById<LineChart>(
                R.id.lineChart
            )

        recycler.layoutManager =
            LinearLayoutManager(this)

        val userId =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
            ).getLong(
                "userId",
                -1
            )

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient
                        .apiService
                        .getMoodHistory(
                            userId
                        )

                if (
                    response.isSuccessful
                ) {

                    val moods =
                        response.body()
                            ?: emptyList()

                    recycler.adapter =
                        MoodAdapter(
                            moods
                        )

                    val entries =
                        mutableListOf<Entry>()

                    moods.forEachIndexed {
                            index,
                            mood ->

                        val value =
                            when (
                                mood.mood
                            ) {

                                "EXCELENTE" -> 5f

                                "BIEN" -> 4f

                                "NORMAL" -> 3f

                                "TRISTE" -> 2f

                                else -> 1f
                            }

                        entries.add(
                            Entry(
                                index.toFloat(),
                                value
                            )
                        )
                    }

                    val dataSet =
                        LineDataSet(
                            entries,
                            "Estado emocional"
                        )

                    chart.data =
                        LineData(
                            dataSet
                        )

                    chart.description.text =
                        "Evolución emocional"

                    chart.invalidate()
                }

            } catch (_: Exception) {
            }
        }
    }
}