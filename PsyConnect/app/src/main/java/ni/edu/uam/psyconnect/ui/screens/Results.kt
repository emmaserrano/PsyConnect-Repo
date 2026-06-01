package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ni.edu.uam.psyconnect.R

class Results : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_results)

        val percentageText = findViewById<TextView>(R.id.tvPercentage)
        val levelText = findViewById<TextView>(R.id.tvLevel)
        val recommendationsText = findViewById<TextView>(R.id.tvRecommendations)

        // Obtener porcentaje enviado desde Test
        val percentage = intent.getIntExtra("percentage", 0)

        // Mostrar porcentaje
        percentageText.text = "$percentage%"

        // Determinar nivel
        val level: String
        val recommendations: String

        when {

            percentage <= 30 -> {

                level = "Ansiedad Leve"

                recommendations =
                    """
                    • Dormir al menos 8 horas
                    • Mantener una rutina saludable
                    • Escuchar música relajante
                    """.trimIndent()
            }

            percentage <= 60 -> {

                level = "Ansiedad Moderada"

                recommendations =
                    """
                    • Practicar respiración profunda
                    • Realizar meditación 5 minutos
                    • Llevar una bitácora emocional
                    • Hacer ejercicio ligero
                    """.trimIndent()
            }

            else -> {

                level = "Ansiedad Alta"

                recommendations =
                    """
                    • Buscar apoyo emocional
                    • Practicar mindfulness
                    • Reducir situaciones de estrés
                    • Considerar visitar un psicólogo
                    """.trimIndent()
            }
        }

        // Mostrar resultados
        levelText.text = "Nivel: $level"

        recommendationsText.text = recommendations
    }
}
