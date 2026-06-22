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
        val tvRecoveryMessage = findViewById<TextView>(R.id.tvRecoveryMessage)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)

        val emailRecibido = intent.getStringExtra("email")

        val esCorreoValido =
            !emailRecibido.isNullOrBlank() &&
                    android.util.Patterns.EMAIL_ADDRESS
                        .matcher(emailRecibido)
                        .matches()

        if (esCorreoValido) {

            etEmail.setText(emailRecibido)

            etEmail.isEnabled = false

            tvDescription.text =
                "Te enviaremos un código de verificación al correo mostrado a continuación."

        } else {

            etEmail.setText("")

            etEmail.isEnabled = true

            tvDescription.text =
                "Ingresa el correo asociado a tu cuenta para enviarte un código de recuperación."

        }

        val btnSend = findViewById<Button>(R.id.btnSendRecovery)
        val etCode = findViewById<EditText>(R.id.etRecoveryCode)
        val btnVerify = findViewById<Button>(R.id.btnVerifyRecovery)
        val btnBackLogin = findViewById<Button>(R.id.btnBackLogin)
        val tvRecoveryCountdown = findViewById<TextView>(R.id.tvRecoveryCountdown)

        btnBackLogin.setOnClickListener {
            finish()
        }

        btnSend.setOnClickListener {

            val email =
                etEmail.text.toString().trim()

            if(email.isEmpty()){

                Toast.makeText(
                    this,
                    "Ingrese un correo",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            lifecycleScope.launch {

                try {
                    // Deshabilitamos el botón para evitar múltiples clics durante la petición
                    btnSend.isEnabled = false

                    val existsResponse =
                        RetrofitClient
                            .apiService
                            .existsEmail(email)

                    if (
                        existsResponse.body() != true
                    ) {
                        btnSend.isEnabled = true
                        Toast.makeText(
                            this@ForgotPassword,
                            "No existe ninguna cuenta asociada a este correo",
                            Toast.LENGTH_LONG
                        ).show()

                        return@launch
                    }

                    val response =
                        RetrofitClient
                            .apiService
                            .sendVerificationCode(email)

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ForgotPassword,
                            "Código enviado correctamente",
                            Toast.LENGTH_LONG
                        ).show()

                        tvRecoveryMessage.visibility = View.VISIBLE

                        if (esCorreoValido) {

                            tvRecoveryMessage.text =
                                "✓ Se ha enviado un código de verificación al correo mostrado."

                        } else {

                            tvRecoveryMessage.text =
                                "✓ Se ha enviado un código de verificación al correo ingresado."

                        }

                        iniciarTemporizador(tvRecoveryCountdown, btnSend)

                    } else {
                        btnSend.isEnabled = true
                        Toast.makeText(
                            this@ForgotPassword,
                            "No se pudo enviar el código",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {
                    btnSend.isEnabled = true
                    Toast.makeText(
                        this@ForgotPassword,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
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

        val messageView = dialog.findViewById<TextView>(android.R.id.message)
        messageView?.gravity = android.view.Gravity.CENTER
    }

    private fun iniciarTemporizador(tvCountdown: TextView, btnSend: Button) {
        tvCountdown.visibility = View.VISIBLE
        btnSend.isEnabled = false
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
