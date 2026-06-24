package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.psyconnect.ui.theme.PsyConnectTheme
import ni.edu.uam.psyconnect.ui.viewmodel.ForgotPasswordViewModel

class ForgotPassword : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[ForgotPasswordViewModel::class.java]
        
        val emailRecibido = intent.getStringExtra("email")
        if (!emailRecibido.isNullOrBlank()) {
            viewModel.onEmailChange(emailRecibido)
        }

        setContent {
            PsyConnectTheme {
                val state by viewModel.uiState.collectAsState()

                LaunchedEffect(state.isCodeVerified) {
                    if (state.isCodeVerified) {
                        val intent = Intent(this@ForgotPassword, ResetPassword::class.java).apply {
                            putExtra("email", state.email)
                        }
                        startActivity(intent)
                        finish()
                    }
                }

                LaunchedEffect(state.error) {
                    state.error?.let {
                        Toast.makeText(this@ForgotPassword, it, Toast.LENGTH_LONG).show()
                        viewModel.clearError()
                    }
                }

                ForgotPasswordScreen(
                    state = state,
                    onEmailChange = viewModel::onEmailChange,
                    onCodeChange = viewModel::onCodeChange,
                    onSendCode = viewModel::sendRecoveryCode,
                    onVerifyCode = viewModel::verifyCode,
                    onBack = { finish() }
                )
            }
        }
    }
}
