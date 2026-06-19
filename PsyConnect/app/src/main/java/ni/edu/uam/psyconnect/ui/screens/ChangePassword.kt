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

        val progressPassword =
            findViewById<ProgressBar>(
                R.id.progressPassword
            )

        val tvPasswordStrength =
            findViewById<TextView>(
                R.id.tvPasswordStrength
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

        etNewPassword.addTextChangedListener(
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

                    val password =
                        s.toString()

                    val tieneLongitud =
                        password.length >= 8

                    val tieneMayuscula =
                        password.any {
                            it.isUpperCase()
                        }

                    val tieneMinuscula =
                        password.any {
                            it.isLowerCase()
                        }

                    val tieneNumero =
                        password.any {
                            it.isDigit()
                        }

                    val tieneEspecial =
                        password.any {
                            !it.isLetterOrDigit()
                        }

                    var score = 0

                    if (tieneLongitud) score += 20
                    if (tieneMayuscula) score += 20
                    if (tieneMinuscula) score += 20
                    if (tieneNumero) score += 20
                    if (tieneEspecial) score += 20

                    progressPassword.progress =
                        score

                    val color =
                        when {

                            score <= 20 -> {

                                tvPasswordStrength.text =
                                    "🔴 Muy débil"

                                Color.RED
                            }

                            score <= 40 -> {

                                tvPasswordStrength.text =
                                    "🟠 Débil"

                                Color.parseColor(
                                    "#FF9800"
                                )
                            }

                            score <= 60 -> {

                                tvPasswordStrength.text =
                                    "🟡 Aceptable"

                                Color.parseColor(
                                    "#FBC02D"
                                )
                            }

                            score <= 80 -> {

                                tvPasswordStrength.text =
                                    "🟢 Fuerte"

                                Color.parseColor(
                                    "#4CAF50"
                                )
                            }

                            else -> {

                                tvPasswordStrength.text =
                                    "✅ Muy fuerte"

                                Color.parseColor(
                                    "#2E7D32"
                                )
                            }
                        }

                    progressPassword.progressTintList =
                        ColorStateList.valueOf(
                            color
                        )
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
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

            if (
                !validarPassword(
                    newPassword
                )
            ) {

                Toast.makeText(
                    this,
                    "La contraseña debe tener mínimo 8 caracteres, mayúscula, minúscula, número y carácter especial",
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
                            "Contraseña actualizada correctamente",
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

    private fun validarPassword(
        password: String
    ): Boolean {

        val regex =
            Regex(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$"
            )

        return regex.matches(
            password
        )
    }
}