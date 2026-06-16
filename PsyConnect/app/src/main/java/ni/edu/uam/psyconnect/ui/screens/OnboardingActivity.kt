package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import ni.edu.uam.psyconnect.R

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val prefs: SharedPreferences =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
            )

        if (
            prefs.getBoolean(
                "onboarding_completed",
                false
            )
        ) {

            startActivity(
                Intent(
                    this,
                    Login::class.java
                )
            )

            finish()

            return
        }

        setContentView(
            R.layout.activity_onboarding
        )

        viewPager =
            findViewById(
                R.id.viewPager
            )

        val btnNext =
            findViewById<MaterialButton>(
                R.id.btnNext
            )

        val adapter =
            OnboardingAdapter(this)

        viewPager.adapter =
            adapter

        btnNext.setOnClickListener {

            if (
                viewPager.currentItem < 2
            ) {

                viewPager.currentItem += 1

            } else {

                prefs.edit()
                    .putBoolean(
                        "onboarding_completed",
                        true
                    )
                    .apply()

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