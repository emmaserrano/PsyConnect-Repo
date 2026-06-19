package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.LoginRequest
import ni.edu.uam.psyconnect.network.RetrofitClient
import ni.edu.uam.psyconnect.ui.components.CustomMessage

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnGoRegister)

        btnLogin.setOnClickListener {

            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {

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
                                email,
                                password
                            )
                        )

                    if (response.isSuccessful) {

                        val authResponse = response.body()

                        if (
                            authResponse != null &&
                            authResponse.success
                        ) {

                            // Guardar userId para futuras pantallas
                            val sharedPreferences =
                                getSharedPreferences(
                                    "psyconnect",
                                    MODE_PRIVATE
                                )

                            sharedPreferences.edit()
                                .putLong("userId", authResponse.userId ?: -1)
                                .putBoolean("isLogged", true)
                                .putString("email", email)
                                .apply()

                            CustomMessage.success(
                                findViewById(android.R.id.content),
                                authResponse.message
                            )

                            val intent =
                                Intent(
                                    this@Login,
                                    Home::class.java
                                )

                            startActivity(intent)

                            finish()

                        } else {

                            CustomMessage.error(
                                findViewById(android.R.id.content),
                                authResponse?.message
                                    ?: "Error al iniciar sesión"
                            )
                        }

                    } else {

                        Toast.makeText(
                            this@Login,
                            "Error de conexión con el servidor",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@Login,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        btnRegister.setOnClickListener {

            val intent =
                Intent(
                    this,
                    Register::class.java
                )

            startActivity(intent)


            tvForgotPassword.setOnClickListener {

                val intent =
                    Intent(
                        this,
                        ForgotPassword::class.java
                    )

                intent.putExtra(
                    "email",
                    etEmail.text.toString().trim()
                )

                startActivity(intent)
            }
        }
    }
}