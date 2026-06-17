package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.ChangePasswordRequest
import ni.edu.uam.psyconnect.network.RetrofitClient

class ChangePassword : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_change_password
        )

        val etCurrentPassword =
            findViewById<EditText>(
                R.id.etCurrentPassword
            )

        val etNewPassword =
            findViewById<EditText>(
                R.id.etNewPassword
            )

        val etConfirmPassword =
            findViewById<EditText>(
                R.id.etConfirmPassword
            )

        val btnSave =
            findViewById<Button>(
                R.id.btnSavePassword
            )

        val userId =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
            ).getLong(
                "userId",
                -1
            )

        btnSave.setOnClickListener {

            val currentPassword =
                etCurrentPassword.text.toString()

            val newPassword =
                etNewPassword.text.toString()

            val confirmPassword =
                etConfirmPassword.text.toString()

            if (
                currentPassword.isEmpty() ||
                newPassword.isEmpty() ||
                confirmPassword.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Complete todos los campos",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            if (
                newPassword != confirmPassword
            ) {

                Toast.makeText(
                    this,
                    "Las contraseñas no coinciden",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            lifecycleScope.launch {

                try {

                    val response =
                        RetrofitClient
                            .apiService
                            .changePassword(

                                ChangePasswordRequest(

                                    userId,

                                    currentPassword,

                                    newPassword
                                )
                            )

                    if (
                        response.isSuccessful
                    ) {

                        Toast.makeText(
                            this@ChangePassword,
                            "Contraseña actualizada",
                            Toast.LENGTH_LONG
                        ).show()

                        finish()

                    } else {

                        Toast.makeText(
                            this@ChangePassword,
                            response.errorBody()
                                ?.string()
                                ?: "Error",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@ChangePassword,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}