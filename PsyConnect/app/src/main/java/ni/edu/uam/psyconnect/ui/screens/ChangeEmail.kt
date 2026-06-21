package ni.edu.uam.psyconnect.ui.screens

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.ChangeEmailRequest
import ni.edu.uam.psyconnect.data.model.VerifyCodeRequest
import ni.edu.uam.psyconnect.network.RetrofitClient

class ChangeEmail : AppCompatActivity() {

    private var emailDisponible = true
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_change_email)

        val etNewEmail =
            findViewById<EditText>(R.id.etNewEmail)

        val tvEmailStatus =
            findViewById<TextView>(R.id.tvEmailStatus)

        val etCode =
            findViewById<EditText>(R.id.etCode)

        val btnSendCode =
            findViewById<Button>(R.id.btnSendCode)

        val btnVerify =
            findViewById<Button>(R.id.btnVerify)

        val progressEmail =
            findViewById<ProgressBar>(R.id.progressEmail)

        val lottieSuccess =
            findViewById<LottieAnimationView>(R.id.lottieSuccess)

        val tvCodeStatus =
            findViewById<TextView>(R.id.tvCodeStatus)

        val userId =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
            ).getLong(
                "userId",
                -1
            )

        btnSendCode.isEnabled = false
        btnVerify.isEnabled = false

        // Validación de correo al perder el foco (mejor UX)
        etNewEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val email = etNewEmail.text.toString().trim()
                if (email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tvEmailStatus.visibility = View.VISIBLE
                    tvEmailStatus.text = "⚠ Correo inválido"
                    tvEmailStatus.setTextColor(Color.parseColor("#F57C00"))
                }
            }
        }

        etNewEmail.addTextChangedListener(

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
                        s.toString().trim()

                    lottieSuccess.visibility =
                        View.GONE

                    // Ocultamos el estado mientras escribe para no molestar
                    tvEmailStatus.visibility = View.GONE

                    if (email.isBlank()) {
                        btnSendCode.isEnabled = false
                        btnVerify.isEnabled = false
                        return
                    }

                    if (
                        !android.util.Patterns.EMAIL_ADDRESS
                            .matcher(email)
                            .matches()
                    ) {
                        emailDisponible = false
                        btnSendCode.isEnabled = false
                        btnVerify.isEnabled = false
                        return
                    }

                    progressEmail.visibility =
                        View.VISIBLE

                    lifecycleScope.launch {

                        try {

                            val response =
                                RetrofitClient
                                    .apiService
                                    .existsEmail(email)

                            progressEmail.visibility =
                                View.GONE

                            if (
                                response.body() == true
                            ) {

                                tvEmailStatus.visibility =
                                    View.VISIBLE

                                tvEmailStatus.text =
                                    "❌ Correo ya registrado"

                                tvEmailStatus.setTextColor(
                                    Color.RED
                                )

                                emailDisponible = false

                                btnSendCode.isEnabled = false
                                btnVerify.isEnabled = false

                            } else {

                                tvEmailStatus.visibility =
                                    View.VISIBLE

                                tvEmailStatus.text =
                                    "✅ Correo disponible"

                                tvEmailStatus.setTextColor(
                                    Color.parseColor("#2E7D32")
                                )

                                lottieSuccess.visibility =
                                    View.VISIBLE

                                lottieSuccess.playAnimation()

                                emailDisponible = true

                                btnSendCode.isEnabled = true
                                // btnVerify se habilitará cuando se ingrese el código
                            }

                        } catch (_: Exception) {

                            progressEmail.visibility =
                                View.GONE
                        }
                    }
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {}
            }
        )

        // Limpiar mensaje de código incorrecto mientras se escribe el nuevo código
        etCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvCodeStatus.visibility = View.GONE
                // Habilitar botón de verificar si hay código y el correo ya se envió (etNewEmail deshabilitado)
                btnVerify.isEnabled = s?.isNotEmpty() == true && !etNewEmail.isEnabled
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnSendCode.setOnClickListener {

            if (!emailDisponible) {

                Toast.makeText(
                    this,
                    "Debes ingresar un correo disponible",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            lifecycleScope.launch {

                try {

                    val email =
                        etNewEmail.text.toString()

                    RetrofitClient
                        .apiService
                        .sendVerificationCode(email)

                    Toast.makeText(
                        this@ChangeEmail,
                        "Código enviado",
                        Toast.LENGTH_LONG
                    ).show()

                    etNewEmail.isEnabled = false

                    iniciarTemporizador(btnSendCode)

                } catch (e: Exception) {

                    Toast.makeText(
                        this@ChangeEmail,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        btnVerify.setOnClickListener {

            lifecycleScope.launch {

                try {

                    val email =
                        etNewEmail.text.toString()

                    val code =
                        etCode.text.toString()

                    val valid =
                        RetrofitClient
                            .apiService
                            .validateCode(
                                VerifyCodeRequest(
                                    email,
                                    code
                                )
                            )

                    if (valid.body() == true) {

                        tvCodeStatus.visibility =
                            View.VISIBLE

                        tvCodeStatus.text =
                            "✅ Código verificado"

                        tvCodeStatus.setTextColor(
                            Color.parseColor("#2E7D32")
                        )

                        val response =
                            RetrofitClient
                                .apiService
                                .changeEmail(
                                    ChangeEmailRequest(
                                        userId,
                                        email
                                    )
                                )

                        if (
                            response.isSuccessful
                        ) {

                            lottieSuccess.visibility =
                                View.VISIBLE

                            lottieSuccess.playAnimation()

                            Toast.makeText(
                                this@ChangeEmail,
                                "Correo actualizado correctamente",
                                Toast.LENGTH_LONG
                            ).show()

                            android.os.Handler(
                                mainLooper
                            ).postDelayed({

                                finish()

                            }, 2500)

                        } else {

                            Toast.makeText(
                                this@ChangeEmail,
                                "No se pudo actualizar",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {

                        tvCodeStatus.visibility =
                            View.VISIBLE

                        tvCodeStatus.text =
                            "❌ Código incorrecto"

                        tvCodeStatus.setTextColor(
                            Color.RED
                        )
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@ChangeEmail,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun iniciarTemporizador(
        btnSendCode: Button
    ) {

        countDownTimer?.cancel()

        countDownTimer =
            object : CountDownTimer(
                120000,
                1000
            ) {

                override fun onTick(
                    millisUntilFinished: Long
                ) {

                    btnSendCode.text =
                        "Reenviar en ${
                            millisUntilFinished / 1000
                        } s"
                }

                override fun onFinish() {

                    btnSendCode.isEnabled = true

                    btnSendCode.text =
                        "Enviar código de verificación"
                }
            }

        btnSendCode.isEnabled = false

        countDownTimer?.start()
    }

    override fun onDestroy() {

        super.onDestroy()

        countDownTimer?.cancel()
    }
}
