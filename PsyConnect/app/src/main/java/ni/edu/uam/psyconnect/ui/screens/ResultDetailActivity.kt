package ni.edu.uam.psyconnect.ui.screens

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.ui.helper.TestInterpreter
import kotlin.math.abs

class ResultDetailActivity : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_result_detail
        )

        val category =
            intent.getStringExtra(
                "category"
            ) ?: "WELLNESS"

        val percentage =
            intent.getIntExtra(
                "percentage",
                0
            )

        val trend =
            intent.getIntExtra(
                "trend",
                0
            )

        val date =
            intent.getStringExtra(
                "date"
            ) ?: "-"

        val feedback =
            TestInterpreter.generate(
                category,
                percentage
            )

        val tvCategory =
            findViewById<TextView>(
                R.id.tvCategory
            )

        val tvPercentage =
            findViewById<TextView>(
                R.id.tvPercentage
            )

        val tvTitle =
            findViewById<TextView>(
                R.id.tvTitle
            )

        val tvDescription =
            findViewById<TextView>(
                R.id.tvDescription
            )

        val tvTrend =
            findViewById<TextView>(
                R.id.tvTrend
            )

        val tvDate =
            findViewById<TextView>(
                R.id.tvDate
            )

        val tvRecommendations =
            findViewById<TextView>(
                R.id.tvRecommendations
            )

        val animation =
            findViewById<LottieAnimationView>(
                R.id.lottieResult
            )

        val btnBack =
            findViewById<MaterialButton>(
                R.id.btnBack
            )

        tvCategory.text =
            traducirCategoria(
                category
            )

        tvPercentage.text =
            "$percentage%"

        tvTitle.text =
            feedback.title

        tvDescription.text =
            feedback.description

        tvDate.text =
            date

        tvRecommendations.text =
            feedback.recommendations
                .joinToString("\n\n") {
                    "• $it"
                }

        animation.setAnimation(
            feedback.animation
        )

        animation.playAnimation()

        when {

            trend > 0 -> {

                tvTrend.text =
                    "Mejoraste $trend% respecto a tu evaluación anterior"

                tvTrend.setTextColor(
                    Color.parseColor("#16A34A")
                )
            }

            trend < 0 -> {

                tvTrend.text =
                    "Disminuyó ${abs(trend)}% respecto a tu evaluación anterior"

                tvTrend.setTextColor(
                    Color.parseColor("#DC2626")
                )
            }

            else -> {

                tvTrend.text =
                    "Sin cambios respecto a tu evaluación anterior"

                tvTrend.setTextColor(
                    Color.parseColor("#6B7280")
                )
            }
        }

        val color = when {

            percentage >= 80 ->
                "#16A34A"

            percentage >= 60 ->
                "#CA8A04"

            percentage >= 40 ->
                "#EA580C"

            else ->
                "#DC2626"
        }

        tvPercentage.setTextColor(
            Color.parseColor(color)
        )

        tvTitle.setTextColor(
            Color.parseColor(color)
        )

        btnBack.setOnClickListener {

            finish()
        }
    }

    private fun traducirCategoria(
        category: String
    ): String {

        return when (category) {

            "WELLNESS" ->
                "Bienestar emocional"

            "STRESS" ->
                "Manejo del estrés"

            "SLEEP" ->
                "Sueño"

            "MOOD" ->
                "Estado de ánimo"

            "SELF_ESTEEM" ->
                "Autoestima"

            "RELATIONSHIPS" ->
                "Relaciones"

            else ->
                "Bienestar"
        }
    }
}