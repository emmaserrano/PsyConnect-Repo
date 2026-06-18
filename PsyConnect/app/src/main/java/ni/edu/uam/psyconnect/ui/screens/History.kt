package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val tvWellness =
            findViewById<TextView>(R.id.tvWellness)

        val tvStress =
            findViewById<TextView>(R.id.tvStress)

        val tvSleep =
            findViewById<TextView>(R.id.tvSleep)

        val tvMood =
            findViewById<TextView>(R.id.tvMood)

        val tvSocial =
            findViewById<TextView>(R.id.tvSocial)

        val progressWellness =
            findViewById<ProgressBar>(R.id.progressWellness)

        val progressStress =
            findViewById<ProgressBar>(R.id.progressStress)

        val progressSleep =
            findViewById<ProgressBar>(R.id.progressSleep)

        val progressMood =
            findViewById<ProgressBar>(R.id.progressMood)

        val progressSocial =
            findViewById<ProgressBar>(R.id.progressSocial)

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
                        1L
                    )

                val response =
                    RetrofitClient
                        .apiService
                        .getHistory(userId)

                if (response.isSuccessful) {

                    val results =
                        response.body() ?: emptyList()

                    recycler.layoutManager =
                        LinearLayoutManager(this@History)

                    recycler.adapter =
                        RecentResultAdapter(results.reversed())

                    if (results.isNotEmpty()) {

                        val average =
                            results
                                .map { it.percentage }
                                .average()
                                .toInt()

                        val best =
                            results.maxOf {
                                it.percentage
                            }

                        val total =
                            results.size

                        val favorite =
                            results
                                .groupingBy {
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

                        configurarCategoria(
                            "WELLNESS",
                            results,
                            tvWellness,
                            progressWellness,
                            "🌿 Bienestar"
                        )

                        configurarCategoria(
                            "STRESS",
                            results,
                            tvStress,
                            progressStress,
                            "😌 Estrés"
                        )

                        configurarCategoria(
                            "SLEEP",
                            results,
                            tvSleep,
                            progressSleep,
                            "😴 Sueño"
                        )

                        configurarCategoria(
                            "MOOD",
                            results,
                            tvMood,
                            progressMood,
                            "😊 Estado de ánimo"
                        )

                        configurarCategoria(
                            "SOCIAL",
                            results,
                            tvSocial,
                            progressSocial,
                            "🤝 Relaciones"
                        )
                    }
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    private fun configurarCategoria(

        category: String,
        results: List<TestResult>,
        textView: TextView,
        progressBar: ProgressBar,
        nombre: String

    ) {

        val categoria =
            results.filter {
                it.category == category
            }

        if (categoria.isNotEmpty()) {

            val promedio =
                categoria
                    .map {
                        it.percentage
                    }
                    .average()
                    .toInt()

            textView.text =
                "$nombre • $promedio %"

            progressBar.progress =
                promedio

        } else {

            textView.text =
                "$nombre • Sin datos"

            progressBar.progress =
                0
        }
    }

    private fun generarInsight(
        average: Int
    ): String {

        return when {

            average >= 80 ->
                "🌿 Tu bienestar general es excelente."

            average >= 60 ->
                "✨ Continúas avanzando positivamente."

            else ->
                "💜 Dedica más tiempo al autocuidado."
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

            "SOCIAL" ->
                "Relaciones"

            else ->
                "-"
        }
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