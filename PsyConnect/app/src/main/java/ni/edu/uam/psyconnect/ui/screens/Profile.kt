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
import kotlin.jvm.java

class Profile : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvAge: TextView

    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)
        tvAge = findViewById(R.id.tvAge)

        val btnEdit = findViewById<Button>(R.id.btnEdit)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val sharedPreferences =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
            )

        userId =
            sharedPreferences.getLong(
                "userId",
                -1
            )

        btnEdit.setOnClickListener {

            val intent =
                Intent(
                    this,
                    EditProfile::class.java
                )

            startActivity(intent)
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

    override fun onResume() {
        super.onResume()

        loadProfile()
    }

    private fun loadProfile() {

        if (userId == -1L) {
            return
        }

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
}