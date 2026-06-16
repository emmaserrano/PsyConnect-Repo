package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
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

        val cbTerms =
            findViewById<CheckBox>(R.id.cbTerms)

        val btnRegister =
            findViewById<Button>(R.id.btnRegister)

        val btnBackLogin =
            findViewById<Button>(R.id.btnBackLogin)

        val tvCountdown =
            findViewById<TextView>(R.id.tvCountdown)

        val progressPassword =
            findViewById<android.widget.ProgressBar>(R.id.progressPassword)

        val tvPasswordStrength =
            findViewById<TextView>(R.id.tvPasswordStrength)

        val tvPasswordRequirements =
            findViewById<TextView>(R.id.tvPasswordRequirements)


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
                        tvCountdown.visibility = View.VISIBLE
                        tiempoRestante = 120000

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

                    if (response.body() == true) {

                        emailVerificado = true

                        etEmail.isEnabled = false

                        etVerificationCode.isEnabled = false

                        btnVerifyCode.visibility = View.GONE

                        btnSendCode.visibility = View.GONE

                        btnResendCode.visibility = View.GONE

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

        etPassword.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    val password =
                        s.toString()

                    var score = 0

                    val tieneLongitud =
                        password.length >= 8

                    val tieneMayuscula =
                        password.any { it.isUpperCase() }

                    val tieneMinuscula =
                        password.any { it.isLowerCase() }

                    val tieneNumero =
                        password.any { it.isDigit() }

                    val tieneEspecial =
                        password.any {
                            !it.isLetterOrDigit()
                        }

                    if (tieneLongitud) score += 20
                    if (tieneMayuscula) score += 20
                    if (tieneMinuscula) score += 20
                    if (tieneNumero) score += 20
                    if (tieneEspecial) score += 20

                    progressPassword.progress =
                        score

                    when {

                        score <= 20 -> {

                            tvPasswordStrength.text =
                                "🔴 Muy débil"
                        }

                        score <= 40 -> {

                            tvPasswordStrength.text =
                                "🟠 Débil"
                        }

                        score <= 60 -> {

                            tvPasswordStrength.text =
                                "🟡 Aceptable"
                        }

                        score <= 80 -> {

                            tvPasswordStrength.text =
                                "🟢 Fuerte"
                        }

                        else -> {

                            tvPasswordStrength.text =
                                "✅ Muy fuerte"
                        }
                    }

                    tvPasswordRequirements.text =
                        """
                ${if (tieneLongitud) "✔" else "✖"} Mínimo 8 caracteres
                ${if (tieneMayuscula) "✔" else "✖"} Una mayúscula
                ${if (tieneMinuscula) "✔" else "✖"} Una minúscula
                ${if (tieneNumero) "✔" else "✖"} Un número
                ${if (tieneEspecial) "✔" else "✖"} Un carácter especial
                """.trimIndent()
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {}
            }
        )

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

            if (!validarPassword(password)) {

                Toast.makeText(
                    this,
                    "La contraseña no cumple los requisitos de seguridad",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

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

            if (!cbTerms.isChecked) {

                Toast.makeText(
                    this,
                    "Debe aceptar los términos y condiciones",
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

    private fun validarPassword(
        password: String
    ): Boolean {

        val regex =
            Regex(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$"
            )

        return regex.matches(password)
    }

    private fun iniciarTemporizador(
        tvCountdown: TextView,
        btnSendCode: Button,
        btnResendCode: Button
    ) {

        btnSendCode.isEnabled = false
        btnResendCode.visibility = View.GONE

        countDownTimer?.cancel()

        countDownTimer =
            object : CountDownTimer(
                120000,
                1000
            ) {

                override fun onTick(
                    millisUntilFinished: Long
                ) {
                    println("TEMPORIZADOR: $millisUntilFinished")

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
                        View.VISIBLE
                }
            }

        countDownTimer?.start()
    }
}