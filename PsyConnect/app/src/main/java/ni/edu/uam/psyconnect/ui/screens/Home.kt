package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.network.RetrofitClient
import ni.edu.uam.psyconnect.ui.adapter.PsychologistAdapter

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val cardTest =
            findViewById<CardView>(
                R.id.cardTest
            )

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

        /*
         * CARGAR PSICÓLOGOS
         */
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
                                "photo",
                                psychologist.photo
                            )

                            startActivity(intent)
                        }
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }

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

        cardTest.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    Test::class.java
                )
            )
        }
    }
}