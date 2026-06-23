package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.psyconnect.data.moodjournal.AchievementRepository
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalDatabase
import ni.edu.uam.psyconnect.ui.viewmodel.AchievementViewModel

class AchievementsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Base de Datos y Repositorio
        val database = MoodJournalDatabase.getDatabase(this)
        val repository = AchievementRepository(database.achievementDao())

        // Crear el ViewModel con Factoría
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AchievementViewModel(repository) as T
            }
        })[AchievementViewModel::class.java]

        // Obtener el ID del usuario actual
        val userId = getSharedPreferences("psyconnect", MODE_PRIVATE).getLong("userId", -1L)
        if (userId != -1L) {
            viewModel.setUserId(userId)
        }

        setContent {
            AchievementScreen(
                viewModel = viewModel,
                onBack = { finish() }
            )
        }
    }
}
