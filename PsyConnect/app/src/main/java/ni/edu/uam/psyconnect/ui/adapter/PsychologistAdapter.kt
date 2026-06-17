package ni.edu.uam.psyconnect.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.Psychologist

class PsychologistAdapter(
    private val psychologists: List<Psychologist>,
    private val onClick: (Psychologist) -> Unit
) : RecyclerView.Adapter<PsychologistAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val ivPhoto =
            itemView.findViewById<ImageView>(
                R.id.ivPhoto
            )

        val tvName =
            itemView.findViewById<TextView>(
                R.id.tvName
            )

        val tvSpecialty =
            itemView.findViewById<TextView>(
                R.id.tvSpecialty
            )

        val tvCity =
            itemView.findViewById<TextView>(
                R.id.tvCity
            )

        val tvStatus =
            itemView.findViewById<TextView>(
                R.id.tvStatus
            )

        val btnWhatsapp =
            itemView.findViewById<Button>(
                R.id.btnWhatsapp
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_psychologist,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return psychologists.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val psychologist =
            psychologists[position]

        // Nombre
        holder.tvName.text =
            psychologist.name

        // Especialidad
        holder.tvSpecialty.text =
            psychologist.specialty

        // Ciudad
        holder.tvCity.text =
            "📍 ${psychologist.city}"

        // Estado
        holder.tvStatus.text =
            "● Disponible"

        // Foto
        Glide.with(holder.itemView.context)
            .load(
                "http://10.0.2.2:8080/uploads/" +
                        psychologist.photo
            )
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)
            .centerCrop()
            .into(holder.ivPhoto)

        // Abrir detalle del especialista
        holder.itemView.setOnClickListener {

            onClick(psychologist)
        }

        // Abrir WhatsApp
        holder.btnWhatsapp.setOnClickListener {

            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://wa.me/${psychologist.phone}"
                    )
                )

            holder.itemView.context
                .startActivity(intent)
        }

        // Animación suave
        holder.itemView.alpha = 0f

        holder.itemView.animate()
            .alpha(1f)
            .setDuration(500)
            .start()
    }
}