package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ni.edu.uam.psyconnect.R

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val btnTest = findViewById<Button>(R.id.btnTest)
        val btnHistory = findViewById<Button>(R.id.btnHistory)
        val btnProfile = findViewById<Button>(R.id.btnProfile)

        // Ir al test
        btnTest.setOnClickListener {
            startActivity(Intent(this, Test::class.java))
        }

        // Ir al historial
        btnHistory.setOnClickListener {
            startActivity(Intent(this, History::class.java))
        }

        // Ir al perfil
        btnProfile.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }
    }
}
