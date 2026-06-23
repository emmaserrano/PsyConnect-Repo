package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalDatabase
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalRepository
import ni.edu.uam.psyconnect.ui.viewmodel.MoodJournalViewModel

class MoodJournalActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicializar la base de datos y el repositorio
        val database = MoodJournalDatabase.getDatabase(this)
        val repository = MoodJournalRepository(database.moodJournalDao())

        // 2. Crear el ViewModel usando una Factoría (Buena práctica de Room + ViewModel)
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MoodJournalViewModel(repository) as T
            }
        })[MoodJournalViewModel::class.java]

        setContent {
            // 3. Pasar el ViewModel a la pantalla
            MoodJournalScreen(
                viewModel = viewModel,
                onBack = { finish() }
            )
        }
    }
}
