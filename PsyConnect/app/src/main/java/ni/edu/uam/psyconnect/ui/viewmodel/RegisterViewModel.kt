package ni.edu.uam.psyconnect.ui.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.data.model.User
import ni.edu.uam.psyconnect.data.model.VerifyCodeRequest
import ni.edu.uam.psyconnect.network.RetrofitClient
import java.util.Locale

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    private var countDownTimer: CountDownTimer? = null
    private var checkUsernameJob: Job? = null
    private var checkEmailJob: Job? = null

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(username = username, isUsernameAvailable = null)
        if (username.isBlank()) return
        
        // Debounce: Espera 500ms antes de consultar a la API para evitar errores de red y estados falsos
        checkUsernameJob?.cancel()
        checkUsernameJob = viewModelScope.launch {
            delay(500)
            try {
                val response = RetrofitClient.apiService.existsUsername(username)
                // Si la API dice true (existe), disponible = false
                _uiState.value = _uiState.value.copy(isUsernameAvailable = response.body() == false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isUsernameAvailable = null)
            }
        }
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, isEmailAvailable = null)
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) return

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

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onBirthdateChange(date: String) {
        _uiState.value = _uiState.value.copy(birthdate = date)
    }

    fun onTermsChange(accepted: Boolean) {
        _uiState.value = _uiState.value.copy(termsAccepted = accepted)
    }

    fun onVerificationCodeChange(code: String) {
        _uiState.value = _uiState.value.copy(verificationCode = code)
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
                    _uiState.value = _uiState.value.copy(codeSent = true, isLoading = false)
                } else {
                    _uiState.value = _uiState.value.copy(error = "No se pudo enviar el código", isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Error de conexión", isLoading = false)
            }
        }
    }

    fun verifyCode() {
        val email = _uiState.value.email
        val code = _uiState.value.verificationCode
        if (email.isBlank() || code.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = RetrofitClient.apiService.validateCode(VerifyCodeRequest(email, code))
                if (response.isSuccessful && response.body() == true) {
                    _uiState.value = _uiState.value.copy(isEmailVerified = true, isLoading = false)
                    countDownTimer?.cancel()
                } else {
                    _uiState.value = _uiState.value.copy(error = "Código incorrecto", isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Error al validar", isLoading = false)
            }
        }
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                _uiState.value = _uiState.value.copy(
                    timerText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds),
                    isTimerActive = true
                )
            }
            override fun onFinish() {
                _uiState.value = _uiState.value.copy(timerText = "00:00", isTimerActive = false)
            }
        }.start()
    }

    fun register() {
        val state = _uiState.value
        val user = User(
            name = state.name,
            username = state.username,
            email = state.email,
            password = state.password,
            birthdate = state.birthdate
        )

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = RetrofitClient.apiService.registerUser(user)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(isRegistered = true, isLoading = false)
                } else {
                    _uiState.value = _uiState.value.copy(error = "Error al registrar usuario", isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Error de conexión", isLoading = false)
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

data class RegisterUiState(
    val name: String = "",
    val username: String = "",
    val isUsernameAvailable: Boolean? = null,
    val email: String = "",
    val isEmailAvailable: Boolean? = null,
    val isEmailVerified: Boolean = false,
    val verificationCode: String = "",
    val codeSent: Boolean = false,
    val password: String = "",
    val birthdate: String = "",
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistered: Boolean = false,
    val timerText: String = "02:00",
    val isTimerActive: Boolean = false
)
