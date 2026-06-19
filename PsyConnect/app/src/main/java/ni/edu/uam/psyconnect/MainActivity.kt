package ni.edu.uam.psyconnect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ni.edu.uam.psyconnect.ui.screens.Home
import ni.edu.uam.psyconnect.ui.screens.Login
import ni.edu.uam.psyconnect.ui.screens.OnboardingActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("psyconnect", MODE_PRIVATE)

        // Aplicar el tema antes de super.onCreate para evitar el "flash" blanco al abrir la app
        val darkMode = sharedPreferences.getBoolean("darkMode", false)
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)

        // Verificamos si es la primera vez (Onboarding)
        val onboardingCompleted = sharedPreferences.getBoolean("onboarding_completed", false)

        // Verificamos si hay una sesión activa
        val userId = sharedPreferences.getLong("userId", -1)

        val intent = when {
            !onboardingCompleted -> Intent(this, OnboardingActivity::class.java)
            userId != -1L -> Intent(this, Home::class.java)
            else -> Intent(this, Login::class.java)
        }

        startActivity(intent)
        finish()
    }
}
