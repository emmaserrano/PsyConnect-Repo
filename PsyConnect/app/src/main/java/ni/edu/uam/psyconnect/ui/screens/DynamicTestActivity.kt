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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.Question
import ni.edu.uam.psyconnect.data.model.TestResultEntity
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalDatabase
import ni.edu.uam.psyconnect.data.moodjournal.TestResultRepository
import ni.edu.uam.psyconnect.data.repository.TestRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    private lateinit var testResultRepository: TestResultRepository

    private val categoryEmojis = mapOf(
        "WELLNESS" to listOf("🌿", "☀️", "💚", "✨"),
        "STRESS" to listOf("🌊", "😌", "💆", "🧘"),
        "SLEEP" to listOf("🌙", "😴", "⭐", "🛌"),
        "SELF_ESTEEM" to listOf("💜", "🌷", "✨", "💖"),
        "RELATIONSHIPS" to listOf("🤝", "💬", "❤️", "👥"),
        "MOOD" to listOf("☀️", "😊", "🌈", "💫")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_test)

        val database = MoodJournalDatabase.getDatabase(this)
        testResultRepository = TestResultRepository(database.testResultDao())

        val category = intent.getStringExtra("category") ?: "WELLNESS"
        questions = TestRepository.getQuestions(category)

        tvCounter = findViewById(R.id.tvQuestionCounter)
        tvQuestion = findViewById(R.id.tvQuestionText)
        tvEmoji = findViewById(R.id.tvEmoji)
        progressBar = findViewById(R.id.progressBar)
        optionsContainer = findViewById(R.id.optionsContainer)
        btnNext = findViewById(R.id.btnNext)

        val emojis = categoryEmojis[category] ?: List(questions.size) { "💭" }

        showQuestion(emojis)

        btnNext.setOnClickListener {
            if (selectedOptionIndex >= 0) {
                val value = 5 - selectedOptionIndex
                answers.add(value)
                selectedOptionIndex = -1

                if (currentIndex + 1 < questions.size) {
                    currentIndex++
                    showQuestion(emojis)
                } else {
                    finalizarTest(category)
                }
            }
        }
    }

    private fun finalizarTest(category: String) {
        val score = answers.sum()
        val maxScore = questions.size * 5
        val percentage = ((score.toFloat() / maxScore.toFloat()) * 100).toInt().coerceIn(0, 100)

        val userId = getSharedPreferences("psyconnect", MODE_PRIVATE).getLong("userId", 1L)
        val createdAt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        
        val level = when {
            percentage >= 80 -> "🌿 Saludable"
            percentage >= 60 -> "✨ Estable"
            else -> "💜 Necesita atención"
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val resultEntity = TestResultEntity(
                category = category,
                percentage = percentage,
                createdAt = createdAt,
                level = level,
                userId = userId
            )
            testResultRepository.insert(resultEntity)
            
            launch(Dispatchers.Main) {
                val intent = Intent(this@DynamicTestActivity, Results::class.java).apply {
                    putExtra("category", category)
                    putExtra("score", score)
                    putExtra("maxScore", maxScore)
                    putExtra("percentage", percentage)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showQuestion(emojis: List<String>) {
        val question = questions[currentIndex]
        val total = questions.size
        val progress = ((currentIndex + 1) * 100) / total

        tvCounter.text = "Pregunta ${currentIndex + 1} de $total"

        ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, progress).apply {
            duration = 400
            start()
        }

        tvEmoji.text = if (currentIndex < emojis.size) emojis[currentIndex] else "💭"

        tvQuestion.alpha = 0f
        tvQuestion.text = question.text
        tvQuestion.animate().alpha(1f).setDuration(300).start()

        btnNext.text = if (currentIndex + 1 < total) "Siguiente →" else "Ver resultados ✓"
        btnNext.isEnabled = false
        selectedOptionIndex = -1

        optionsContainer.removeAllViews()
        question.options.forEachIndexed { index, optionText ->
            optionsContainer.addView(buildOptionView(optionText, index))
        }
    }

    private fun buildOptionView(text: String, index: Int): MaterialButton {
        val btn = MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle).apply {
            this.text = text
            textSize = 14f
            gravity = Gravity.CENTER
            setTextColor(Color.parseColor("#374151"))
            cornerRadius = 36
            strokeWidth = 4
            strokeColor = ContextCompat.getColorStateList(this@DynamicTestActivity, R.color.turquesa_claro)
            setBackgroundColor(Color.parseColor("#F8FAFA"))
            setPadding(0, 28, 0, 28)
            isAllCaps = false
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = 16 }
            
            setOnClickListener {
                selectedOptionIndex = index
                updateOptionSelection(index)
                btnNext.isEnabled = true
            }
        }
        return btn
    }

    private fun updateOptionSelection(selectedIndex: Int) {
        val turquesa = ContextCompat.getColor(this, R.color.turquesa_principal)
        val grisClaro = Color.parseColor("#F8FAFA")
        val grisTexto = Color.parseColor("#374151")
        val blanco = Color.WHITE

        for (i in 0 until optionsContainer.childCount) {
            val btn = optionsContainer.getChildAt(i) as MaterialButton
            if (i == selectedIndex) {
                btn.setBackgroundColor(turquesa)
                btn.setTextColor(blanco)
                btn.animate().scaleX(1.02f).scaleY(1.02f).setDuration(150).start()
            } else {
                btn.setBackgroundColor(grisClaro)
                btn.setTextColor(grisTexto)
                btn.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
            }
        }
    }
}
