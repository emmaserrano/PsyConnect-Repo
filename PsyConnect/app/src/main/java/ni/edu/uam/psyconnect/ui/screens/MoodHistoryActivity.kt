package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalDatabase
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalRepository
import ni.edu.uam.psyconnect.ui.viewmodel.MoodHistoryViewModel

class MoodHistoryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Base de Datos y Repositorio
        val database = MoodJournalDatabase.getDatabase(this)
        val repository = MoodJournalRepository(database.moodJournalDao())

        // Crear el ViewModel con Factoría
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MoodHistoryViewModel(repository) as T
            }
        })[MoodHistoryViewModel::class.java]

        setContent {
            MoodHistoryScreen(
                viewModel = viewModel,
                onBack = { finish() }
            )
        }
    }
}
