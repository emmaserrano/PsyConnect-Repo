package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ni.edu.uam.psyconnect.R

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Conecta con el XML de registro
        setContentView(R.layout.activity_register)
    }
}
