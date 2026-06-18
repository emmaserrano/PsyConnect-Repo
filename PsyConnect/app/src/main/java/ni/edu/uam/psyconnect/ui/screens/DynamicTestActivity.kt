package ni.edu.uam.psyconnect.ui.screens

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.Question
import ni.edu.uam.psyconnect.data.repository.TestRepository

class DynamicTestActivity : AppCompatActivity() {

    private var currentIndex = 0

    private var selectedOptionIndex = -1

    private val answers = mutableListOf<Int>()

    private lateinit var questions: List<Question>

    private lateinit var tvCounter: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var tvEmoji: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var optionsContainer: LinearLayout
    private lateinit var btnNext: MaterialButton

    private val categoryEmojis = mapOf(

        "WELLNESS" to listOf(
            "🌿",
            "☀️",
            "💚",
            "✨"
        ),

        "STRESS" to listOf(
            "🌊",
            "😌",
            "💆",
            "🧘"
        ),

        "SLEEP" to listOf(
            "🌙",
            "😴",
            "⭐",
            "🛌"
        ),

        "SELF_ESTEEM" to listOf(
            "💜",
            "🌷",
            "✨",
            "💖"
        ),

        "RELATIONSHIPS" to listOf(
            "🤝",
            "💬",
            "❤️",
            "👥"
        ),

        "MOOD" to listOf(
            "☀️",
            "😊",
            "🌈",
            "💫"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_dynamic_test
        )

        val category =
            intent.getStringExtra(
                "category"
            ) ?: "WELLNESS"

        questions =
            TestRepository.getQuestions(
                category
            )

        tvCounter =
            findViewById(
                R.id.tvQuestionCounter
            )

        tvQuestion =
            findViewById(
                R.id.tvQuestionText
            )

        tvEmoji =
            findViewById(
                R.id.tvEmoji
            )

        progressBar =
            findViewById(
                R.id.progressBar
            )

        optionsContainer =
            findViewById(
                R.id.optionsContainer
            )

        btnNext =
            findViewById(
                R.id.btnNext
            )

        val emojis =
            categoryEmojis[category]
                ?: List(questions.size) {
                    "💭"
                }

        showQuestion(emojis)

        btnNext.setOnClickListener {

            if (selectedOptionIndex >= 0) {

                // Siempre = 5
                // Frecuentemente = 4
                // A veces = 3
                // Rara vez = 2
                // Nunca = 1

                val value =
                    5 - selectedOptionIndex

                answers.add(
                    value
                )

                selectedOptionIndex = -1

                if (currentIndex + 1 < questions.size) {

                    currentIndex++

                    showQuestion(
                        emojis
                    )

                } else {

                    val score =
                        answers.sum()

                    val maxScore =
                        questions.size * 5

                    val percentage =
                        (score * 100) / maxScore

                    val intent =
                        Intent(
                            this,
                            Results::class.java
                        )

                    intent.putExtra(
                        "percentage",
                        percentage
                    )

                    intent.putExtra(
                        "category",
                        category
                    )

                    startActivity(
                        intent
                    )

                    finish()
                }
            }
        }
    }

    private fun showQuestion(
        emojis: List<String>
    ) {

        val question =
            questions[currentIndex]

        val total =
            questions.size

        val progress =
            ((currentIndex + 1) * 100) / total

        tvCounter.text =
            "Pregunta ${currentIndex + 1} de $total"

        val animator =
            ObjectAnimator.ofInt(
                progressBar,
                "progress",
                progressBar.progress,
                progress
            )

        animator.duration = 400

        animator.start()

        tvEmoji.text =
            if (currentIndex < emojis.size)
                emojis[currentIndex]
            else
                "💭"

        tvQuestion.alpha = 0f

        tvQuestion.text =
            question.text

        tvQuestion.animate()
            .alpha(1f)
            .setDuration(300)
            .start()

        btnNext.text =
            if (currentIndex + 1 < total)
                "Siguiente →"
            else
                "Ver resultados ✓"

        btnNext.isEnabled = false

        selectedOptionIndex = -1

        optionsContainer.removeAllViews()

        question.options.forEachIndexed { index, optionText ->

            val optionView =
                buildOptionView(
                    optionText,
                    index
                )

            optionsContainer.addView(
                optionView
            )
        }
    }

    private fun buildOptionView(
        text: String,
        index: Int
    ): MaterialButton {

        val btn =
            MaterialButton(
                this,
                null,
                com.google.android.material.R.attr.materialButtonOutlinedStyle
            )

        btn.text = text

        btn.textSize = 14f

        btn.gravity =
            Gravity.CENTER

        btn.setTextColor(
            Color.parseColor(
                "#374151"
            )
        )

        btn.cornerRadius = 36

        btn.strokeWidth = 4

        btn.strokeColor =
            ContextCompat.getColorStateList(
                this,
                R.color.turquesa_claro
            )

        btn.setBackgroundColor(
            Color.parseColor(
                "#F8FAFA"
            )
        )

        btn.setPadding(
            0,
            28,
            0,
            28
        )

        btn.isAllCaps = false

        val params =
            LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                LinearLayout.LayoutParams.WRAP_CONTENT
            )

        params.bottomMargin = 16

        btn.layoutParams =
            params

        btn.setOnClickListener {

            selectedOptionIndex =
                index

            updateOptionSelection(
                index
            )

            btnNext.isEnabled = true
        }

        return btn
    }

    private fun updateOptionSelection(
        selectedIndex: Int
    ) {

        val turquesa =
            ContextCompat.getColor(
                this,
                R.color.turquesa_principal
            )

        val grisClaro =
            Color.parseColor(
                "#F8FAFA"
            )

        val grisTexto =
            Color.parseColor(
                "#374151"
            )

        val blanco =
            Color.WHITE

        for (i in 0 until optionsContainer.childCount) {

            val btn =
                optionsContainer.getChildAt(i)
                        as MaterialButton

            if (i == selectedIndex) {

                btn.setBackgroundColor(
                    turquesa
                )

                btn.setTextColor(
                    blanco
                )

                btn.animate()
                    .scaleX(1.02f)
                    .scaleY(1.02f)
                    .setDuration(150)
                    .start()

            } else {

                btn.setBackgroundColor(
                    grisClaro
                )

                btn.setTextColor(
                    grisTexto
                )

                btn.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(150)
                    .start()
            }
        }
    }
}