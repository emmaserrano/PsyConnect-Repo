package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.ui.helper.EmotionalFeedbackGenerator

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

        val score =
            intent.getIntExtra(
                "score",
                0
            )

        tvScore.text =
            "Puntaje obtenido: $score"

        val feedback =
            EmotionalFeedbackGenerator.generate(
                score
            )

        tvTitle.text =
            feedback.title

        tvDescription.text =
            feedback.description

        tvRecommendations.text =
            feedback.recommendations.joinToString(
                separator = "\n\n"
            ) {
                "✔ $it"
            }

        animation.setAnimation(
            feedback.animation
        )

        animation.playAnimation()

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
}