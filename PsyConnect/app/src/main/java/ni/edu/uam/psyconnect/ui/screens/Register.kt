package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.User
import ni.edu.uam.psyconnect.network.RetrofitClient

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etAge = findViewById<EditText>(R.id.etAge)

        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {

            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val age = etAge.text.toString()

            if (
                name.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                age.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Complete todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val user = User(
                name = name,
                email = email,
                password = password,
                age = age.toInt()
            )

            lifecycleScope.launch {

                try {

                    val response =
                        RetrofitClient.apiService.registerUser(user)

                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@Register,
                            "Usuario registrado correctamente",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        Toast.makeText(
                            this@Register,
                            "Error al registrar usuario",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@Register,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}