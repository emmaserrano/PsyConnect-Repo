package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import ni.edu.uam.psyconnect.R

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboarding)

        viewPager =
            findViewById(R.id.viewPager)

        val btnNext =
            findViewById<MaterialButton>(
                R.id.btnNext
            )

        val adapter =
            OnboardingAdapter(this)

        viewPager.adapter =
            adapter

        btnNext.setOnClickListener {

            if (viewPager.currentItem < 2) {

                viewPager.currentItem += 1

            } else {

                startActivity(
                    Intent(
                        this,
                        Login::class.java
                    )
                )

                finish()
            }
        }
    }
}