package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.network.RetrofitClient
import ni.edu.uam.psyconnect.ui.adapter.MoodAdapter

class MoodHistoryActivity : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_mood_history
        )

        val recycler =
            findViewById<RecyclerView>(
                R.id.recyclerMoodHistory
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

                    recycler.adapter =
                        MoodAdapter(
                            response.body()
                                ?: emptyList()
                        )
                }

            } catch (_: Exception) {
            }
        }
    }
}