package ni.edu.uam.psyconnect.ui.screens

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.os.Handler
import android.os.Looper
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

    private var usernameDisponible = true
    private var nombreValido = true
    private var usernameOriginal = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        val etName = findViewById<EditText>(R.id.etName)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etBirthdate = findViewById<EditText>(R.id.etBirthdate)
        val tvUsernameStatus = findViewById<TextView>(R.id.tvUsernameStatus)
        val tvNameStatus = findViewById<TextView>(R.id.tvNameStatus)
        val tvChanges = findViewById<TextView>(R.id.tvChanges)
        val lottieProfileSuccess =findViewById<com.airbnb.lottie.LottieAnimationView>(R.id.lottieProfileSuccess)
        val tvDescriptionCounter = findViewById<TextView>(R.id.tvDescriptionCounter)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val userId =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
            ).getLong(
                "userId",
                -1
            )

        var currentEmail = ""

        var originalName = ""
        var originalDescription = ""
        var originalBirthdate = ""

        fun verificarCambios() {

            val hayCambios =
                etName.text.toString() != originalName ||
                        etUsername.text.toString() != usernameOriginal ||
                        etDescription.text.toString() != originalDescription ||
                        etBirthdate.text.toString() != originalBirthdate

            tvChanges.visibility =
                if (hayCambios)
                    TextView.VISIBLE
                else
                    TextView.GONE
        }

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

            picker.addOnPositiveButtonClickListener {

                val formato =
                    SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    )

                formato.timeZone =
                    TimeZone.getTimeZone("UTC")

                etBirthdate.setText(
                    formato.format(
                        Date(it)
                    )
                )

                verificarCambios()
            }
        }

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient
                        .apiService
                        .getUserById(userId)

                if (response.isSuccessful) {

                    response.body()?.let { user ->

                        usernameOriginal = user.username

                        etName.setText(user.name)
                        etUsername.setText(user.username)
                        etDescription.setText(user.description)
                        tvDescriptionCounter.text = "${etDescription.text.length}/100"
                        etBirthdate.setText(user.birthdate)

                        originalName = user.name
                        originalDescription = user.description ?: ""
                        originalBirthdate = user.birthdate

                        currentEmail = user.email
                    }
                }

            } catch (_: Exception) {
            }
        }

        etUsername.addTextChangedListener(
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

                    verificarCambios()

                    val username =
                        s.toString()

                    if (
                        username.isBlank()
                    ) {
                        return
                    }

                    if (
                        username == usernameOriginal
                    ) {

                        tvUsernameStatus.visibility =
                            TextView.GONE

                        usernameDisponible =
                            true

                        return
                    }

                    lifecycleScope.launch {

                        try {

                            val response =
                                RetrofitClient
                                    .apiService
                                    .existsUsername(
                                        username
                                    )

                            if (
                                response.isSuccessful
                            ) {

                                val existe =
                                    response.body() ?: false

                                if (existe) {

                                    tvUsernameStatus.visibility =
                                        TextView.VISIBLE

                                    tvUsernameStatus.text =
                                        "❌ Nombre de usuario ocupado"

                                    tvUsernameStatus.setTextColor(
                                        Color.RED
                                    )

                                    usernameDisponible =
                                        false

                                } else {

                                    tvUsernameStatus.visibility =
                                        TextView.VISIBLE

                                    tvUsernameStatus.text =
                                        "✅ Nombre de usuario disponible"

                                    tvUsernameStatus.setTextColor(
                                        Color.parseColor(
                                            "#2E7D32"
                                        )
                                    )

                                    usernameDisponible =
                                        true
                                }
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

        etName.addTextChangedListener(
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
                    verificarCambios()
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        etName.addTextChangedListener(

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

                    verificarCambios()

                    val nombre =
                        s.toString().trim()

                    if (
                        nombre.length < 3
                    ) {

                        tvNameStatus.visibility =
                            TextView.VISIBLE

                        tvNameStatus.text =
                            "❌ Ingresa un nombre válido"

                        tvNameStatus.setTextColor(
                            Color.RED
                        )

                        nombreValido = false

                        btnSave.isEnabled = false

                    } else {

                        tvNameStatus.visibility =
                            TextView.VISIBLE

                        tvNameStatus.text =
                            "✅ Nombre válido"

                        tvNameStatus.setTextColor(
                            Color.parseColor(
                                "#2E7D32"
                            )
                        )

                        nombreValido = true

                        btnSave.isEnabled =
                            usernameDisponible
                    }
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        etDescription.addTextChangedListener(
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

                    verificarCambios()

                    tvDescriptionCounter.text =
                        "${s?.length ?: 0}/100"
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        btnSave.setOnClickListener {
            if (
                !nombreValido
            ) {

                Toast.makeText(
                    this,
                    "Ingresa un nombre válido",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            if (
                !usernameDisponible
            ) {

                Toast.makeText(
                    this,
                    "Debes elegir otro nombre de usuario",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            lifecycleScope.launch {

                try {

                    val updatedUser =
                        User(
                            id = userId,
                            name = etName.text.toString(),
                            username = etUsername.text.toString(),
                            email = currentEmail,
                            password = "",
                            birthdate = etBirthdate.text.toString(),
                            description = etDescription.text.toString()
                        )

                    val response =
                        RetrofitClient
                            .apiService
                            .updateUser(
                                userId,
                                updatedUser
                            )

                    if (response.isSuccessful) {

                        lottieProfileSuccess.visibility =
                            TextView.VISIBLE

                        lottieProfileSuccess.playAnimation()

                        Toast.makeText(
                            this@EditProfile,
                            "Perfil actualizado correctamente",
                            Toast.LENGTH_LONG
                        ).show()

                        Handler(
                            Looper.getMainLooper()
                        ).postDelayed({

                            finish()

                        }, 2000)

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
    }
}