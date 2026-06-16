package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.RecoveryCodeRequest
import ni.edu.uam.psyconnect.network.RetrofitClient

class ForgotPassword : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_forgot_password
        )

        val etEmail =
            findViewById<EditText>(
                R.id.etEmailRecovery
            )

        val btnSend =
            findViewById<Button>(
                R.id.btnSendRecovery
            )

        val etCode =
            findViewById<EditText>(
                R.id.etRecoveryCode
            )

        val btnVerify =
            findViewById<Button>(
                R.id.btnVerifyRecovery
            )

        val btnBackLogin =
            findViewById<Button>(
                R.id.btnBackLogin
            )

        btnBackLogin.setOnClickListener {
            finish()
        }

        btnSend.setOnClickListener {

            val email =
                etEmail.text.toString()

            lifecycleScope.launch {

                try {

                    val response =
                        RetrofitClient
                            .apiService
                            .sendVerificationCode(email)

                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@ForgotPassword,
                            "Correo enviado",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@ForgotPassword,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        btnVerify.setOnClickListener {

            val email =
                etEmail.text.toString()

            val code =
                etCode.text.toString()

            lifecycleScope.launch {

                try {

                    val response =
                        RetrofitClient
                            .apiService
                            .validateRecoveryCode(
                                RecoveryCodeRequest(
                                    email,
                                    code
                                )
                            )

                    if (response.body() == true) {

                        startActivity(

                            Intent(
                                this@ForgotPassword,
                                ResetPassword::class.java
                            ).apply {

                                putExtra(
                                    "email",
                                    email
                                )
                            }
                        )

                        finish()

                    } else {

                        Toast.makeText(
                            this@ForgotPassword,
                            "Código incorrecto",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@ForgotPassword,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}