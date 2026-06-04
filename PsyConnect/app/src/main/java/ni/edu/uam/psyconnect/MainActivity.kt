package ni.edu.uam.psyconnect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ni.edu.uam.psyconnect.ui.screens.Home
import ni.edu.uam.psyconnect.ui.screens.Login

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences =
            getSharedPreferences(
                "psyconnect",
                MODE_PRIVATE
            )

        val userId =
            sharedPreferences.getLong(
                "userId",
                -1
            )

        val intent =
            if (userId != -1L) {
                Intent(this, Home::class.java)
            } else {
                Intent(this, Login::class.java)
            }

        startActivity(intent)

        finish()
    }
}