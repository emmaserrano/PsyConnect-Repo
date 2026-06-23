package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalDatabase
import ni.edu.uam.psyconnect.data.moodjournal.TestResultRepository
import ni.edu.uam.psyconnect.ui.viewmodel.DynamicTestViewModel

class DynamicTestActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val category = intent.getStringExtra("category") ?: "WELLNESS"
        
        // Configuración de la base de datos y el ViewModel con Factoría
        val database = MoodJournalDatabase.getDatabase(this)
        val repository = TestResultRepository(database.testResultDao())
        
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DynamicTestViewModel(repository) as T
            }
        })[DynamicTestViewModel::class.java]

        // Inicializar el test
        viewModel.initTest(category)

        val sharedPreferences = getSharedPreferences("psyconnect", MODE_PRIVATE)
        val userId = sharedPreferences.getLong("userId", 1L)

        setContent {
            val state by viewModel.uiState.collectAsState()

            DynamicTestScreen(
                state = state,
                onOptionSelected = { index ->
                    viewModel.onOptionSelected(index)
                },
                onNextClick = {
                    viewModel.nextQuestion(userId) { percentage ->
                        // Navegar a la pantalla de resultados al terminar
                        val intent = Intent(this@DynamicTestActivity, Results::class.java).apply {
                            putExtra("category", category)
                            putExtra("percentage", percentage)
                        }
                        startActivity(intent)
                        finish()
                    }
                }
            )
        }
    }
}
