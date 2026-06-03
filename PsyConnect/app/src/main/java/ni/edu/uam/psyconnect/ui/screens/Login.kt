package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.LoginRequest
import ni.edu.uam.psyconnect.network.RetrofitClient

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

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
                                .putLong(
                                    "userId",
                                    authResponse.userId ?: -1
                                )
                                .apply()

                            Toast.makeText(
                                this@Login,
                                authResponse.message,
                                Toast.LENGTH_LONG
                            ).show()

                            val intent =
                                Intent(
                                    this@Login,
                                    Home::class.java
                                )

                            startActivity(intent)

                            finish()

                        } else {

                            Toast.makeText(
                                this@Login,
                                authResponse?.message
                                    ?: "Error al iniciar sesión",
                                Toast.LENGTH_LONG
                            ).show()
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
        }
    }
}