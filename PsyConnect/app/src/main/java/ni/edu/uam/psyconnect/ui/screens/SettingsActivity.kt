package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import ni.edu.uam.psyconnect.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        val appearance =
            findViewById<LinearLayout>(R.id.cardAppearance)

        val security =
            findViewById<LinearLayout>(R.id.cardSecurity)

        val about =
            findViewById<LinearLayout>(R.id.cardAbout)

        appearance.setOnClickListener {

            // Próximo commit
        }

        security.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ChangePassword::class.java
                )
            )
        }

        about.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AboutActivity::class.java
                )
            )
        }

    }

}