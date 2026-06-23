package ni.edu.uam.psyconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalEntry
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalRepository

class MoodJournalViewModel(
    private val repository: MoodJournalRepository
) : ViewModel() {

    // Convertimos el Flow del repositorio en un StateFlow que Compose puede observar fácilmente
    val entries: StateFlow<List<MoodJournalEntry>> = repository.allEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertEntry(entry: MoodJournalEntry) {
        viewModelScope.launch {
            repository.insert(entry)
        }
    }

    fun updateEntry(entry: MoodJournalEntry) {
        viewModelScope.launch {
            repository.update(entry)
        }
    }

    fun deleteEntry(entry: MoodJournalEntry) {
        viewModelScope.launch {
            repository.delete(entry)
        }
    }
}
