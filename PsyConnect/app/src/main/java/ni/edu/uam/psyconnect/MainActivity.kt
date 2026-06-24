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
        val prefs = getSharedPreferences("psyconnect", MODE_PRIVATE)
        
        // Aplicar modo oscuro antes del super.onCreate
        val darkMode = prefs.getBoolean("darkMode", false)
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)

        val onboardingCompleted = prefs.getBoolean("onboarding_completed", false)
        val isLogged = prefs.getBoolean("isLogged", false)

        val intent = when {
            !onboardingCompleted -> Intent(this, OnboardingActivity::class.java)
            isLogged -> Intent(this, Home::class.java)
            else -> Intent(this, Login::class.java)
        }

        startActivity(intent)
        finish()
    }
}
