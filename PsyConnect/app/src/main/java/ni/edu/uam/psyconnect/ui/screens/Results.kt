package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.TestResult
import ni.edu.uam.psyconnect.network.RetrofitClient
import ni.edu.uam.psyconnect.ui.helper.TestInterpreter

class Results : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_results
        )

        val tvScore =
            findViewById<TextView>(
                R.id.tvScore
            )

        val tvTitle =
            findViewById<TextView>(
                R.id.tvTitle
            )

        val tvDescription =
            findViewById<TextView>(
                R.id.tvDescription
            )

        val tvRecommendations =
            findViewById<TextView>(
                R.id.tvRecommendations
            )

        val animation =
            findViewById<LottieAnimationView>(
                R.id.lottieResult
            )

        val btnHistory =
            findViewById<Button>(
                R.id.btnHistory
            )

        val btnHome =
            findViewById<Button>(
                R.id.btnHome
            )

        val percentage =
            intent.getIntExtra(
                "score",
                0
            )

        val category =
            intent.getStringExtra(
                "category"
            ) ?: "WELLNESS"

        val feedback =
            TestInterpreter.generate(
                category,
                percentage
            )

        tvScore.text =
            "Resultado obtenido: $percentage %"

        tvTitle.text =
            feedback.title

        tvDescription.text =
            feedback.description

        tvRecommendations.text =
            feedback.recommendations.joinToString(
                "\n\n"
            ) {
                "✔ $it"
            }

        animation.setAnimation(
            feedback.animation
        )

        animation.playAnimation()

        guardarResultado(
            category,
            percentage,
            feedback.title
        )

        btnHistory.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    History::class.java
                )
            )
        }

        btnHome.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    Home::class.java
                )
            )

            finish()
        }
    }

    private fun guardarResultado(

        category: String,
        percentage: Int,
        level: String

    ) {

        val sharedPreferences =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
            )

        val userId =
            sharedPreferences.getLong(
                "userId",
                1L
            )

        val result =
            TestResult(

                userId = userId,

                category = category,

                percentage = percentage,

                level = level
            )

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient
                        .apiService
                        .saveResult(
                            result
                        )

                if (
                    response.isSuccessful
                ) {

                    println(
                        "Resultado guardado correctamente"
                    )

                } else {

                    println(
                        "Error al guardar resultado"
                    )
                }

            } catch (
                e: Exception
            ) {

                e.printStackTrace()
            }
        }
    }
}