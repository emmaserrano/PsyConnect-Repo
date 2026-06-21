package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import ni.edu.uam.psyconnect.R

class SecurityActivity : AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_security
        )

        val cardPassword =
            findViewById<LinearLayout>(
                R.id.cardPassword
            )

        val cardEmail =
            findViewById<LinearLayout>(
                R.id.cardEmail
            )

        cardPassword.setOnClickListener {

            startActivity(

                Intent(
                    this,
                    ChangePassword::class.java
                )
            )
        }

        cardEmail.setOnClickListener {

            startActivity(

                Intent(
                    this,
                    ChangeEmail::class.java
                )
            )
        }
    }
}