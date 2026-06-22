package ni.edu.uam.psyconnect.ui.screens

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.network.RetrofitClient
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
            intent.getStringExtra("category")
                ?: "WELLNESS"

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

        val layoutEvolution =
            findViewById<LinearLayout>(
                R.id.layoutEvolution
            )

        val tvBest =
            findViewById<TextView>(
                R.id.tvBest
            )

        val tvAverage =
            findViewById<TextView>(
                R.id.tvAverage
            )

        val tvTests =
            findViewById<TextView>(
                R.id.tvTests
            )

        tvCategory.text =
            traducirCategoria(category)

        tvPercentage.text =
            "$percentage%"

        tvTitle.text =
            feedback.title

        tvDescription.text =
            feedback.description

        tvDate.text =
            "📅 $date"

        tvRecommendations.text =
            feedback.recommendations
                .joinToString("\n\n") {
                    "✔ $it"
                }

        animation.setAnimation(
            feedback.animation
        )

        animation.playAnimation()

        when {

            trend > 0 -> {

                tvTrend.text =
                    "📈 Mejoraste $trend% respecto al test anterior"

                tvTrend.setTextColor(
                    Color.parseColor("#16A34A")
                )
            }

            trend < 0 -> {

                tvTrend.text =
                    "📉 Disminuyó ${abs(trend)}% respecto al test anterior"

                tvTrend.setTextColor(
                    Color.parseColor("#DC2626")
                )
            }

            else -> {

                tvTrend.text =
                    "➖ Sin cambios respecto al test anterior"

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

        cargarEvolucion(
            category,
            layoutEvolution,
            tvBest,
            tvAverage,
            tvTests
        )

        btnBack.setOnClickListener {

            finish()
        }
    }

    private fun cargarEvolucion(
        category: String,
        container: LinearLayout,
        tvBest: TextView,
        tvAverage: TextView,
        tvTests: TextView
    ) {

        lifecycleScope.launch {

            try {

                val userId =
                    getSharedPreferences(
                        "psyconnect",
                        MODE_PRIVATE
                    ).getLong(
                        "userId",
                        1L
                    )

                val response =
                    RetrofitClient
                        .apiService
                        .getHistory(userId)

                if (!response.isSuccessful)
                    return@launch

                val results =
                    response.body()
                        ?.filter { result ->
                            result.category == category
                        }
                        ?.sortedBy { result ->
                            result.id
                        }
                        ?: emptyList()

                tvTests.text =
                    results.size.toString()

                if (results.isNotEmpty()) {

                    tvBest.text =
                        "${results.maxOf { it.percentage }}%"

                    tvAverage.text =
                        "${results.map { it.percentage }.average().toInt()}%"
                }

                container.removeAllViews()

                results.forEach { result ->

                    val card =
                        CardView(
                            this@ResultDetailActivity
                        )

                    card.radius = 40f

                    val params =
                        LinearLayout.LayoutParams(
                            180,
                            180
                        )

                    params.marginEnd = 16

                    card.layoutParams =
                        params

                    val text =
                        TextView(
                            this@ResultDetailActivity
                        )

                    text.text =
                        "${result.percentage}%"

                    text.textSize = 22f

                    text.setPadding(
                        20,
                        50,
                        20,
                        20
                    )

                    card.addView(text)

                    container.addView(card)
                }
            }

            catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    private fun traducirCategoria(
        category: String
    ): String {

        return when (category) {

            "WELLNESS" ->
                "🌿 Bienestar"

            "STRESS" ->
                "😌 Manejo del estrés"

            "SLEEP" ->
                "😴 Sueño"

            "MOOD" ->
                "😊 Estado de ánimo"

            "SELF_ESTEEM" ->
                "💜 Autoestima"

            "RELATIONSHIPS" ->
                "🤝 Relaciones"

            else ->
                "📊 General"
        }
    }
}