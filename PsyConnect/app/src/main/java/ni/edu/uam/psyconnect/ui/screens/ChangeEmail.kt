package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.ChangeEmailRequest
import ni.edu.uam.psyconnect.data.model.VerifyCodeRequest
import ni.edu.uam.psyconnect.network.RetrofitClient

class ChangeEmail : AppCompatActivity() {


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_change_email)

        val etNewEmail = findViewById<EditText>(R.id.etNewEmail)
        val tvEmailStatus = findViewById<TextView>(R.id.tvEmailStatus)

        val etCode = findViewById<EditText>(R.id.etCode)

        val btnSendCode = findViewById<Button>(R.id.btnSendCode)

        val btnVerify = findViewById<Button>(R.id.btnVerify)

        val userId = getSharedPreferences("psyconnect", MODE_PRIVATE).getLong("userId", -1)

        etNewEmail.addTextChangedListener(

            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    val email =
                        s.toString().trim()

                    if (email.isBlank()) {

                        tvEmailStatus.visibility =
                            TextView.GONE

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

                                emailDisponible =
                                    false

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

                                emailDisponible =
                                    true
                            }

                        } catch (_: Exception) {
                        }
                    }
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        btnSendCode.setOnClickListener {
            if (
                !emailDisponible
            ) {

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
                        .sendVerificationCode(
                            email
                        )

                    Toast.makeText(
                        this@ChangeEmail,
                        "Código enviado",
                        Toast.LENGTH_LONG
                    ).show()

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
            if (
                !emailDisponible
            ) {

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

                    if (
                        valid.body() == true
                    ) {

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

                            Toast.makeText(
                                this@ChangeEmail,
                                "Correo actualizado correctamente",
                                Toast.LENGTH_LONG
                            ).show()

                            finish()

                        } else {

                            Toast.makeText(
                                this@ChangeEmail,
                                response.errorBody()?.string()
                                    ?: "No se pudo actualizar el correo",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {

                        Toast.makeText(
                            this@ChangeEmail,
                            "Código incorrecto",
                            Toast.LENGTH_LONG
                        ).show()
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
}