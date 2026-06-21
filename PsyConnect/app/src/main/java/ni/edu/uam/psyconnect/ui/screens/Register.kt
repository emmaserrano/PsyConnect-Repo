package ni.edu.uam.psyconnect.ui.screens

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
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
    private var usernameDisponible = false
    private var emailDisponible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etName)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val tvUsernameStatus = findViewById<TextView>(R.id.tvUsernameStatus)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val tvEmailStatus = findViewById<TextView>(R.id.tvEmailStatus)
        val etVerificationCode = findViewById<EditText>(R.id.etVerificationCode)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etBirthdate = findViewById<EditText>(R.id.etBirthdate)
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

        etBirthdate.setOnClickListener {

            val picker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecciona tu fecha de nacimiento")
                    .build()

            picker.show(
                supportFragmentManager,
                "DATE_PICKER"
            )

            picker.addOnPositiveButtonClickListener { selection ->

                val formato =
                    SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    )

                formato.timeZone =
                    TimeZone.getTimeZone("UTC")

                etBirthdate.setText(
                    formato.format(
                        Date(selection)
                    )
                )
            }
        }

        tvPasswordStrength.text = ""
        tvPasswordRequirements.text = ""

        etUsername.addTextChangedListener(

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

                    val username =
                        s.toString()

                    if (username.isBlank()) {

                        tvUsernameStatus.visibility =
                            TextView.GONE

                        usernameDisponible = false

                        return
                    }

                    lifecycleScope.launch {

                        try {

                            val response =
                                RetrofitClient
                                    .apiService
                                    .existsUsername(
                                        username
                                    )

                            if (
                                response.body() == true
                            ) {

                                tvUsernameStatus.visibility =
                                    TextView.VISIBLE

                                tvUsernameStatus.text =
                                    "❌ Nombre de usuario ocupado"

                                tvUsernameStatus.setTextColor(
                                    Color.RED
                                )

                                usernameDisponible = false

                            } else {

                                tvUsernameStatus.visibility =
                                    TextView.VISIBLE

                                tvUsernameStatus.text =
                                    "✅ Nombre de usuario disponible"

                                tvUsernameStatus.setTextColor(
                                    Color.parseColor(
                                        "#2E7D32"
                                    )
                                )

                                usernameDisponible = true
                            }

                        } catch (_: Exception) {}
                    }
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {}
            }
        )

        etEmail.addTextChangedListener(

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

                    val email =
                        s.toString()

                    if (email.isBlank()) {

                        tvEmailStatus.visibility =
                            TextView.GONE

                        emailDisponible = false

                        return
                    }

                    lifecycleScope.launch {

                        try {

                            val response =
                                RetrofitClient
                                    .apiService
                                    .existsEmail(
                                        email
                                    )

                            if (
                                response.body() == true
                            ) {

                                tvEmailStatus.visibility =
                                    TextView.VISIBLE

                                tvEmailStatus.text =
                                    "❌ Correo ya registrado"

                                tvEmailStatus.setTextColor(
                                    Color.RED
                                )

                                emailDisponible = false

                            } else {

                                tvEmailStatus.visibility =
                                    TextView.VISIBLE

                                tvEmailStatus.text =
                                    "✅ Correo disponible"

                                tvEmailStatus.setTextColor(
                                    Color.parseColor(
                                        "#2E7D32"
                                    )
                                )

                                emailDisponible = true
                            }

                        } catch (_: Exception) {}
                    }
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {}
            }
        )

        btnSendCode.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isEmpty()) {
                mostrarAlerta("Campo requerido", "Por favor, ingresa un correo electrónico.")
                return@setOnClickListener
            }

            if (!emailDisponible) {

                mostrarAlerta(
                    "Correo ocupado",
                    "Debes ingresar un correo disponible."
                )

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
                        mostrarAlerta("Error de Envío", "No se pudo enviar el código.")
                    }
                } catch (e: Exception) {
                    mostrarAlerta("Error de Conexión", "No se pudo conectar con el servidor.")
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
                    mostrarAlerta("Error", "Error al validar el código.")
                }
            }
        }

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (password.isEmpty()) {
                    progressPassword.progress = 0
                    tvPasswordStrength.text = ""
                    tvPasswordRequirements.text = ""
                    return
                }

                val tieneLongitud = password.length >= 8
                val tieneMayuscula = password.any { it.isUpperCase() }
                val tieneMinuscula = password.any { it.isLowerCase() }
                val tieneNumero = password.any { it.isDigit() }
                val tieneEspecial = password.any { !it.isLetterOrDigit() }

                var score = 0
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
                        Color.parseColor("#FF9800")
                    }
                    score <= 60 -> {
                        tvPasswordStrength.text = "🟡 Aceptable"
                        Color.parseColor("#FBC02D")
                    }
                    score <= 80 -> {
                        tvPasswordStrength.text = "🟢 Fuerte"
                        Color.parseColor("#4CAF50")
                    }
                    else -> {
                        tvPasswordStrength.text = "✅ Muy fuerte"
                        Color.parseColor("#2E7D32")
                    }
                }
                progressPassword.progressTintList = ColorStateList.valueOf(color)
                actualizarRequisitos(tvPasswordRequirements, tieneLongitud, tieneMayuscula, tieneMinuscula, tieneNumero, tieneEspecial)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val birthdate = etBirthdate.text.toString()

            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || birthdate.isEmpty()) {
                mostrarAlerta("Campos incompletos", "Por favor completa todos los campos, el nombre de usuario es obligatorio.")
                return@setOnClickListener
            }

            if (!emailVerificado) {
                mostrarAlerta("Verificación pendiente", "Debes verificar tu correo antes de registrarte.")
                return@setOnClickListener
            }

            if (!validarPassword(password)) {
                mostrarAlerta("Contraseña insegura", "La contraseña debe cumplir con todos los requisitos.")
                return@setOnClickListener
            }

            if (!cbTerms.isChecked) {
                mostrarAlerta("Términos y condiciones", "Debes aceptar los términos para continuar.")
                return@setOnClickListener
            }

            if (!usernameDisponible) {
                mostrarAlerta("Usuario ocupado", "Selecciona otro nombre de usuario.")
                return@setOnClickListener
            }

            if (!emailDisponible) {
                mostrarAlerta("Correo ocupado", "Selecciona otro correo.")
                return@setOnClickListener
            }

            val user = User(name = name, username = username, email = email, password = password, birthdate = birthdate)
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.registerUser(user)
                    if (response.isSuccessful) {
                        Toast.makeText(this@Register, "¡Bienvenido! Usuario registrado.", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        mostrarAlerta("Error de Registro", "No se pudo crear la cuenta.")
                    }
                } catch (e: Exception) {
                    mostrarAlerta("Error", "Ocurrió un error inesperado.")
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
            .show()
    }

    private fun validarPassword(password: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$")
        return regex.matches(password)
    }

    private fun actualizarRequisitos(tv: TextView, tieneLongitud: Boolean, tieneMayuscula: Boolean, tieneMinuscula: Boolean, tieneNumero: Boolean, tieneEspecial: Boolean) {
        val builder = SpannableStringBuilder()
        fun agregarLinea(cumplido: Boolean, texto: String) {
            val inicio = builder.length
            builder.append("${if (cumplido) "✔" else "✖"} $texto\n")
            builder.setSpan(
                ForegroundColorSpan(if (cumplido) Color.parseColor("#2E7D32") else Color.parseColor("#D32F2F")),
                inicio, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        agregarLinea(tieneLongitud, "Mínimo 8 caracteres")
        agregarLinea(tieneMayuscula, "Una mayúscula")
        agregarLinea(tieneMinuscula, "Una minúscula")
        agregarLinea(tieneNumero, "Un número")
        agregarLinea(tieneEspecial, "Un carácter especial")
        tv.text = builder
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