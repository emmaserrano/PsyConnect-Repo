package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import ni.edu.uam.psyconnect.R

class TestCategoryActivity : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_test_category
        )

        findViewById<LinearLayout>(
            R.id.cardWellness
        ).setOnClickListener {

            openCategory(
                "WELLNESS"
            )
        }

        findViewById<LinearLayout>(
            R.id.cardStress
        ).setOnClickListener {

            openCategory(
                "STRESS"
            )
        }

        findViewById<LinearLayout>(
            R.id.cardMood
        ).setOnClickListener {

            openCategory(
                "MOOD"
            )
        }

        findViewById<LinearLayout>(
            R.id.cardSleep
        ).setOnClickListener {

            openCategory(
                "SLEEP"
            )
        }

        findViewById<LinearLayout>(
            R.id.cardSocial
        ).setOnClickListener {

            openCategory(
                "SOCIAL"
            )
        }
    }

    private fun openCategory(
        category: String
    ) {

        val intent =
            Intent(
                this,
                DynamicTestActivity::class.java
            )

        intent.putExtra(
            "category",
            category
        )

        startActivity(intent)
    }
}