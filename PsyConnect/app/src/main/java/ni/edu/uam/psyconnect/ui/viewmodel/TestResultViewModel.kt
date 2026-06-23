package ni.edu.uam.psyconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.data.model.TestResultEntity
import ni.edu.uam.psyconnect.data.moodjournal.TestResultRepository

@OptIn(ExperimentalCoroutinesApi::class)
class TestResultViewModel(
    private val repository: TestResultRepository
) : ViewModel() {

    private val _userId = MutableStateFlow<Long?>(null)
    
    // Observamos los resultados del usuario actual de forma reactiva
    val results: StateFlow<List<TestResultEntity>> = _userId
        .filterNotNull()
        .flatMapLatest { id -> repository.getResultsByUser(id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setUserId(id: Long) {
        _userId.value = id
    }

    fun insertResult(result: TestResultEntity) {
        viewModelScope.launch {
            repository.insert(result)
        }
    }

    fun deleteResult(result: TestResultEntity) {
        viewModelScope.launch {
            repository.delete(result)
        }
    }
}
