package ni.edu.uam.psyconnect.ui.screens

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.ResetPasswordRequest
import ni.edu.uam.psyconnect.network.RetrofitClient

class ResetPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val email = intent.getStringExtra("email") ?: ""

        if (email.isEmpty()) {
            mostrarAlerta("Error", "No se pudo recuperar la información de la cuenta. Por favor, intenta el proceso de nuevo.")
            finish()
            return
        }
        
        val etPassword = findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnSave = findViewById<Button>(R.id.btnSavePassword)
        val progressPassword = findViewById<ProgressBar>(R.id.progressPassword)
        val tvPasswordStrength = findViewById<TextView>(R.id.tvPasswordStrength)
        val tvPasswordRequirements = findViewById<TextView>(R.id.tvPasswordRequirements)

        // Estado inicial limpio
        tvPasswordStrength.text = ""
        tvPasswordRequirements.text = ""

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

                actualizarRequisitos(
                    tvPasswordRequirements,
                    tieneLongitud,
                    tieneMayuscula,
                    tieneMinuscula,
                    tieneNumero,
                    tieneEspecial
                )
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnSave.setOnClickListener {
            val newPassword = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                mostrarAlerta("Campos incompletos", "Por favor, completa todos los campos.")
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                mostrarAlerta("Error de coincidencia", "Las contraseñas ingresadas no coinciden.")
                return@setOnClickListener
            }

            if (!validarPassword(newPassword)) {
                mostrarAlerta("Contraseña insegura", "La contraseña debe cumplir con todos los requisitos de seguridad marcados en verde.")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.resetPassword(
                        ResetPasswordRequest(email, newPassword)
                    )

                    if (response.isSuccessful) {
                        Toast.makeText(this@ResetPassword, "Contraseña actualizada con éxito", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        val errorDetail = response.errorBody()?.string() ?: "Error desconocido"
                        mostrarAlerta("Error al actualizar", "No se pudo cambiar la contraseña.\n\nServidor: $errorDetail")
                    }

                } catch (e: Exception) {
                    mostrarAlerta("Error de conexión", "No se pudo contactar al servidor: ${e.message}")
                }
            }
        }
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

    private fun actualizarRequisitos(
        tv: TextView,
        tieneLongitud: Boolean,
        tieneMayuscula: Boolean,
        tieneMinuscula: Boolean,
        tieneNumero: Boolean,
        tieneEspecial: Boolean
    ) {
        val builder = SpannableStringBuilder()

        fun agregarLinea(cumplido: Boolean, texto: String) {
            val inicio = builder.length
            builder.append("${if (cumplido) "✔" else "✖"} $texto\n")
            builder.setSpan(
                ForegroundColorSpan(if (cumplido) Color.parseColor("#2E7D32") else Color.parseColor("#D32F2F")),
                inicio,
                builder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        agregarLinea(tieneLongitud, "Mínimo 8 caracteres")
        agregarLinea(tieneMayuscula, "Una mayúscula")
        agregarLinea(tieneMinuscula, "Una minúscula")
        agregarLinea(tieneNumero, "Un número")
        agregarLinea(tieneEspecial, "Un carácter especial")

        tv.text = builder
    }
}
