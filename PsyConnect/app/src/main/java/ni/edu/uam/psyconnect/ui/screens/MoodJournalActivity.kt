package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalDatabase
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalRepository

class MoodJournalActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = MoodJournalRepository(
            MoodJournalDatabase.getDatabase(this).moodJournalDao()
        )

        setContent {
            MoodJournalScreen(
                repository = repository,
                onBack = { finish() }
            )
        }
    }
}
