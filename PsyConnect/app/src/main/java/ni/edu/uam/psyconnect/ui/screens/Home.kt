package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
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

        val username =
            sharedPreferences.getString(
                "username",
                "Usuario"
            )

        tvGreeting.text =
            "Hola, $username 👋"

        /*
         * EVALUACIONES
         */
        val recyclerWellness =
            findViewById<RecyclerView>(
                R.id.recyclerWellness
            )

        recyclerWellness.layoutManager =
            LinearLayoutManager(this)

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
                    R.raw.mood,
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
                    "SOCIAL"
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
}