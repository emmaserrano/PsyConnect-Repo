package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.repository.TestRepository

class DynamicTestActivity : AppCompatActivity() {

    private var score = 0

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_dynamic_test
        )

        val category =
            intent.getStringExtra(
                "category"
            ) ?: "WELLNESS"

        val container =
            findViewById<LinearLayout>(
                R.id.questionsContainer
            )

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

            container.addView(title)

            val radioGroup =
                RadioGroup(this)

            question.options.forEachIndexed { index, option ->

                val radio =
                    RadioButton(this)

                radio.text = option

                radio.id = index

                radioGroup.addView(radio)
            }

            groups.add(radioGroup)

            container.addView(radioGroup)
        }

        findViewById<Button>(
            R.id.btnFinish
        ).setOnClickListener {

            score = 0

            groups.forEach {

                score +=
                    it.indexOfChild(
                        it.findViewById(
                            it.checkedRadioButtonId
                        )
                    ) + 1
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

            startActivity(intent)
        }
    }
}