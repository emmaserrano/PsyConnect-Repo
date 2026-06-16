package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
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

        btnSend.setOnClickListener {

            val email =
                etEmail.text.toString()

            lifecycleScope.launch {

                try {

                    val response =
                        RetrofitClient
                            .apiService
                            .sendRecoveryCode(email)

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
    }
}