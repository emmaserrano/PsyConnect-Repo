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
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.Question
import ni.edu.uam.psyconnect.data.repository.TestRepository
import com.google.android.material.button.MaterialButton

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

    // Emojis según categoría
    private val categoryEmojis = mapOf(
        "WELLNESS" to listOf("🌿", "☀️", "💚", "✨"),
        "STRESS"   to listOf("🌊", "😤", "💆", "🧘"),
        "MOOD"     to listOf("☀️", "😊", "🌈", "💫"),
        "SLEEP"    to listOf("🌙", "😴", "⭐", "🛌"),
        "SOCIAL"   to listOf("🤝", "💬", "👥", "❤️")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_test)

        val category = intent.getStringExtra("category") ?: "WELLNESS"
        questions = TestRepository.getQuestions(category)

        tvCounter       = findViewById(R.id.tvQuestionCounter)
        tvQuestion      = findViewById(R.id.tvQuestionText)
        tvEmoji         = findViewById(R.id.tvEmoji)
        progressBar     = findViewById(R.id.progressBar)
        optionsContainer = findViewById(R.id.optionsContainer)
        btnNext         = findViewById(R.id.btnNext)

        val emojis = categoryEmojis[category] ?: List(questions.size) { "💭" }

        showQuestion(emojis)

        btnNext.setOnClickListener {
            if (selectedOptionIndex >= 0) {
                // Guardar respuesta (1-indexed para el score)
                answers.add(selectedOptionIndex + 1)
                selectedOptionIndex = -1

                if (currentIndex + 1 < questions.size) {
                    currentIndex++
                    showQuestion(emojis)
                } else {
                    // Finalizar test
                    val score = answers.sum()
                    val intent = Intent(this, Results::class.java)
                    intent.putExtra("score", score)
                    intent.putExtra("category", category)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun showQuestion(emojis: List<String>) {
        val question = questions[currentIndex]
        val total = questions.size
        val progress = ((currentIndex + 1) * 100) / total

        // Actualizar counter
        tvCounter.text = "Pregunta ${currentIndex + 1} de $total"

        // Animar barra de progreso
        val animator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, progress)
        animator.duration = 400
        animator.start()

        // Emoji de la pregunta
        tvEmoji.text = if (currentIndex < emojis.size) emojis[currentIndex] else "💭"

        // Texto con animación fade-in
        tvQuestion.alpha = 0f
        tvQuestion.text = question.text
        tvQuestion.animate().alpha(1f).setDuration(300).start()

        // Botón: "Siguiente" o "Ver resultados"
        btnNext.text = if (currentIndex + 1 < total) "Siguiente →" else "Ver resultados ✓"
        btnNext.isEnabled = false

        // Resetear selección
        selectedOptionIndex = -1

        // Limpiar y construir opciones
        optionsContainer.removeAllViews()

        question.options.forEachIndexed { index, optionText ->
            val optionView = buildOptionView(optionText, index)
            optionsContainer.addView(optionView)
        }
    }

    private fun buildOptionView(text: String, index: Int): MaterialButton {
        val btn = MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle)

        btn.text = text
        btn.textSize = 14f
        btn.gravity = Gravity.CENTER
        btn.setTextColor(Color.parseColor("#374151"))
        btn.cornerRadius = 36  // dp → px se aplica internamente en MaterialButton
        btn.strokeWidth = 4
        btn.strokeColor = ContextCompat.getColorStateList(this, R.color.turquesa_claro)
        btn.setBackgroundColor(Color.parseColor("#F8FAFA"))
        btn.setPadding(0, 28, 0, 28)
        btn.isAllCaps = false

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = 16
        btn.layoutParams = params

        btn.setOnClickListener {
            selectedOptionIndex = index
            updateOptionSelection(index)
            btnNext.isEnabled = true
        }

        return btn
    }

    private fun updateOptionSelection(selectedIndex: Int) {
        val turquesa = ContextCompat.getColor(this, R.color.turquesa_principal)
        val turquesaClaro = ContextCompat.getColor(this, R.color.turquesa_claro)
        val grisClaro = Color.parseColor("#F8FAFA")
        val grisBorde = ContextCompat.getColorStateList(this, R.color.turquesa_claro)
        val grisTexto = Color.parseColor("#374151")
        val blancoColor = Color.WHITE

        for (i in 0 until optionsContainer.childCount) {
            val btn = optionsContainer.getChildAt(i) as? MaterialButton ?: continue

            if (i == selectedIndex) {
                // Seleccionado: fondo turquesa
                btn.setBackgroundColor(turquesa)
                btn.setTextColor(blancoColor)
                btn.strokeColor = ContextCompat.getColorStateList(this, R.color.turquesa_oscuro)
                // Pequeña animación de escala
                btn.animate().scaleX(1.02f).scaleY(1.02f).setDuration(150).start()
            } else {
                // No seleccionado: restaurar
                btn.setBackgroundColor(grisClaro)
                btn.setTextColor(grisTexto)
                btn.strokeColor = grisBorde
                btn.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
            }
        }
    }
}