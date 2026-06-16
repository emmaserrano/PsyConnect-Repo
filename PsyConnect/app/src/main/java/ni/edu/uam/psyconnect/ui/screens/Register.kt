package ni.edu.uam.psyconnect.ui.screens

import android.content.res.ColorStateList
import android.graphics.Color
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.User
import ni.edu.uam.psyconnect.data.model.VerifyCodeRequest
import ni.edu.uam.psyconnect.network.RetrofitClient
import java.util.Locale

class Register : AppCompatActivity() {

    private var emailVerificado = false
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etName)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etVerificationCode = findViewById<EditText>(R.id.etVerificationCode)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etAge = findViewById<EditText>(R.id.etAge)
        val btnSendCode = findViewById<Button>(R.id.btnSendCode)
        val btnVerifyCode = findViewById<Button>(R.id.btnVerifyCode)
        val btnResendCode = findViewById<Button>(R.id.btnResendCode)
        val cbTerms = findViewById<CheckBox>(R.id.cbTerms)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnBackLogin = findViewById<Button>(R.id.btnBackLogin)
        val tvCountdown = findViewById<TextView>(R.id.tvCountdown)
        val progressPassword = findViewById<android.widget.ProgressBar>(R.id.progressPassword)
        val tvPasswordStrength = findViewById<TextView>(R.id.tvPasswordStrength)
        val tvPasswordRequirements = findViewById<TextView>(R.id.tvPasswordRequirements)

        btnSendCode.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isEmpty()) {
                mostrarAlerta("Campo requerido", "Por favor, ingresa un correo electrónico.")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.sendVerificationCode(email)
                    if (response.isSuccessful) {
                        tvCountdown.visibility = View.VISIBLE
                        iniciarTemporizador(tvCountdown, btnSendCode, btnResendCode)
                        Toast.makeText(this@Register, "Código enviado correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        val errorDetail = response.errorBody()?.string() ?: "Error desconocido"
                        mostrarAlerta("Error de Envío", "No se pudo enviar el código.\n\nServidor: $errorDetail")
                    }
                } catch (e: Exception) {
                    mostrarAlerta("Error de Conexión", "No se pudo conectar con el servidor.\n\n${e.message}")
                }
            }
        }

        btnResendCode.setOnClickListener {
            btnSendCode.performClick()
        }

        btnVerifyCode.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val code = etVerificationCode.text.toString().trim()

            if (email.isEmpty() || code.isEmpty()) {
                mostrarAlerta("Datos incompletos", "Ingrese correo y código.")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.validateCode(VerifyCodeRequest(email, code))
                    if (response.isSuccessful && response.body() == true) {
                        emailVerificado = true
                        etEmail.isEnabled = false
                        etVerificationCode.isEnabled = false
                        btnVerifyCode.visibility = View.GONE
                        btnSendCode.visibility = View.GONE
                        btnResendCode.visibility = View.GONE
                        tvCountdown.visibility = View.GONE
                        countDownTimer?.cancel()
                        Toast.makeText(this@Register, "Correo verificado correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        mostrarAlerta("Código Incorrecto", "El código de verificación no es válido.")
                    }
                } catch (e: Exception) {
                    mostrarAlerta("Error", "Error al validar el código: ${e.message}")
                }
            }
        }

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                var score = 0
                val tieneLongitud = password.length >= 8
                val tieneMayuscula = password.any { it.isUpperCase() }
                val tieneMinuscula = password.any { it.isLowerCase() }
                val tieneNumero = password.any { it.isDigit() }
                val tieneEspecial = password.any { !it.isLetterOrDigit() }

                if (tieneLongitud) score += 20
                if (tieneMayuscula) score += 20
                if (tieneMinuscula) score += 20
                if (tieneNumero) score += 20
                if (tieneEspecial) score += 20

                progressPassword.progress = score
                
                val color = when {
                    score <= 20 -> {
                        tvPasswordStrength.text = "🔴 Muy débil"
                        Color.RED
                    }
                    score <= 40 -> {
                        tvPasswordStrength.text = "🟠 Débil"
                        Color.parseColor("#FF8C00") // Naranja
                    }
                    score <= 60 -> {
                        tvPasswordStrength.text = "🟡 Aceptable"
                        Color.YELLOW
                    }
                    score <= 80 -> {
                        tvPasswordStrength.text = "🟢 Fuerte"
                        Color.parseColor("#14B8A6") // Verde Turquesa
                    }
                    else -> {
                        tvPasswordStrength.text = "✅ Muy fuerte"
                        Color.parseColor("#0F766E") // Verde Oscuro
                    }
                }
                
                progressPassword.progressTintList = ColorStateList.valueOf(color)

                tvPasswordRequirements.text = """
                    ${if (tieneLongitud) "✔" else "✖"} Mínimo 8 caracteres
                    ${if (tieneMayuscula) "✔" else "✖"} Una mayúscula
                    ${if (tieneMinuscula) "✔" else "✖"} Una minúscula
                    ${if (tieneNumero) "✔" else "✖"} Un número
                    ${if (tieneEspecial) "✔" else "✖"} Un carácter especial
                """.trimIndent()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val age = etAge.text.toString()

            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || age.isEmpty()) {
                mostrarAlerta("Campos incompletos", "Por favor completa todos los campos del formulario.")
                return@setOnClickListener
            }

            if (!emailVerificado) {
                mostrarAlerta("Verificación pendiente", "Debes verificar tu correo antes de registrarte.")
                return@setOnClickListener
            }

            if (!validarPassword(password)) {
                mostrarAlerta("Contraseña débil", "La contraseña no cumple con los requisitos mínimos de seguridad.")
                return@setOnClickListener
            }

            if (!cbTerms.isChecked) {
                mostrarAlerta("Términos y condiciones", "Debes aceptar los términos para continuar.")
                return@setOnClickListener
            }

            val user = User(name = name, username = username, email = email, password = password, age = age.toInt())
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.registerUser(user)
                    if (response.isSuccessful) {
                        Toast.makeText(this@Register, "¡Bienvenido! Usuario registrado.", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        val errorDetail = response.errorBody()?.string() ?: "Error al registrar"
                        mostrarAlerta("Error de Registro", errorDetail)
                    }
                } catch (e: Exception) {
                    mostrarAlerta("Error", "Ocurrió un error inesperado: ${e.message}")
                }
            }
        }

        btnBackLogin.setOnClickListener { finish() }
    }

    private fun mostrarAlerta(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Entendido", null)
            .setIcon(android.R.drawable.stat_notify_error)
            .show()
    }

    private fun validarPassword(password: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$")
        return regex.matches(password)
    }

    private fun iniciarTemporizador(tvCountdown: TextView, btnSendCode: Button, btnResendCode: Button) {
        btnSendCode.isEnabled = false
        btnResendCode.visibility = View.GONE
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutos = millisUntilFinished / 60000
                val segundos = (millisUntilFinished % 60000) / 1000
                tvCountdown.text = String.format(Locale.getDefault(), "Código válido por %02d:%02d", minutos, segundos)
            }

            override fun onFinish() {
                tvCountdown.text = "Código expirado"
                btnSendCode.isEnabled = true
                btnResendCode.visibility = View.VISIBLE
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
