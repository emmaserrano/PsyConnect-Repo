package ni.edu.uam.psyconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalEntry
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalRepository

class MoodHistoryViewModel(
    private val repository: MoodJournalRepository
) : ViewModel() {

    private val _userId = MutableStateFlow<Long?>(null)

    fun setUserId(id: Long) {
        _userId.value = id
    }

    // Observamos las entradas del diario filtradas por usuario para la gráfica y la lista
    @OptIn(ExperimentalCoroutinesApi::class)
    val moodEntries: StateFlow<List<MoodJournalEntry>> = _userId.flatMapLatest { id ->
        if (id != null) {
            repository.getEntriesByUser(id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}
