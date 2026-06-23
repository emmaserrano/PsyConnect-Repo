package ni.edu.uam.psyconnect

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import ni.edu.uam.psyconnect.ui.screens.Home
import ni.edu.uam.psyconnect.ui.screens.Login
import ni.edu.uam.psyconnect.ui.screens.OnboardingActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("psyconnect", MODE_PRIVATE)
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
