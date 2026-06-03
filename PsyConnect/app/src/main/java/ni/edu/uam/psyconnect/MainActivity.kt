package ni.edu.uam.psyconnect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ni.edu.uam.psyconnect.ui.screens.Home
import ni.edu.uam.psyconnect.ui.screens.Login

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("psyconnect", MODE_PRIVATE)

        val isLogged = sharedPreferences.getBoolean("isLogged", false)

        if (isLogged) {

            val intent = Intent(this, Home::class.java)
            startActivity(intent)

        } else {

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        finish()
    }
}