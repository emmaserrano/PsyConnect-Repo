package ni.edu.uam.psyconnect.ui.screens

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        
        val etPassword = findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnSave = findViewById<Button>(R.id.btnSavePassword)
        val progressPassword = findViewById<ProgressBar>(R.id.progressPassword)
        val tvPasswordStrength = findViewById<TextView>(R.id.tvPasswordStrength)
        val tvPasswordRequirements = findViewById<TextView>(R.id.tvPasswordRequirements)

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
                
                // Cambiar color de la barra y texto según el score
                val color = when {
                    score <= 20 -> {
                        tvPasswordStrength.text = "🔴 Muy débil"
                        Color.RED
                    }
                    score <= 40 -> {
                        tvPasswordStrength.text = "🟠 Débil"
                        Color.parseColor("#FF8C00") // Naranja oscuro
                    }
                    score <= 60 -> {
                        tvPasswordStrength.text = "🟡 Aceptable"
                        Color.YELLOW
                    }
                    score <= 80 -> {
                        tvPasswordStrength.text = "🟢 Fuerte"
                        Color.parseColor("#14B8A6") // Turquesa principal
                    }
                    else -> {
                        tvPasswordStrength.text = "✅ Muy fuerte"
                        Color.parseColor("#0F766E") // Turquesa oscuro
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

        btnSave.setOnClickListener {
            val newPassword = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                mostrarAlerta("Campos incompletos", "Por favor, completa todos los campos.")
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                mostrarAlerta("Error de coincidencia", "Las contraseñas no coinciden.")
                return@setOnClickListener
            }

            if (!validarPassword(newPassword)) {
                mostrarAlerta("Contraseña insegura", "La contraseña debe cumplir con todos los requisitos de seguridad.")
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
                    mostrarAlerta("Error de conexión", "Error de red: ${e.message}")
                }
            }
        }
    }

    private fun mostrarAlerta(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .setIcon(android.R.drawable.stat_notify_error)
            .show()
    }

    private fun validarPassword(password: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$")
        return regex.matches(password)
    }
}
