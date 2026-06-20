package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.network.RetrofitClient
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Profile : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvWelcome: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvAge: TextView
    private lateinit var tvUsername: TextView

    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)
        tvWelcome = findViewById(R.id.tvWelcome)
        tvAge = findViewById(R.id.tvAge)
        tvUsername = findViewById(R.id.tvUsername)

        val btnEdit =
            findViewById<Button>(R.id.btnEdit)

        val btnSettings =
            findViewById<Button>(R.id.btnSettings)

        val btnLogout =
            findViewById<Button>(R.id.btnLogout)

        val bottomNav =
            findViewById<BottomNavigationView>(
                R.id.bottomNavigation
            )

        bottomNav.selectedItemId =
            R.id.nav_profile

        bottomNav.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {

                    startActivity(
                        Intent(
                            this,
                            Home::class.java
                        )
                    )

                    finish()

                    true
                }

                R.id.nav_history -> {

                    startActivity(
                        Intent(
                            this,
                            History::class.java
                        )
                    )

                    finish()

                    true
                }

                R.id.nav_profile -> {

                    true
                }

                else -> false
            }
        }

        val sharedPreferences =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
            )

        userId =
            sharedPreferences.getLong(
                "userId",
                -1
            )

        btnEdit.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    EditProfile::class.java
                )
            )
        }

        btnSettings.setOnClickListener {

            startActivity(

                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        }

        btnLogout.setOnClickListener {

            sharedPreferences.edit()
                .remove("userId")
                .apply()

            startActivity(
                Intent(
                    this,
                    Login::class.java
                )
            )

            finish()
        }
    }

    override fun onResume() {

        super.onResume()

        loadProfile()
    }

    private fun calcularEdad(fechaNacimiento: String?): String {

        if (fechaNacimiento.isNullOrBlank()) {
            return "No registrada"
        }

        return try {

            val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val fecha = formato.parse(fechaNacimiento)

            if (fecha == null) {
                return "No registrada"
            }

            val nacimiento = Calendar.getInstance()
            nacimiento.time = fecha

            val hoy = Calendar.getInstance()

            var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)

            if (
                hoy.get(Calendar.DAY_OF_YEAR) <
                nacimiento.get(Calendar.DAY_OF_YEAR)
            ) {
                edad--
            }

            "$edad años"

        } catch (e: Exception) {

            "No registrada"
        }
    }

    private fun loadProfile() {

        if (userId == -1L) {
            return
        }

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

                        tvName.text =
                            user.name

                        tvEmail.text =
                            user.email

                        tvWelcome.text =
                            "Hola ${user.name}, gracias por cuidar de tu bienestar hoy."

                        tvAge.text =
                            "Edad: ${calcularEdad(user.birthdate)}"

                        tvUsername.text =
                            "Usuario: ${user.username}"
                    }

                } else {

                    Toast.makeText(
                        this@Profile,
                        "\"No pudimos cargar tu información en este momento. Intenta nuevamente en unos instantes.\"",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@Profile,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}