package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import ni.edu.uam.psyconnect.data.model.Mood
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.WellnessItem
import ni.edu.uam.psyconnect.network.RetrofitClient
import ni.edu.uam.psyconnect.ui.adapter.PsychologistAdapter
import ni.edu.uam.psyconnect.ui.adapter.WellnessAdapter

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val tvGreeting =
            findViewById<TextView>(
                R.id.tvGreeting
            )

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

                        val user =
                            response.body()

                        tvGreeting.text =
                            "Hola, ${user?.name} 👋"

                        val moodResponse =
                            RetrofitClient
                                .apiService
                                .hasMoodToday(
                                    userId
                                )

                        if (
                            moodResponse.isSuccessful &&
                            moodResponse.body() == false
                        ) {

                            val prefs =
                                getSharedPreferences(
                                    "psyconnect",
                                    MODE_PRIVATE
                                )

                            val hoy =
                                SimpleDateFormat(
                                    "yyyy-MM-dd",
                                    Locale.getDefault()
                                ).format(
                                    Date()
                                )

                            val ultimaFechaMood =
                                prefs.getString(
                                    "ultimaFechaMood",
                                    ""
                                )

                            if (
                                ultimaFechaMood != hoy
                            ) {

                                mostrarDialogoMood(userId)

                                prefs.edit()
                                    .putString(
                                        "ultimaFechaMood",
                                        hoy
                                    )
                                    .apply()
                            }
                        }
                    }

                } catch (_: Exception) {

                    tvGreeting.text =
                        "Hola 👋"
                }
            }
        }
        /*
         * EVALUACIONES
         */
        val recyclerWellness =
            findViewById<RecyclerView>(
                R.id.recyclerWellness
            )

        recyclerWellness.layoutManager =
            LinearLayoutManager(this)

        recyclerWellness.setHasFixedSize(false)

        recyclerWellness.isNestedScrollingEnabled =
            false

        val evaluations =
            listOf(

                WellnessItem(
                    "🌿 Bienestar emocional",
                    "Evalúa tu equilibrio emocional general.",
                    R.raw.wellbeing,
                    "WELLNESS"
                ),

                WellnessItem(
                    "🌊 Estrés",
                    "Conoce tu nivel actual de estrés.",
                    R.raw.stress,
                    "STRESS"
                ),

                WellnessItem(
                    "☀ Estado de ánimo",
                    "Descubre cómo te has sentido últimamente.",
                    R.raw.happy,
                    "MOOD"
                ),

                WellnessItem(
                    "🌙 Sueño y descanso",
                    "Analiza la calidad de tu descanso.",
                    R.raw.sleep,
                    "SLEEP"
                ),

                WellnessItem(
                    "🤝 Relaciones sociales",
                    "Reflexiona sobre tu interacción con otras personas.",
                    R.raw.social,
                    "RELATIONSHIPS"
                )
            )

        recyclerWellness.adapter =
            WellnessAdapter(
                evaluations
            ) { item ->

                val intent =
                    Intent(
                        this,
                        DynamicTestActivity::class.java
                    )

                intent.putExtra(
                    "category",
                    item.category
                )

                startActivity(intent)
            }

        /*
         * PSICÓLOGOS
         */
        val recyclerPsychologists =
            findViewById<RecyclerView>(
                R.id.recyclerPsychologists
            )

        recyclerPsychologists.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient
                        .apiService
                        .getPsychologists()

                if (response.isSuccessful) {

                    val psychologists =
                        response.body() ?: emptyList()

                    recyclerPsychologists.adapter =
                        PsychologistAdapter(
                            psychologists
                        ) { psychologist ->

                            val intent =
                                Intent(
                                    this@Home,
                                    DetailPsychologist::class.java
                                )

                            intent.putExtra(
                                "name",
                                psychologist.name
                            )

                            intent.putExtra(
                                "specialty",
                                psychologist.specialty
                            )

                            intent.putExtra(
                                "city",
                                psychologist.city
                            )

                            intent.putExtra(
                                "email",
                                psychologist.email
                            )

                            intent.putExtra(
                                "description",
                                psychologist.description
                            )

                            intent.putExtra(
                                "phone",
                                psychologist.phone
                            )

                            intent.putExtra(
                                "photo",
                                psychologist.photo
                            )

                            startActivity(intent)
                        }
                }

            } catch (_: Exception) {
            }
        }

        /*
         * BOTTOM NAVIGATION
         */
        val bottomNav =
            findViewById<BottomNavigationView>(
                R.id.bottomNavigation
            )

        bottomNav.selectedItemId =
            R.id.nav_home

        bottomNav.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> true

                R.id.nav_history -> {

                    startActivity(
                        Intent(
                            this,
                            History::class.java
                        )
                    )

                    finish()

                    true
                }

                R.id.nav_profile -> {

                    startActivity(
                        Intent(
                            this,
                            Profile::class.java
                        )
                    )

                    finish()

                    true
                }

                else -> false
            }
        }
    }

    private fun mostrarDialogoMood(userId: Long) {

        val view =
            LayoutInflater.from(this)
                .inflate(
                    R.layout.dialog_mood,
                    null
                )

        val dialog =
            AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create()

        fun guardarMood(mood: String) {

            lifecycleScope.launch {

                try {

                    RetrofitClient
                        .apiService
                        .saveMood(
                            Mood(
                                userId = userId,
                                mood = mood
                            )
                        )

                    Toast.makeText(
                        this@Home,
                        "Estado emocional registrado",
                        Toast.LENGTH_LONG
                    ).show()

                    dialog.dismiss()

                } catch (_: Exception) {

                    Toast.makeText(
                        this@Home,
                        "No se pudo guardar",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        view.findViewById<Button>(
            R.id.btnExcellent
        ).setOnClickListener {

            guardarMood("EXCELENTE")
        }

        view.findViewById<Button>(
            R.id.btnGood
        ).setOnClickListener {

            guardarMood("BIEN")
        }

        view.findViewById<Button>(
            R.id.btnNormal
        ).setOnClickListener {

            guardarMood("NORMAL")
        }

        view.findViewById<Button>(
            R.id.btnSad
        ).setOnClickListener {

            guardarMood("TRISTE")
        }

        view.findViewById<Button>(
            R.id.btnVeryBad
        ).setOnClickListener {

            guardarMood("MUY_MAL")
        }

        dialog.show()
    }
}