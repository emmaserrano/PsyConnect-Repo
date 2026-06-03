package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.TestResult
import ni.edu.uam.psyconnect.network.RetrofitClient
import kotlin.math.abs

class Results : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_results)

        val btnProgress =
            findViewById<Button>(
                R.id.btnProgress
            )

        btnProgress.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    History::class.java
                )
            )
        }

        val percentageText =
            findViewById<TextView>(
                R.id.tvPercentage
            )

        val levelText =
            findViewById<TextView>(
                R.id.tvLevel
            )

        val recommendationsText =
            findViewById<TextView>(
                R.id.tvRecommendations
            )

        val progressMessage =
            findViewById<TextView>(
                R.id.tvProgressMessage
            )

        // Obtener porcentaje enviado desde Test
        val percentage =
            intent.getIntExtra(
                "percentage",
                0
            )

        // Mostrar porcentaje
        percentageText.text = "$percentage%"

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

        levelText.text = "Nivel: $level"

        recommendationsText.text = recommendations

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

                    val historyResponse =
                        RetrofitClient
                            .apiService
                            .getHistory(userId)

                    if (historyResponse.isSuccessful) {

                        val history =
                            historyResponse.body()
                                ?: emptyList()

                        if (history.isNotEmpty()) {

                            val previous =
                                history.first()

                            val difference =
                                percentage -
                                        previous.percentage

                            progressMessage.text =
                                when {

                                    difference < 0 ->
                                        "Excelente. Tu ansiedad disminuyó ${abs(difference)}% respecto al test anterior."

                                    difference > 0 ->
                                        "Tu ansiedad aumentó $difference% respecto al test anterior."

                                    else ->
                                        "Tu resultado es igual al test anterior."
                                }

                        } else {

                            progressMessage.text =
                                "Este es tu primer test registrado."
                        }
                    }

                    RetrofitClient
                        .apiService
                        .saveResult(
                            TestResult(
                                userId = userId,
                                percentage = percentage,
                                level = level
                            )
                        )

                } catch (_: Exception) {
                }
            }
        }
    }
}