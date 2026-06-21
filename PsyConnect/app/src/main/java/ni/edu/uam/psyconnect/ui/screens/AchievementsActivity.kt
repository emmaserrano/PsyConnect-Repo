package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.network.RetrofitClient
import ni.edu.uam.psyconnect.ui.adapter.AchievementAdapter

class AchievementsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        // 1. Inicialización de vistas
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvTotalAchievements = findViewById<TextView>(R.id.tvTotalAchievements)
        val recycler = findViewById<RecyclerView>(R.id.recyclerAchievements)
        val layoutEmptyState = findViewById<LinearLayout>(R.id.layoutEmptyState)
        val progressLoading = findViewById<ProgressBar>(R.id.progressLoading)

        // 2. Configuración inicial
        btnBack.setOnClickListener { finish() }
        recycler.layoutManager = LinearLayoutManager(this)
        
        val userId = getSharedPreferences("psyconnect", MODE_PRIVATE).getLong("userId", -1)

        // 3. Carga de datos
        loadAchievements(userId, tvTotalAchievements, recycler, layoutEmptyState, progressLoading)
    }

    private fun loadAchievements(
        userId: Long,
        tvTotal: TextView,
        recycler: RecyclerView,
        emptyState: LinearLayout,
        progress: ProgressBar
    ) {
        if (userId == -1L) return

        progress.visibility = View.VISIBLE
        emptyState.visibility = View.GONE
        recycler.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getAchievements(userId)

                progress.visibility = View.GONE

                if (response.isSuccessful) {
                    val achievements = response.body() ?: emptyList()
                    
                    if (achievements.isEmpty()) {
                        tvTotal.text = "0 insignias obtenidas"
                        emptyState.visibility = View.VISIBLE
                        recycler.visibility = View.GONE
                    } else {
                        tvTotal.text = "${achievements.size} insignias obtenidas"
                        emptyState.visibility = View.GONE
                        recycler.visibility = View.VISIBLE
                        recycler.adapter = AchievementAdapter(achievements)
                    }
                } else {
                    tvTotal.text = "Error al cargar"
                    emptyState.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                progress.visibility = View.GONE
                tvTotal.text = "Sin conexión"
                emptyState.visibility = View.VISIBLE
                e.printStackTrace()
            }
        }
    }
}
