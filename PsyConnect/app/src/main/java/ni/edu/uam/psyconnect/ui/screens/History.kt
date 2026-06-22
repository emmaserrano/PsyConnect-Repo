package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import com.google.android.material.chip.Chip
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

    private var allResults: List<TestResult> = emptyList()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_history
        )

        cargarHistorial()
        configurarFiltros()
        configurarBottomNav()
    }

    private fun cargarHistorial() {

        val tvHeroAverage =
            findViewById<TextView>(
                R.id.tvHeroAverage
            )

        val tvHeroInsight =
            findViewById<TextView>(
                R.id.tvHeroInsight
            )

        val tvAverage =
            findViewById<TextView>(
                R.id.tvAverage
            )

        val tvBest =
            findViewById<TextView>(
                R.id.tvBest
            )

        val tvTotal =
            findViewById<TextView>(
                R.id.tvTotal
            )

        val tvFavorite =
            findViewById<TextView>(
                R.id.tvFavorite
            )

        val tvInsight =
            findViewById<TextView>(
                R.id.tvInsight
            )

        val recycler =
            findViewById<RecyclerView>(
                R.id.recyclerResults
            )

        val progressWellness =
            findViewById<ProgressBar>(
                R.id.progressWellness
            )

        val progressStress =
            findViewById<ProgressBar>(
                R.id.progressStress
            )

        val progressSleep =
            findViewById<ProgressBar>(
                R.id.progressSleep
            )

        val progressMood =
            findViewById<ProgressBar>(
                R.id.progressMood
            )

        val progressSocial =
            findViewById<ProgressBar>(
                R.id.progressSocial
            )

        val tvWellness =
            findViewById<TextView>(
                R.id.tvWellness
            )

        val tvStress =
            findViewById<TextView>(
                R.id.tvStress
            )

        val tvSleep =
            findViewById<TextView>(
                R.id.tvSleep
            )

        val tvMood =
            findViewById<TextView>(
                R.id.tvMood
            )

        val tvSocial =
            findViewById<TextView>(
                R.id.tvSocial
            )

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
                        .getHistory(
                            userId
                        )

                if (response.isSuccessful) {

                    val results =
                        response.body()
                            ?: emptyList()

                    allResults = results


                    recycler.layoutManager =
                        LinearLayoutManager(
                            this@History
                        )

                    filtrarResultados(null)

                    if (results.isNotEmpty()) {

                        val average =
                            results
                                .map {
                                    it.percentage
                                }
                                .average()
                                .toInt()

                        val best =
                            results.maxOf {
                                it.percentage
                            }

                        val total =
                            results.size

                        tvAverage.text =
                            "$average%"

                        tvHeroAverage.text =
                            "$average%"

                        tvBest.text =
                            "$best%"

                        tvTotal.text =
                            total.toString()

                        tvFavorite.text =
                            obtenerAreaFuerte(
                                results
                            )

                        tvInsight.text =
                            generarInsight(
                                average
                            )

                        tvHeroInsight.text =
                            generarInsight(
                                average
                            )

                        configurarCategorias(

                            results,

                            progressWellness,
                            progressStress,
                            progressSleep,
                            progressMood,
                            progressSocial,

                            tvWellness,
                            tvStress,
                            tvSleep,
                            tvMood,
                            tvSocial
                        )
                    }
                }

            } catch (
                e: Exception
            ) {

                e.printStackTrace()
            }
        }
    }

    private fun filtrarResultados(
        category: String?
    ) {

        val recycler =
            findViewById<RecyclerView>(
                R.id.recyclerResults
            )

        val filtered =

            if (category == null) {

                allResults
                    .sortedByDescending {
                        it.id
                    }

            } else {

                allResults
                    .filter {
                        it.category == category
                    }
                    .sortedByDescending {
                        it.id
                    }
            }

        recycler.adapter =
            RecentResultAdapter(
                filtered
            )
    }

    private fun configurarFiltros() {

        findViewById<Chip>(
            R.id.chipAll
        ).setOnClickListener {

            filtrarResultados(
                null
            )
        }

        findViewById<Chip>(
            R.id.chipWellness
        ).setOnClickListener {

            filtrarResultados(
                "WELLNESS"
            )
        }

        findViewById<Chip>(
            R.id.chipStress
        ).setOnClickListener {

            filtrarResultados(
                "STRESS"
            )
        }

        findViewById<Chip>(
            R.id.chipSleep
        ).setOnClickListener {

            filtrarResultados(
                "SLEEP"
            )
        }

        findViewById<Chip>(
            R.id.chipMood
        ).setOnClickListener {

            filtrarResultados(
                "MOOD"
            )
        }

        findViewById<Chip>(
            R.id.chipRelationships
        ).setOnClickListener {

            filtrarResultados(
                "RELATIONSHIPS"
            )
        }
    }

    private fun configurarCategorias(

        results: List<TestResult>,

        progressWellness: ProgressBar,
        progressStress: ProgressBar,
        progressSleep: ProgressBar,
        progressMood: ProgressBar,
        progressSocial: ProgressBar,

        tvWellness: TextView,
        tvStress: TextView,
        tvSleep: TextView,
        tvMood: TextView,
        tvSocial: TextView

    ) {

        val wellness =
            promedioCategoria(
                results,
                "WELLNESS"
            )

        val stress =
            promedioCategoria(
                results,
                "STRESS"
            )

        val sleep =
            promedioCategoria(
                results,
                "SLEEP"
            )

        val mood =
            promedioCategoria(
                results,
                "MOOD"
            )

        val social =
            promedioCategoria(
                results,
                "RELATIONSHIPS"
            )

        progressWellness.progress =
            wellness

        progressStress.progress =
            stress

        progressSleep.progress =
            sleep

        progressMood.progress =
            mood

        progressSocial.progress =
            social

        tvWellness.text =
            "🌿 Bienestar ($wellness%)"

        tvStress.text =
            "😌 Manejo del estrés ($stress%)"

        tvSleep.text =
            "😴 Sueño ($sleep%)"

        tvMood.text =
            "😊 Estado de ánimo ($mood%)"

        tvSocial.text =
            "🤝 Relaciones ($social%)"
    }

    private fun promedioCategoria(
        results: List<TestResult>,
        category: String
    ): Int {

        val categoryResults =
            results.filter {
                it.category == category
            }

        if (
            categoryResults.isEmpty()
        ) {
            return 0
        }

        return categoryResults
            .map {
                it.percentage
            }
            .average()
            .toInt()
    }

    private fun obtenerAreaFuerte(
        results: List<TestResult>
    ): String {

        val mejoresPromedios =

            results.groupBy {
                it.category
            }
                .mapValues {

                    it.value
                        .map { result ->
                            result.percentage
                        }
                        .average()
                }

        val mejor =
            mejoresPromedios
                .maxByOrNull {
                    it.value
                }
                ?.key ?: "-"

        return traducirCategoria(
            mejor
        )
    }

    private fun generarInsight(
        average: Int
    ): String {

        return when {

            average >= 80 ->
                "🌿 Tu bienestar general se mantiene en niveles saludables."

            average >= 60 ->
                "✨ Has mostrado avances positivos en tu bienestar."

            else ->
                "💜 Dedica tiempo al autocuidado y seguimiento de tus emociones."
        }
    }

    private fun traducirCategoria(
        category: String
    ): String {

        return when (category) {

            "WELLNESS" ->
                "Bienestar"

            "STRESS" ->
                "Manejo del estrés"

            "SLEEP" ->
                "Sueño"

            "MOOD" ->
                "Estado de ánimo"

            "SELF_ESTEEM" ->
                "Autoestima"

            "RELATIONSHIPS" ->
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

                R.id.nav_history ->
                    true

                else ->
                    false
            }
        }
    }
}