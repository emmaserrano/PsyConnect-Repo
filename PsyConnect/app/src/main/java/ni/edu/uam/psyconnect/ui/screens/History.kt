package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import com.google.android.material.chip.Chip
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.TestResult
import ni.edu.uam.psyconnect.data.model.TestResultEntity
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalDatabase
import ni.edu.uam.psyconnect.data.moodjournal.TestResultRepository
import ni.edu.uam.psyconnect.ui.adapter.RecentResultAdapter
import ni.edu.uam.psyconnect.ui.viewmodel.TestResultViewModel

class History : AppCompatActivity() {

    private var allResults: List<TestResultEntity> = emptyList()
    private lateinit var viewModel: TestResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setupViewModel()
        configurarFiltros()
        configurarBottomNav()
        observarDatos()
    }

    private fun setupViewModel() {
        val database = MoodJournalDatabase.getDatabase(this)
        val repository = TestResultRepository(database.testResultDao())
        
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TestResultViewModel(repository) as T
            }
        })[TestResultViewModel::class.java]

        val userId = getSharedPreferences("psyconnect", MODE_PRIVATE).getLong("userId", 1L)
        viewModel.setUserId(userId)
    }

    private fun observarDatos() {
        lifecycleScope.launch {
            viewModel.results.collectLatest { results ->
                allResults = results
                actualizarInterfaz(results)
            }
        }
    }

    private fun actualizarInterfaz(results: List<TestResultEntity>) {
        val tvHeroAverage = findViewById<TextView>(R.id.tvHeroAverage)
        val tvHeroInsight = findViewById<TextView>(R.id.tvHeroInsight)
        val tvAverage = findViewById<TextView>(R.id.tvAverage)
        val tvBest = findViewById<TextView>(R.id.tvBest)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val tvFavorite = findViewById<TextView>(R.id.tvFavorite)
        val tvInsight = findViewById<TextView>(R.id.tvInsight)
        val recycler = findViewById<RecyclerView>(R.id.recyclerResults)

        recycler.layoutManager = LinearLayoutManager(this)
        filtrarResultados(null)

        if (results.isNotEmpty()) {
            val average = results.map { it.percentage }.average().toInt()
            val best = results.maxOf { it.percentage }
            val total = results.size

            tvAverage.text = "$average%"
            tvHeroAverage.text = "$average%"
            tvBest.text = "$best%"
            tvTotal.text = total.toString()
            tvFavorite.text = obtenerAreaFuerte(results)
            
            val insightText = generarInsight(average)
            tvInsight.text = insightText
            tvHeroInsight.text = insightText

            actualizarBarrasProgreso(results)
        }
    }

    private fun filtrarResultados(category: String?) {
        val recycler = findViewById<RecyclerView>(R.id.recyclerResults)
        val filtered = if (category == null) {
            allResults
        } else {
            allResults.filter { it.category == category }
        }
        
        // Mapeo corregido de TestResultEntity a TestResult
        val adapterList = filtered.map { entity ->
            TestResult(
                id = entity.id.toLong(),
                userId = entity.userId,
                category = entity.category,
                percentage = entity.percentage,
                level = entity.level,
                createdAt = entity.createdAt
            )
        }
        recycler.adapter = RecentResultAdapter(adapterList)
    }

    private fun configurarFiltros() {
        findViewById<Chip>(R.id.chipAll).setOnClickListener { filtrarResultados(null) }
        findViewById<Chip>(R.id.chipWellness).setOnClickListener { filtrarResultados("WELLNESS") }
        findViewById<Chip>(R.id.chipStress).setOnClickListener { filtrarResultados("STRESS") }
        findViewById<Chip>(R.id.chipSleep).setOnClickListener { filtrarResultados("SLEEP") }
        findViewById<Chip>(R.id.chipMood).setOnClickListener { filtrarResultados("MOOD") }
        findViewById<Chip>(R.id.chipRelationships).setOnClickListener { filtrarResultados("RELATIONSHIPS") }
    }

    private fun actualizarBarrasProgreso(results: List<TestResultEntity>) {
        val categories = listOf("WELLNESS", "STRESS", "SLEEP", "MOOD", "RELATIONSHIPS")
        val progressBars = listOf<ProgressBar>(
            findViewById(R.id.progressWellness),
            findViewById(R.id.progressStress),
            findViewById(R.id.progressSleep),
            findViewById(R.id.progressMood),
            findViewById(R.id.progressSocial)
        )
        val textViews = listOf<TextView>(
            findViewById(R.id.tvWellness),
            findViewById(R.id.tvStress),
            findViewById(R.id.tvSleep),
            findViewById(R.id.tvMood),
            findViewById(R.id.tvSocial)
        )
        val names = listOf("Bienestar", "Manejo del estrés", "Sueño", "Estado de ánimo", "Relaciones")
        val emojis = listOf("🌿", "😌", "😴", "😊", "🤝")

        categories.forEachIndexed { i, cat ->
            val prom = promedioCategoria(results, cat)
            progressBars[i].progress = prom
            textViews[i].text = "${emojis[i]} ${names[i]} ($prom%)"
        }
    }

    private fun promedioCategoria(results: List<TestResultEntity>, category: String): Int {
        val catResults = results.filter { it.category == category }
        return if (catResults.isEmpty()) 0 else catResults.map { it.percentage }.average().toInt()
    }

    private fun obtenerAreaFuerte(results: List<TestResultEntity>): String {
        if (results.isEmpty()) return "-"
        val promedios = results.groupBy { it.category }
            .mapValues { it.value.map { r -> r.percentage }.average() }
        val mejor = promedios.maxByOrNull { it.value }?.key ?: "-"
        return traducirCategoria(mejor)
    }

    private fun generarInsight(average: Int): String {
        return when {
            average >= 80 -> "🌿 Tu bienestar general se mantiene en niveles saludables."
            average >= 60 -> "✨ Has mostrado avances positivos en tu bienestar."
            else -> "💜 Dedica tiempo al autocuidado y seguimiento de tus emociones."
        }
    }

    private fun traducirCategoria(category: String): String {
        return when (category) {
            "WELLNESS" -> "Bienestar"
            "STRESS" -> "Manejo del estrés"
            "SLEEP" -> "Sueño"
            "MOOD" -> "Estado de ánimo"
            "RELATIONSHIPS" -> "Relaciones"
            else -> "-"
        }
    }

    private fun configurarBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_history
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> { startActivity(Intent(this, Home::class.java)); finish(); true }
                R.id.nav_profile -> { startActivity(Intent(this, Profile::class.java)); finish(); true }
                R.id.nav_history -> true
                else -> false
            }
        }
    }
}
