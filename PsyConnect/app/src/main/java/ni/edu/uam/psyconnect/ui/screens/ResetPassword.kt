package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.ResetPasswordRequest
import ni.edu.uam.psyconnect.network.RetrofitClient

class ResetPassword : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_reset_password
        )

        val email =
            intent.getStringExtra("email")
                ?: ""

        val etPassword =
            findViewById<EditText>(
                R.id.etNewPassword
            )

        val btnSave =
            findViewById<Button>(
                R.id.btnSavePassword
            )

        btnSave.setOnClickListener {

            val newPassword =
                etPassword.text.toString()

            if (newPassword.length < 8) {

                Toast.makeText(
                    this,
                    "La contraseña debe tener al menos 8 caracteres",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            lifecycleScope.launch {

                try {

                    val response =
                        RetrofitClient
                            .apiService
                            .resetPassword(
                                ResetPasswordRequest(
                                    email,
                                    newPassword
                                )
                            )

                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@ResetPassword,
                            "Contraseña actualizada",
                            Toast.LENGTH_LONG
                        ).show()

                        finish()

                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@ResetPassword,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}