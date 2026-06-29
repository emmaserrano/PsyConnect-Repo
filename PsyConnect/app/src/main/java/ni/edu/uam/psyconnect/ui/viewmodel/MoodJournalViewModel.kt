package ni.edu.uam.psyconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalEntry
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalRepository

class MoodJournalViewModel(
    private val repository: MoodJournalRepository
) : ViewModel() {

    private val _userId = MutableStateFlow<Long?>(null)
    private val _entries = MutableStateFlow<List<MoodJournalEntry>>(emptyList())
    val entries: StateFlow<List<MoodJournalEntry>> = _entries

    fun setUserId(id: Long) {
        _userId.value = id
        loadEntries()
    }

    private fun loadEntries() {
        val id = _userId.value ?: return
        viewModelScope.launch {
            try {
                val response = ni.edu.uam.psyconnect.network.RetrofitClient.apiService.getMoodHistory(id)
                if (response.isSuccessful) {
                    _entries.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun insertEntry(entry: MoodJournalEntry) {
        viewModelScope.launch {
            try {
                val response = ni.edu.uam.psyconnect.network.RetrofitClient.apiService.saveMood(entry)
                if (response.isSuccessful) {
                    loadEntries() // Recargar historial después de guardar
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateEntry(entry: MoodJournalEntry) {
        // En una implementación real en la nube necesitaríamos un endpoint PUT
        // Por simplicidad si no existe, lo insertamos/sobrescribimos
        insertEntry(entry)
    }

    fun deleteEntry(entry: MoodJournalEntry) {
        viewModelScope.launch {
            try {
                val response = ni.edu.uam.psyconnect.network.RetrofitClient.apiService.deleteMood(entry.id.toLong())
                if (response.isSuccessful) {
                    loadEntries()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
