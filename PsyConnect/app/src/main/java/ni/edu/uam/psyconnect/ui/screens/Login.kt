package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ni.edu.uam.psyconnect.R
import kotlin.jvm.java

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Conecta esta pantalla con el XML
        setContentView(R.layout.activity_login)

        // Referencia al botón iniciar sesión
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Referencia al botón crear cuenta
        val btnRegister = findViewById<Button>(R.id.btnGoRegister)

        // Navegar al Home
        btnLogin.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        // Navegar al Register
        btnRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}
