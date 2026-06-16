package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
    private var tiempoRestante = 120000L
    private var temporizador: CountDownTimer? = null

    private var countDownTimer: CountDownTimer? = null

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

        val etPassword =
            findViewById<EditText>(R.id.etPassword)

        val etAge =
            findViewById<EditText>(R.id.etAge)

        val btnSendCode =
            findViewById<Button>(R.id.btnSendCode)

        val btnVerifyCode =
            findViewById<Button>(R.id.btnVerifyCode)

        val btnResendCode =
            findViewById<Button>(R.id.btnResendCode)

        val btnRegister =
            findViewById<Button>(R.id.btnRegister)

        val btnBackLogin =
            findViewById<Button>(R.id.btnBackLogin)

        val tvCountdown =
            findViewById<TextView>(R.id.tvCountdown)

        btnSendCode.setOnClickListener {

            val email =
                etEmail.text.toString().trim()

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

                        iniciarTemporizador(
                            tvCountdown,
                            btnSendCode,
                            btnResendCode
                        )
                        Toast.makeText(
                            this@Register,
                            "Código enviado correctamente",
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

        btnResendCode.setOnClickListener {

            btnSendCode.performClick()
        }

        btnVerifyCode.setOnClickListener {

            val email =
                etEmail.text.toString().trim()

            val code =
                etVerificationCode.text.toString().trim()

            if (
                email.isEmpty() ||
                code.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Ingrese correo y código",
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
                            .validateCode(request)

                    if (
                        response.isSuccessful &&
                        response.body() == true
                    ) {

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
                            "Código incorrecto o vencido",
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

        btnRegister.setOnClickListener {

            val name =
                etName.text.toString().trim()

            val username =
                etUsername.text.toString().trim()

            val email =
                etEmail.text.toString().trim()

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

            if (!validarPassword(password)) {

                Toast.makeText(
                    this,
                    """
                    La contraseña debe tener:
                    • 8 caracteres mínimo
                    • 1 mayúscula
                    • 1 minúscula
                    • 1 número
                    """.trimIndent(),
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

                        Toast.makeText(
                            this@Register,
                            response.errorBody()?.string()
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

    private fun iniciarTemporizador(
        btnSendCode: Button,
        btnResendCode: Button,
        tvCountdown: TextView
    ) {

        btnSendCode.isEnabled = false
        btnResendCode.visibility = View.GONE

        temporizador?.cancel()

        temporizador =
            object : CountDownTimer(
                tiempoRestante,
                1000
            ) {

                override fun onTick(
                    millisUntilFinished: Long
                ) {

                    val segundos =
                        millisUntilFinished / 1000

                    tvCountdown.text =
                        "Código válido durante ${segundos}s"
                }

                override fun onFinish() {

                    tvCountdown.text =
                        "El código expiró"

                    btnSendCode.isEnabled = true

                    btnResendCode.visibility =
                        View.VISIBLE
                }
            }

        temporizador?.start()
    }

    private fun validarPassword(
        password: String
    ): Boolean {

        val regex =
            Regex(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$"
            )

        return regex.matches(password)
    }

    private fun iniciarTemporizador(
        tvCountdown: TextView,
        btnSendCode: Button,
        btnResendCode: Button
    ) {

        btnSendCode.isEnabled = false
        btnResendCode.visibility = Button.GONE

        countDownTimer?.cancel()

        countDownTimer =
            object : CountDownTimer(
                120000,
                1000
            ) {

                override fun onTick(
                    millisUntilFinished: Long
                ) {

                    val minutos =
                        millisUntilFinished / 60000

                    val segundos =
                        (millisUntilFinished % 60000) / 1000

                    tvCountdown.text =
                        String.format(
                            "Código válido por %02d:%02d",
                            minutos,
                            segundos
                        )
                }

                override fun onFinish() {

                    tvCountdown.text =
                        "Código expirado"

                    btnSendCode.isEnabled = true

                    btnResendCode.visibility =
                        Button.VISIBLE
                }
            }

        countDownTimer?.start()
    }

    override fun onDestroy() {

        super.onDestroy()

        countDownTimer?.cancel()
    }
}