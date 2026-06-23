package ni.edu.uam.psyconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.data.model.TestResult
import ni.edu.uam.psyconnect.network.RetrofitClient

class ResultsViewModel : ViewModel() {

    fun syncResultWithServer(userId: Long, category: String, percentage: Int, level: String) {
        viewModelScope.launch {
            try {
                val result = TestResult(
                    userId = userId,
                    category = category,
                    percentage = percentage,
                    level = level
                )
                // Intentamos sincronizar con el servidor (aunque ya se guardó localmente en Room)
                RetrofitClient.apiService.saveResult(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
