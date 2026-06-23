package ni.edu.uam.psyconnect

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.appcompat.app.AppCompatDelegate
import ni.edu.uam.psyconnect.ui.screens.Home
import ni.edu.uam.psyconnect.ui.screens.Login
import ni.edu.uam.psyconnect.ui.screens.OnboardingActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("psyconnect", MODE_PRIVATE)

        // Aplicar el tema
        val darkMode = sharedPreferences.getBoolean("darkMode", false)
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }

                    LaunchedEffect(Unit) {
                        val onboardingCompleted = sharedPreferences.getBoolean("onboarding_completed", false)
                        val userId = sharedPreferences.getLong("userId", -1)

                        val intent = when {
                            !onboardingCompleted -> Intent(this@MainActivity, OnboardingActivity::class.java)
                            userId != -1L -> Intent(this@MainActivity, Home::class.java)
                            else -> Intent(this@MainActivity, Login::class.java)
                        }

                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}
