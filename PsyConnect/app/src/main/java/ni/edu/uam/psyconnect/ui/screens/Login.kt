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
import ni.edu.uam.psyconnect.ui.viewmodel.LoginState
import ni.edu.uam.psyconnect.ui.viewmodel.LoginViewModel

class Login : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        val sharedPreferences = getSharedPreferences("psyconnect", MODE_PRIVATE)

        setContent {
            PsyConnectTheme {
                val loginState by viewModel.loginState.collectAsState()

                LaunchedEffect(loginState) {
                    when (loginState) {
                        is LoginState.Success -> {
                            val state = loginState as LoginState.Success
                            sharedPreferences.edit()
                                .putLong("userId", state.userId)
                                .putBoolean("isLogged", true)
                                .putString("identifier", state.identifier)
                                .apply()

                            Toast.makeText(this@Login, state.message, Toast.LENGTH_SHORT).show()
                            
                            // Verificar si es la primera vez que inicia sesión para completar perfil
                            val hasCompletedProfile = sharedPreferences.getBoolean("profile_setup_${state.userId}", false)
                            
                            if (!hasCompletedProfile) {
                                startActivity(Intent(this@Login, CompleteProfileActivity::class.java))
                            } else {
                                startActivity(Intent(this@Login, Home::class.java))
                            }
                            finish()
                        }
                        is LoginState.Error -> {
                            val state = loginState as LoginState.Error
                            Toast.makeText(this@Login, state.message, Toast.LENGTH_LONG).show()
                            viewModel.resetState()
                        }
                        else -> {}
                    }
                }

                LoginScreen(
                    onLoginClick = { identifier, password ->
                        viewModel.login(identifier, password)
                    },
                    onRegisterClick = {
                        startActivity(Intent(this, Register::class.java))
                    },
                    onForgotPasswordClick = { identifier ->
                        val intent = Intent(this, ForgotPassword::class.java)
                        intent.putExtra("email", identifier)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
