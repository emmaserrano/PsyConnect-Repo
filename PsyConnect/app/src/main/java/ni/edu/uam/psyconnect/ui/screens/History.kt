package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.network.RetrofitClient

class History : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history)

        val container =
            findViewById<LinearLayout>(
                R.id.containerHistory
            )

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

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient
                        .apiService
                        .getHistory(userId)

                if (response.isSuccessful) {

                    val results =
                        response.body()

                    results?.forEach { result ->

                        val textView =
                            TextView(this@History)

                        textView.text =
                            """
                            Fecha: ${result.createdAt}
                            Resultado: ${result.percentage}%
                            Nivel: ${result.level}
                            
                            """.trimIndent()

                        textView.textSize = 18f

                        container.addView(textView)
                    }
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@History,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}