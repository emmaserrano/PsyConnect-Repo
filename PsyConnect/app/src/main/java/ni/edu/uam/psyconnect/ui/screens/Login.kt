package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.LoginRequest
import ni.edu.uam.psyconnect.network.RetrofitClient
import ni.edu.uam.psyconnect.ui.components.CustomMessage

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
            )

        val darkMode =
            sharedPreferences.getBoolean(
                "darkMode",
                false
            )

        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        } else {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        setContentView(R.layout.activity_login)

        val etIdentifier = findViewById<EditText>(R.id.etEmail) // Mismo ID, ahora para email o usuario
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnGoRegister)

        btnLogin.setOnClickListener {

            val identifier = etIdentifier.text.toString().trim()
            val password = etPassword.text.toString()

            if (identifier.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Complete todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response =
                        RetrofitClient.apiService.login(
                            LoginRequest(
                                identifier,
                                password
                            )
                        )

                    if (response.isSuccessful) {
                        val authResponse = response.body()

                        if (authResponse != null && authResponse.success) {
                            val prefs = getSharedPreferences("psyconnect", MODE_PRIVATE)
                            prefs.edit()
                                .putLong("userId", authResponse.userId ?: -1)
                                .putBoolean("isLogged", true)
                                .putString("identifier", identifier)
                                .apply()

                            CustomMessage.success(
                                findViewById(android.R.id.content),
                                authResponse.message
                            )

                            val intent = Intent(this@Login, Home::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            CustomMessage.error(
                                findViewById(android.R.id.content),
                                authResponse?.message ?: "Error al iniciar sesión"
                            )
                        }
                    } else {
                        Toast.makeText(
                            this@Login,
                            "Credenciales incorrectas o error en el servidor",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@Login,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            intent.putExtra("email", etIdentifier.text.toString().trim())
            startActivity(intent)
        }
    }
}