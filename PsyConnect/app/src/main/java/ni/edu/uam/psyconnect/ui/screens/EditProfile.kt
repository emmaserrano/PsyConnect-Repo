package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.User
import ni.edu.uam.psyconnect.network.RetrofitClient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class EditProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        val etName =
            findViewById<EditText>(R.id.etName)

        val etUsername =
            findViewById<EditText>(R.id.etUsername)

        val etDescription =
            findViewById<EditText>(R.id.etDescription)

        val etBirthdate =
            findViewById<EditText>(R.id.etBirthdate)

        val btnSave =
            findViewById<Button>(R.id.btnSave)

        etBirthdate.setOnClickListener {

            val picker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(
                        "Selecciona tu fecha de nacimiento"
                    )
                    .build()

            picker.show(
                supportFragmentManager,
                "DATE_PICKER"
            )

            picker.addOnPositiveButtonClickListener { selection ->

                val formato =
                    SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    )

                formato.timeZone =
                    TimeZone.getTimeZone("UTC")

                etBirthdate.setText(
                    formato.format(
                        Date(selection)
                    )
                )
            }
        }

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

        var currentEmail = ""

        if (userId != -1L) {

            lifecycleScope.launch {

                try {

                    val response =
                        RetrofitClient
                            .apiService
                            .getUserById(userId)

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
                            response.errorBody()?.string()
                                ?: "No se pudo actualizar",
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

        btnSave.setOnClickListener {

            lifecycleScope.launch {

                try {

                    val updatedUser =
                        User(
                            id = userId,

                            name =
                                etName.text.toString(),

                            username =
                                etUsername.text.toString(),

                            email =
                                currentEmail,

                            password = "",

                            birthdate =
                                etBirthdate.text.toString(),

                            description =
                                etDescription.text.toString()
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
    }
}