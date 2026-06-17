package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.RecoveryCodeRequest
import ni.edu.uam.psyconnect.network.RetrofitClient
import java.util.Locale

class ForgotPassword : AppCompatActivity() {

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val etEmail = findViewById<EditText>(R.id.etEmailRecovery)
        val btnSend = findViewById<Button>(R.id.btnSendRecovery)
        val etCode = findViewById<EditText>(R.id.etRecoveryCode)
        val btnVerify = findViewById<Button>(R.id.btnVerifyRecovery)
        val btnBackLogin = findViewById<Button>(R.id.btnBackLogin)
        val tvRecoveryMessage = findViewById<TextView>(R.id.tvRecoveryMessage)
        val tvRecoveryCountdown = findViewById<TextView>(R.id.tvRecoveryCountdown)

        btnBackLogin.setOnClickListener {
            finish()
        }

        btnSend.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                mostrarAlerta("Campo requerido", "Por favor, ingresa tu correo electrónico.")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    // Cambiado a sendVerificationCode porque el endpoint de recovery dio 404
                    val response = RetrofitClient.apiService.sendVerificationCode(email)

                    if (response.isSuccessful) {
                        btnSend.isEnabled = false
                        tvRecoveryMessage.visibility = View.VISIBLE
                        tvRecoveryMessage.text = "Código enviado a:\n$email"
                        
                        tvRecoveryCountdown.visibility = View.VISIBLE
                        iniciarTemporizador(tvRecoveryCountdown, btnSend)

                        Toast.makeText(this@ForgotPassword, "¡Código enviado con éxito!", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorDetail = response.errorBody()?.string() ?: "Error desconocido"
                        mostrarAlerta("Error del servidor", "No se pudo enviar el código.\n\nServidor: $errorDetail")
                    }

                } catch (e: Exception) {
                    mostrarAlerta("Error de Conexión", "No se pudo conectar con el servidor. Verifica tu internet.")
                }
            }
        }

        btnVerify.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val code = etCode.text.toString().trim()

            if (email.isEmpty() || code.isEmpty()) {
                mostrarAlerta("Datos incompletos", "Debes ingresar el correo y el código.")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.validateRecoveryCode(
                        RecoveryCodeRequest(email, code)
                    )

                    if (response.isSuccessful && response.body() == true) {
                        startActivity(
                            Intent(this@ForgotPassword, ResetPassword::class.java).apply {
                                putExtra("email", email)
                            }
                        )
                        finish()
                    } else {
                        mostrarAlerta("Código Inválido", "El código es incorrecto o ha expirado.")
                    }
                } catch (e: Exception) {
                    mostrarAlerta("Error", "Error al validar: ${e.message}")
                }
            }
        }
    }

    private fun mostrarAlerta(titulo: String, mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Entendido") { dialog, _ -> dialog.dismiss() }
        
        val dialog = builder.create()
        dialog.show()

        // Opcional: Centrar el texto del mensaje para que se vea más profesional
        val messageView = dialog.findViewById<TextView>(android.R.id.message)
        messageView?.gravity = android.view.Gravity.CENTER
    }

    private fun iniciarTemporizador(tvCountdown: TextView, btnSend: Button) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutos = millisUntilFinished / 60000
                val segundos = (millisUntilFinished % 60000) / 1000
                tvCountdown.text = String.format(Locale.getDefault(), "Código válido por %02d:%02d", minutos, segundos)
            }

            override fun onFinish() {
                tvCountdown.text = "El código ha expirado"
                btnSend.isEnabled = true
                mostrarAlerta("Tiempo agotado", "El código ha expirado. Por favor, solicita uno nuevo si no lograste usarlo.")
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
