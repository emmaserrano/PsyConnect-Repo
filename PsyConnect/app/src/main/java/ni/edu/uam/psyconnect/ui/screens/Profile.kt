package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.network.RetrofitClient
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Profile : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvUsernameHeader: TextView
    private lateinit var tvWelcome: TextView
    private lateinit var tvAge: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvDescription: TextView
    private lateinit var imgProfile: ImageView
    
    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inicialización de vistas
        tvName = findViewById(R.id.tvName)
        tvUsernameHeader = findViewById(R.id.tvUsernameHeader)
        tvWelcome = findViewById(R.id.tvWelcome)
        tvAge = findViewById(R.id.tvAge)
        tvUsername = findViewById(R.id.tvUsername)
        tvDescription = findViewById(R.id.tvDescription)
        imgProfile = findViewById(R.id.imgProfile)
        
        // Inicialización de botones (Ahora con 'val' para que sean variables locales correctas)
        val btnEdit = findViewById<Button>(R.id.btnEdit)
        val btnSettings = findViewById<Button>(R.id.btnSettings)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnMoodHistory = findViewById<Button>(R.id.btnMoodHistory)
        val btnAchievements = findViewById<Button>(R.id.btnAchievements)

        // Configuración de navegación inferior
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_profile

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, Home::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this, History::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }

        val sharedPreferences = getSharedPreferences("psyconnect", MODE_PRIVATE)
        userId = sharedPreferences.getLong("userId", -1)

        // Eventos de clic
        btnEdit.setOnClickListener {
            startActivity(Intent(this, EditProfile::class.java))
        }

        btnMoodHistory.setOnClickListener {
            startActivity(Intent(this, MoodHistoryActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        btnAchievements.setOnClickListener {
            // Corregido: El nombre de la clase es AchievementsActivity
            startActivity(Intent(this, AchievementsActivity::class.java))
        }

        btnLogout.setOnClickListener {
            sharedPreferences.edit { remove("userId") }
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadProfile()
    }

    private fun calcularEdad(fechaNacimiento: String?): String {
        if (fechaNacimiento.isNullOrBlank()) return "No registrada"
        return try {
            val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val fecha = formato.parse(fechaNacimiento) ?: return "No registrada"
            val nacimiento = Calendar.getInstance().apply { time = fecha }
            val hoy = Calendar.getInstance()
            var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)
            if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) edad--
            "$edad años"
        } catch (e: Exception) {
            "No registrada"
        }
    }

    private fun loadProfile() {
        if (userId == -1L) return
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserById(userId)
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        tvName.text = it.name
                        tvUsernameHeader.text = "@${it.username}"
                        tvWelcome.text = "Hola ${it.name}, gracias por cuidar de tu bienestar hoy."
                        tvAge.text = "Edad: ${calcularEdad(it.birthdate)}"
                        tvUsername.text = "Usuario: ${it.username}"
                        
                        tvDescription.text = it.description.takeIf { d -> d.isNotBlank() } 
                            ?: "Aún no has agregado una descripción."

                        if (!it.profileImage.isNullOrBlank()) {
                            Glide.with(this@Profile)
                                .load(it.profileImage)
                                .placeholder(R.mipmap.ic_launcher_round)
                                .circleCrop()
                                .into(imgProfile)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
