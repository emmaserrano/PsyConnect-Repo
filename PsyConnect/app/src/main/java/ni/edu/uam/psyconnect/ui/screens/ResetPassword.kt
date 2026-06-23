package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.psyconnect.ui.viewmodel.ResetPasswordViewModel

class ResetPassword : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[ResetPasswordViewModel::class.java]
        
        val email = intent.getStringExtra("email") ?: ""

        if (email.isEmpty()) {
            Toast.makeText(this, "Error al recuperar la cuenta", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setContent {
            val state by viewModel.uiState.collectAsState()

            // Manejo de éxito
            LaunchedEffect(state.isSuccess) {
                if (state.isSuccess) {
                    Toast.makeText(this@ResetPassword, "Contraseña actualizada con éxito", Toast.LENGTH_LONG).show()
                    finish() // Vuelve a la pantalla de Login
                }
            }

            // Manejo de errores
            LaunchedEffect(state.error) {
                state.error?.let {
                    Toast.makeText(this@ResetPassword, it, Toast.LENGTH_LONG).show()
                    viewModel.clearError()
                }
            }

            ResetPasswordScreen(
                state = state,
                onPasswordChange = viewModel::onPasswordChange,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                onSave = { viewModel.resetPassword(email) },
                onBack = { finish() }
            )
        }
    }
}
