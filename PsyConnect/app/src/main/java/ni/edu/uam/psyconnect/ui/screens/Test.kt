package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ni.edu.uam.psyconnect.R

class Test : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_test)

        // Botón resultados
        val btnResults = findViewById<Button>(R.id.btnResults)

        // SeekBars
        val seek1 = findViewById<SeekBar>(R.id.seekQuestion1)
        val seek2 = findViewById<SeekBar>(R.id.seekQuestion2)
        val seek3 = findViewById<SeekBar>(R.id.seekQuestion3)
        val seek4 = findViewById<SeekBar>(R.id.seekQuestion4)

        // TextViews de valores
        val value1 = findViewById<TextView>(R.id.tvValue1)
        val value2 = findViewById<TextView>(R.id.tvValue2)
        val value3 = findViewById<TextView>(R.id.tvValue3)
        val value4 = findViewById<TextView>(R.id.tvValue4)

        // Función para actualizar valores
        fun setupSeekBar(seekBar: SeekBar, textView: TextView) {

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                    // +1 porque SeekBar inicia en 0
                    val value = progress + 1

                    textView.text = "Valor: $value"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        // Activar listeners
        setupSeekBar(seek1, value1)
        setupSeekBar(seek2, value2)
        setupSeekBar(seek3, value3)
        setupSeekBar(seek4, value4)

        // Navegar a resultados
        btnResults.setOnClickListener {

            // Obtener valores de los sliders
            val score1 = seek1.progress + 1
            val score2 = seek2.progress + 1
            val score3 = seek3.progress + 1
            val score4 = seek4.progress + 1

            // Suma total
            val total = score1 + score2 + score3 + score4

            // Máximo posible = 20
            val percentage = (total * 100) / 20

            // Enviar datos a Results
            val intent = Intent(this, Results::class.java)

            intent.putExtra("percentage", percentage)

            startActivity(intent)
        }
    }
}
