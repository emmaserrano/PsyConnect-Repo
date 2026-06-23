package ni.edu.uam.psyconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.data.model.User
import ni.edu.uam.psyconnect.network.RetrofitClient
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfileViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _age = MutableStateFlow("No registrada")
    val age: StateFlow<String> = _age

    fun loadProfile(userId: Long) {
        if (userId == -1L) return
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserById(userId)
                if (response.isSuccessful) {
                    val userData = response.body()
                    _user.value = userData
                    _age.value = calcularEdad(userData?.birthdate)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun calcularEdad(fechaNacimiento: String?): String {
        if (fechaNacimiento.isNullOrBlank()) return "No registrada"
        return try {
            val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val fecha = formato.parse(fechaNacimiento) ?: return "No registrada"
            val nacimiento = Calendar.getInstance().apply { time = fecha }
            val hoy = Calendar.getInstance()
            var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)
            if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) edad--
            "$edad años"
        } catch (e: Exception) {
            "No registrada"
        }
    }
}
