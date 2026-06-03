package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.network.RetrofitClient

class Profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvAge = findViewById<TextView>(R.id.tvAge)

        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val sharedPreferences =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
            )

        val userId =
            sharedPreferences.getLong(
                "userId",
                -1
            )

        if (userId != -1L) {

            lifecycleScope.launch {

                try {

                    val response =
                        RetrofitClient
                            .apiService
                            .getUserById(userId)

                    if (response.isSuccessful) {

                        val user = response.body()

                        if (user != null) {

                            tvName.text = user.name
                            tvEmail.text = user.email
                            tvAge.text = "Edad: ${user.age}"
                        }

                    } else {

                        Toast.makeText(
                            this@Profile,
                            "No se pudo cargar el perfil",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@Profile,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        btnLogout.setOnClickListener {

            sharedPreferences.edit()
                .remove("userId")
                .apply()

            val intent =
                Intent(
                    this,
                    Login::class.java
                )

            startActivity(intent)

            finish()
        }
    }
}