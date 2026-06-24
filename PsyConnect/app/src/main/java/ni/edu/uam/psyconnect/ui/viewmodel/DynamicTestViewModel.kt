package ni.edu.uam.psyconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.data.model.Question
import ni.edu.uam.psyconnect.data.model.TestResultEntity
import ni.edu.uam.psyconnect.data.moodjournal.TestResultRepository
import ni.edu.uam.psyconnect.data.repository.TestRepository
import java.text.SimpleDateFormat
import java.util.*

class DynamicTestViewModel(
    private val repository: TestResultRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DynamicTestUiState())
    val uiState: StateFlow<DynamicTestUiState> = _uiState.asStateFlow()

    private var allQuestions: List<Question> = emptyList()
    private val answers = mutableListOf<Int>()

    fun initTest(category: String) {
        allQuestions = TestRepository.getQuestions(category)
        _uiState.value = _uiState.value.copy(
            category = category,
            totalQuestions = allQuestions.size,
            currentQuestion = allQuestions.firstOrNull()
        )
    }

    fun onOptionSelected(index: Int) {
        _uiState.value = _uiState.value.copy(selectedOptionIndex = index)
    }

    fun nextQuestion(userId: Long, onTestFinished: (Int) -> Unit) {
        val selectedIndex = _uiState.value.selectedOptionIndex ?: return
        
        // Calcular valor de la respuesta (Invertido según tu lógica original: 5 - index)
        val value = 5 - selectedIndex
        answers.add(value)

        val nextIndex = _uiState.value.currentIndex + 1
        if (nextIndex < allQuestions.size) {
            _uiState.value = _uiState.value.copy(
                currentIndex = nextIndex,
                currentQuestion = allQuestions[nextIndex],
                selectedOptionIndex = null
            )
        } else {
            finishTest(userId, onTestFinished)
        }
    }

    private fun finishTest(userId: Long, onTestFinished: (Int) -> Unit) {
        val score = answers.sum()
        val maxScore = allQuestions.size * 5
        val percentage = ((score.toFloat() / maxScore.toFloat()) * 100).toInt().coerceIn(0, 100)

        val createdAt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val level = when {
            percentage >= 80 -> "🌿 Saludable"
            percentage >= 60 -> "✨ Estable"
            else -> "💜 Necesita atención"
        }

        viewModelScope.launch {
            val resultEntity = TestResultEntity(
                category = _uiState.value.category,
                percentage = percentage,
                createdAt = createdAt,
                level = level,
                userId = userId
            )
            repository.insert(resultEntity)
            onTestFinished(percentage)
        }
    }
}

data class DynamicTestUiState(
    val category: String = "",
    val currentIndex: Int = 0,
    val totalQuestions: Int = 0,
    val currentQuestion: Question? = null,
    val selectedOptionIndex: Int? = null,
    val isLoading: Boolean = false
)
