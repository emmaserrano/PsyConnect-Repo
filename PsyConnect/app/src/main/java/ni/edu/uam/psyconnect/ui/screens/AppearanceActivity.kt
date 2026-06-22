package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import ni.edu.uam.psyconnect.R

class AppearanceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_appearance)

        val switchDarkMode =
            findViewById<SwitchMaterial>(
                R.id.switchDarkMode
            )

        val sharedPreferences =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
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

            recreate()
        }
    }
}