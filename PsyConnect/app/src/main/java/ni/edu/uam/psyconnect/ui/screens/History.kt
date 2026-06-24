package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalDatabase
import ni.edu.uam.psyconnect.data.moodjournal.TestResultRepository
import ni.edu.uam.psyconnect.ui.theme.PsyConnectTheme
import ni.edu.uam.psyconnect.ui.viewmodel.TestResultViewModel

class History : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = MoodJournalDatabase.getDatabase(this)
        val repository = TestResultRepository(database.testResultDao())
        
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TestResultViewModel(repository) as T
            }
        })[TestResultViewModel::class.java]

        val sharedPreferences = getSharedPreferences("psyconnect", MODE_PRIVATE)
        val userId = sharedPreferences.getLong("userId", 1L)
        val isDarkMode = sharedPreferences.getBoolean("darkMode", false)
        
        viewModel.setUserId(userId)

        setContent {
            PsyConnectTheme(darkTheme = isDarkMode) {
                HistoryScreen(
                    viewModel = viewModel,
                    onBack = { finish() }
                )
            }
        }
    }
}
