package ni.edu.uam.psyconnect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ni.edu.uam.psyconnect.ui.screens.Login
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Navega automáticamente al Login
        val intent = Intent(this, Login::class.java)
        startActivity(intent)

        // Cierra MainActivity
        finish()
    }
}
