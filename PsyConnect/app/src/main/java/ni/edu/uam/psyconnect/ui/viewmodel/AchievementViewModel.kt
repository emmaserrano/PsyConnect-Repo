package ni.edu.uam.psyconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.data.model.AchievementEntity
import ni.edu.uam.psyconnect.data.moodjournal.AchievementRepository

@OptIn(ExperimentalCoroutinesApi::class)
class AchievementViewModel(
    private val repository: AchievementRepository
) : ViewModel() {

    private val _userId = MutableStateFlow<Long?>(null)
    
    // Observamos los logros locales (Room) de forma reactiva
    val achievements: StateFlow<List<AchievementEntity>> = _userId
        .filterNotNull()
        .flatMapLatest { id -> repository.getAchievementsLocal(id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun setUserId(id: Long) {
        _userId.value = id
        refreshAchievements(id)
    }

    fun refreshAchievements(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.refreshAchievements(id)
            _isLoading.value = false
        }
    }
}
