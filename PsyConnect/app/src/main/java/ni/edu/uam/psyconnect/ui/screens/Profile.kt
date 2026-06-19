package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.network.RetrofitClient

class Profile : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvWelcome: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvAge: TextView

    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)
        tvWelcome = findViewById(R.id.tvWelcome)
        tvAge = findViewById(R.id.tvAge)

        val btnEdit =
            findViewById<Button>(R.id.btnEdit)

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

        val switchDarkMode =
            findViewById<SwitchMaterial>(
                R.id.switchDarkMode
            )

        val darkModeEnabled =
            sharedPreferences.getBoolean(
                "darkMode",
                false
            )

        switchDarkMode.isChecked =
            darkModeEnabled

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->

            sharedPreferences.edit()
                .putBoolean(
                    "darkMode",
                    isChecked
                )
                .apply()

            if (isChecked) {

                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )

            } else {

                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }

        btnEdit.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    EditProfile::class.java
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
                            "Edad: ${user.birthdate}"
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