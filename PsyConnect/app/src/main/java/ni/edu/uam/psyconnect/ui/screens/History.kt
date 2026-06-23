package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalDatabase
import ni.edu.uam.psyconnect.data.moodjournal.TestResultRepository
import ni.edu.uam.psyconnect.ui.viewmodel.TestResultViewModel

class History : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración de la base de datos y el ViewModel
        val database = MoodJournalDatabase.getDatabase(this)
        val repository = TestResultRepository(database.testResultDao())
        
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TestResultViewModel(repository) as T
            }
        })[TestResultViewModel::class.java]

        // Obtener el ID del usuario para filtrar los datos
        val userId = getSharedPreferences("psyconnect", MODE_PRIVATE).getLong("userId", 1L)
        viewModel.setUserId(userId)

        setContent {
            // Llamamos a la nueva pantalla de Compose
            HistoryScreen(
                viewModel = viewModel,
                onBack = { finish() }
            )
        }
    }
}
