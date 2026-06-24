package ni.edu.uam.psyconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalEntry
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalRepository

class MoodHistoryViewModel(
    private val repository: MoodJournalRepository
) : ViewModel() {

    // Observamos todas las entradas del diario para la gráfica y la lista
    val moodEntries: StateFlow<List<MoodJournalEntry>> = repository.allEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
