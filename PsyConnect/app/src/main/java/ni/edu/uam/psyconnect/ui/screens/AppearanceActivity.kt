package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.*
import ni.edu.uam.psyconnect.ui.theme.PsyConnectTheme

class AppearanceActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("psyconnect", MODE_PRIVATE)

        setContent {
            // Leemos el estado directamente de SharedPreferences para que sea reactivo tras la recreación
            var isDarkMode by remember { 
                mutableStateOf(sharedPreferences.getBoolean("darkMode", false)) 
            }

            PsyConnectTheme(darkTheme = isDarkMode) {
                AppearanceScreen(
                    isDarkMode = isDarkMode,
                    onDarkModeChange = { checked ->
                        // 1. Guardar preferencia
                        sharedPreferences.edit().putBoolean("darkMode", checked).apply()
                        isDarkMode = checked

                        // 2. Aplicar a nivel sistema (esto recreará la actividad automáticamente)
                        if (checked) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }
                    },
                    onBack = { finish() }
                )
            }
        }
    }
}
