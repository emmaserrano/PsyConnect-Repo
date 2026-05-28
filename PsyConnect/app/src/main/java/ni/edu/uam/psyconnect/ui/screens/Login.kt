package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ni.edu.uam.psyconnect.R

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Conecta con el XML
        setContentView(R.layout.activity_login)

        // Botón login
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Botón registro
        val btnRegister = findViewById<Button>(R.id.btnGoRegister)

        // Ir al Home
        btnLogin.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        // Ir a Register
        btnRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}