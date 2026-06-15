package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.User
import ni.edu.uam.psyconnect.data.model.VerifyCodeRequest
import ni.edu.uam.psyconnect.network.RetrofitClient

class Register : AppCompatActivity() {

    private var emailVerificado = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        val etName =
            findViewById<EditText>(R.id.etName)

        val etUsername =
            findViewById<EditText>(R.id.etUsername)

        val etEmail =
            findViewById<EditText>(R.id.etEmail)

        val etVerificationCode =
            findViewById<EditText>(R.id.etVerificationCode)

        val btnSendCode =
            findViewById<Button>(R.id.btnSendCode)

        val btnVerifyCode =
            findViewById<Button>(R.id.btnVerifyCode)

        val etPassword =
            findViewById<EditText>(R.id.etPassword)

        val etAge =
            findViewById<EditText>(R.id.etAge)

        val btnRegister =
            findViewById<Button>(R.id.btnRegister)

        val btnBackLogin =
            findViewById<Button>(R.id.btnBackLogin)

        /*
         * ENVIAR CÓDIGO
         */
        btnSendCode.setOnClickListener {

            val email =
                etEmail.text.toString()

            if (email.isEmpty()) {

                Toast.makeText(
                    this,
                    "Ingrese un correo",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            lifecycleScope.launch {

                try {

                    val response =
                        RetrofitClient
                            .apiService
                            .sendVerificationCode(email)

                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@Register,
                            "Código enviado al correo",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        Toast.makeText(
                            this@Register,
                            "No se pudo enviar el código",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@Register,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        /*
         * VERIFICAR CÓDIGO
         */
        btnVerifyCode.setOnClickListener {

            val email =
                etEmail.text.toString()

            val code =
                etVerificationCode.text.toString()

            if (
                email.isEmpty() ||
                code.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Complete correo y código",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            lifecycleScope.launch {

                try {

                    val request =
                        VerifyCodeRequest(
                            email,
                            code
                        )

                    val response =
                        RetrofitClient
                            .apiService
                            .verifyCode(request)

                    if (response.isSuccessful) {

                        emailVerificado = true

                        Toast.makeText(
                            this@Register,
                            "Correo verificado correctamente",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        emailVerificado = false

                        Toast.makeText(
                            this@Register,
                            "Código incorrecto",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@Register,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        /*
         * REGISTRAR USUARIO
         */
        btnRegister.setOnClickListener {

            val name =
                etName.text.toString()

            val username =
                etUsername.text.toString()

            val email =
                etEmail.text.toString()

            val password =
                etPassword.text.toString()

            val age =
                etAge.text.toString()

            if (
                name.isEmpty() ||
                username.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                age.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Complete todos los campos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (!emailVerificado) {

                Toast.makeText(
                    this,
                    "Debe verificar su correo antes de registrarse",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            val user =
                User(
                    name = name,
                    username = username,
                    email = email,
                    password = password,
                    age = age.toInt()
                )

            lifecycleScope.launch {

                try {

                    val response =
                        RetrofitClient
                            .apiService
                            .registerUser(user)

                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@Register,
                            "Usuario registrado correctamente",
                            Toast.LENGTH_LONG
                        ).show()

                        finish()

                    } else {

                        val errorMessage =
                            response.errorBody()?.string()

                        Toast.makeText(
                            this@Register,
                            errorMessage
                                ?: "Error al registrar usuario",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@Register,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        btnBackLogin.setOnClickListener {

            finish()
        }
    }
}