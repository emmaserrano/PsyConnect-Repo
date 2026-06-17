package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import ni.edu.uam.psyconnect.R

class DetailPsychologist : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_detail_psychologist
        )

        val ivPhoto =
            findViewById<ImageView>(R.id.ivPhoto)

        val tvName =
            findViewById<TextView>(R.id.tvName)

        val tvSpecialty =
            findViewById<TextView>(R.id.tvSpecialty)

        val tvCity =
            findViewById<TextView>(R.id.tvCity)

        val tvEmail =
            findViewById<TextView>(R.id.tvEmail)

        val tvPhone =
            findViewById<TextView>(R.id.tvPhone)

        val tvDescription =
            findViewById<TextView>(R.id.tvDescription)

        val btnWhatsapp =
            findViewById<Button>(R.id.btnWhatsapp)

        val name =
            intent.getStringExtra("name") ?: ""

        val specialty =
            intent.getStringExtra("specialty") ?: ""

        val city =
            intent.getStringExtra("city") ?: ""

        val email =
            intent.getStringExtra("email") ?: ""
        Toast.makeText(
            this,
            "Email recibido: $email",
            Toast.LENGTH_LONG
        ).show()

        val phone =
            intent.getStringExtra("phone") ?: ""

        val description =
            intent.getStringExtra("description") ?: ""

        val photo =
            intent.getStringExtra("photo") ?: ""

        tvName.text =
            name

        tvSpecialty.text =
            "🧠 $specialty"

        tvCity.text =
            "📍 $city"

        tvEmail.text =
            "✉ $email"

        tvPhone.text =
            "📱 $phone"

        tvDescription.text =
            description

        Glide.with(this)
            .load(
                "http://10.0.2.2:8080/uploads/$photo"
            )
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)
            .into(ivPhoto)

        btnWhatsapp.setOnClickListener {

            val url =
                "https://wa.me/505$phone?text=Hola,%20vi%20tu%20perfil%20en%20PsyConnect%20y%20me%20gustaría%20más%20información."

            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )

            startActivity(intent)
        }
    }
}