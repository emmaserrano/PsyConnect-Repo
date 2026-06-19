package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.User
import ni.edu.uam.psyconnect.network.RetrofitClient

class EditProfile : AppCompatActivity() {

    private var hasChanges = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        val etName =
            findViewById<EditText>(R.id.etName)

        val etEmail =
            findViewById<EditText>(R.id.etEmail)

        val etBirthdate =
            findViewById<EditText>(R.id.etBirthdate)

        etBirthdate.setOnClickListener {

            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona tu fecha de nacimiento")
                .build()

            picker.show(supportFragmentManager, "DATE_PICKER")

            picker.addOnPositiveButtonClickListener { selection ->

                val formato = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                )

                formato.timeZone = TimeZone.getTimeZone("UTC")

                etBirthdate.setText(
                    formato.format(Date(selection))
                )
            }
        }

        val btnSave =
            findViewById<Button>(R.id.btnSave)
        val btnChangePassword =
            findViewById<Button>(R.id.btnChangePassword)

        val sharedPreferences =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
            )

        val userId =
            sharedPreferences.getLong(
                "userId",
                -1
            )

        if (userId != -1L) {

            lifecycleScope.launch {

                try {

                    val response =
                        RetrofitClient
                            .apiService
                            .getUserById(userId)

                    if (response.isSuccessful) {

                        val user =
                            response.body()

                        if (user != null) {

                            etName.setText(user.name)
                            etEmail.setText(user.email)
                            etBirthdate.setText(user.birthdate)
                        }
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@EditProfile,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        btnSave.setOnClickListener {

            lifecycleScope.launch {

                try {

                    val updatedUser =
                        User(
                            id = userId,
                            name = etName.text.toString(),
                            username = "",
                            email = etEmail.text.toString(),
                            password = "",
                            birthdate = etBirthdate.text.toString()
                        )

                    val response =
                        RetrofitClient
                            .apiService
                            .updateUser(
                                userId,
                                updatedUser
                            )

                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@EditProfile,
                            "Perfil actualizado",
                            Toast.LENGTH_LONG
                        ).show()

                        finish()

                    } else {

                        Toast.makeText(
                            this@EditProfile,
                            "No se pudo actualizar",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@EditProfile,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        btnChangePassword.setOnClickListener {

            startActivity(

                Intent(
                    this,
                    ChangePassword::class.java
                )
            )
        }
    }
}