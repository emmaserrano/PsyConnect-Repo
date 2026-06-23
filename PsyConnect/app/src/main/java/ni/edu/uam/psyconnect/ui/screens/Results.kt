package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.psyconnect.ui.helper.TestInterpreter
import ni.edu.uam.psyconnect.ui.viewmodel.ResultsViewModel

class Results : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[ResultsViewModel::class.java]

        val percentage = intent.getIntExtra("percentage", 0)
        val category = intent.getStringExtra("category") ?: "WELLNESS"
        
        // Obtener datos para la sincronización inicial
        val sharedPreferences = getSharedPreferences("psyconnect", MODE_PRIVATE)
        val userId = sharedPreferences.getLong("userId", 1L)
        val feedback = TestInterpreter.generate(category, percentage)

        // Sincronizar con el servidor (Room ya lo guardó en DynamicTestActivity)
        viewModel.syncResultWithServer(userId, category, percentage, feedback.title)

        setContent {
            ResultsScreen(
                category = category,
                percentage = percentage,
                onNavigateToHistory = {
                    startActivity(Intent(this, History::class.java))
                    finish()
                },
                onNavigateToHome = {
                    startActivity(Intent(this, Home::class.java))
                    finish()
                }
            )
        }
    }
}
