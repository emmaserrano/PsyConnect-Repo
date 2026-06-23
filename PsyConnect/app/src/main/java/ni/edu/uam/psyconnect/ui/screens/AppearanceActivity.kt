package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.*

class AppearanceActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("psyconnect", MODE_PRIVATE)

        setContent {
            var isDarkMode by remember { 
                mutableStateOf(sharedPreferences.getBoolean("darkMode", false)) 
            }

            AppearanceScreen(
                isDarkMode = isDarkMode,
                onDarkModeChange = { checked ->
                    isDarkMode = checked
                    sharedPreferences.edit().putBoolean("darkMode", checked).apply()

                    if (checked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    // Recreamos la actividad para aplicar el tema visualmente
                    recreate()
                },
                onBack = { finish() }
            )
        }
    }
}
