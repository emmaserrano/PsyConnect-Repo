package ni.edu.uam.psyconnect.ui.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.data.model.ChangeEmailRequest
import ni.edu.uam.psyconnect.data.model.VerifyCodeRequest
import ni.edu.uam.psyconnect.network.RetrofitClient
import java.util.Locale

class ChangeEmailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChangeEmailUiState())
    val uiState: StateFlow<ChangeEmailUiState> = _uiState.asStateFlow()

    private var checkEmailJob: Job? = null
    private var countDownTimer: CountDownTimer? = null

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, isEmailAvailable = null)
        
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            checkEmailAvailability(email)
        }
    }

    private fun checkEmailAvailability(email: String) {
        checkEmailJob?.cancel()
        checkEmailJob = viewModelScope.launch {
            delay(500)
            try {
                val response = RetrofitClient.apiService.existsEmail(email)
                _uiState.value = _uiState.value.copy(isEmailAvailable = response.body() == false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isEmailAvailable = null)
            }
        }
    }

    fun onCodeChange(code: String) {
        _uiState.value = _uiState.value.copy(code = code)
    }

    fun sendCode() {
        val email = _uiState.value.email
        if (email.isBlank() || _uiState.value.isEmailAvailable != true) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = RetrofitClient.apiService.sendVerificationCode(email)
                if (response.isSuccessful) {
                    startTimer()
                    _uiState.value = _uiState.value.copy(isLoading = false, isCodeSent = true)
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "No se pudo enviar el código")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Error de conexión")
            }
        }
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _uiState.value = _uiState.value.copy(
                    timerText = String.format(Locale.getDefault(), "%02d:%02d", 0, millisUntilFinished / 1000),
                    isTimerActive = true
                )
            }
            override fun onFinish() {
                _uiState.value = _uiState.value.copy(timerText = "00:00", isTimerActive = false)
            }
        }.start()
    }

    fun verifyAndChangeEmail(userId: Long) {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // 1. Validar Código
                val verifyResponse = RetrofitClient.apiService.validateCode(VerifyCodeRequest(state.email, state.code))
                if (verifyResponse.body() == true) {
                    // 2. Cambiar Email
                    val changeResponse = RetrofitClient.apiService.changeEmail(ChangeEmailRequest(userId, state.email))
                    if (changeResponse.isSuccessful) {
                        _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                    } else {
                        _uiState.value = _uiState.value.copy(isLoading = false, error = "Error al actualizar el correo")
                    }
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Código de verificación incorrecto")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Error de conexión: ${e.message}")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }
}

data class ChangeEmailUiState(
    val email: String = "",
    val code: String = "",
    val isEmailAvailable: Boolean? = null,
    val isCodeSent: Boolean = false,
    val isTimerActive: Boolean = false,
    val timerText: String = "02:00",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
