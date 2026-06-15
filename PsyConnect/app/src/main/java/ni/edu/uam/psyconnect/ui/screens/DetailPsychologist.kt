package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
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

        val tvDescription =
            findViewById<TextView>(R.id.tvDescription)

        val name =
            intent.getStringExtra("name")

        val specialty =
            intent.getStringExtra("specialty")

        val city =
            intent.getStringExtra("city")

        val email =
            intent.getStringExtra("email")

        val description =
            intent.getStringExtra("description")

        val photo =
            intent.getStringExtra("photo")

        tvName.text = name
        tvSpecialty.text = specialty
        tvCity.text = city
        tvEmail.text = email
        tvDescription.text = description

        Glide.with(this)
            .load(
                "http://10.0.2.2:8080/uploads/$photo"
            )
            .into(ivPhoto)
    }
}