package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.repository.TestRepository

class Test : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_test
        )

        val category =
            intent.getStringExtra(
                "category"
            ) ?: "WELLNESS"

        val tvCategoryTitle =
            findViewById<TextView>(
                R.id.tvCategoryTitle
            )

        val questionsContainer =
            findViewById<LinearLayout>(
                R.id.questionsContainer
            )

        val btnFinish =
            findViewById<MaterialButton>(
                R.id.btnFinish
            )

        tvCategoryTitle.text =
            when(category){

                "WELLNESS" ->
                    "🌿 Bienestar emocional"

                "STRESS" ->
                    "🌊 Estrés"

                "MOOD" ->
                    "☀ Estado de ánimo"

                "SLEEP" ->
                    "🌙 Sueño y descanso"

                else ->
                    "🤝 Relaciones sociales"
            }

        val questions =
            TestRepository.getQuestions(
                category
            )

        val groups =
            mutableListOf<RadioGroup>()

        questions.forEach { question ->

            val title =
                TextView(this)

            title.text =
                question.text

            title.textSize = 18f

            title.setPadding(
                0,
                24,
                0,
                12
            )

            questionsContainer.addView(
                title
            )

            val radioGroup =
                RadioGroup(this)

            question.options.forEachIndexed { index, option ->

                val radioButton =
                    RadioButton(this)

                radioButton.text =
                    option

                radioButton.id =
                    index + 1

                radioGroup.addView(
                    radioButton
                )
            }

            groups.add(
                radioGroup
            )

            questionsContainer.addView(
                radioGroup
            )
        }

        btnFinish.setOnClickListener {

            var score = 0

            groups.forEach { group ->

                if (
                    group.checkedRadioButtonId != -1
                ) {

                    score +=
                        group.checkedRadioButtonId
                }
            }

            val intent =
                Intent(
                    this,
                    Results::class.java
                )

            intent.putExtra(
                "score",
                score
            )

            intent.putExtra(
                "category",
                category
            )

            startActivity(
                intent
            )
        }
    }
}