package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalDatabase
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalRepository
import ni.edu.uam.psyconnect.ui.theme.PsyConnectTheme
import ni.edu.uam.psyconnect.ui.viewmodel.MoodJournalViewModel

class MoodJournalActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = MoodJournalDatabase.getDatabase(this)
        val repository = MoodJournalRepository(database.moodJournalDao())

        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MoodJournalViewModel(repository) as T
            }
        })[MoodJournalViewModel::class.java]

        val sharedPreferences = getSharedPreferences("psyconnect", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("darkMode", false)

        setContent {
            PsyConnectTheme(darkTheme = isDarkMode) {
                MoodJournalScreen(
                    viewModel = viewModel,
                    onBack = { finish() }
                )
            }
        }
    }
}
