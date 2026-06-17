package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.EmotionalTest
import ni.edu.uam.psyconnect.ui.adapter.EmotionalTestAdapter

class WellnessCenter : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_wellness_center
        )

        val recycler =
            findViewById<RecyclerView>(
                R.id.recyclerTests
            )

        recycler.layoutManager =
            LinearLayoutManager(this)

        val tests = listOf(

            EmotionalTest(
                "🌿 Bienestar emocional",
                "Explora cómo te has sentido últimamente.",
                "5 min",
                "Fácil",
                R.raw.mood
            ),

            EmotionalTest(
                "🌊 Estrés",
                "Identifica señales de sobrecarga emocional.",
                "4 min",
                "Fácil",
                R.raw.stress
            ),

            EmotionalTest(
                "🌙 Sueño y descanso",
                "Evalúa la calidad de tu descanso.",
                "3 min",
                "Fácil",
                R.raw.sleep
            ),

            EmotionalTest(
                "🤝 Relaciones sociales",
                "Reflexiona sobre tus vínculos y apoyo social.",
                "5 min",
                "Media",
                R.raw.social
            ),

            EmotionalTest(
                "❤️ Autoestima",
                "Conoce cómo te percibes y valoras.",
                "6 min",
                "Media",
                R.raw.selfesteem
            ),

            EmotionalTest(
                "😊 Emociones positivas",
                "Fortalece hábitos emocionales saludables.",
                "4 min",
                "Fácil",
                R.raw.gratitude
            )
        )

        recycler.adapter =
            EmotionalTestAdapter(
                tests
            ) {

                startActivity(

                    Intent(
                        this,
                        Test::class.java
                    )
                )
            }
    }
}