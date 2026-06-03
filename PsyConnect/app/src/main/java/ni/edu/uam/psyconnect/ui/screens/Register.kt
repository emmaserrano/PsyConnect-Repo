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
import ni.edu.uam.psyconnect.data.repository.AuthRepository

class Register : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etAge: EditText
    private lateinit var btnRegister: Button

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etAge = findViewById(R.id.etAge)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {

            registerUser()
        }
    }

    private fun registerUser() {

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

            return
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
                    authRepository.registerUser(user)

                if (response.isSuccessful) {

                    Toast.makeText(
                        this@Register,
                        "Usuario registrado correctamente",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()

                } else {

                    Toast.makeText(
                        this@Register,
                        "No se pudo registrar",
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