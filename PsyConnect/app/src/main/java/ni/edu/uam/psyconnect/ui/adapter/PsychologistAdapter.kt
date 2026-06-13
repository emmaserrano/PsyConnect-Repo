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
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.Psychologist

class PsychologistAdapter(
    private val psychologists: List<Psychologist>
) : RecyclerView.Adapter<PsychologistAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val ivPhoto =
            itemView.findViewById<ImageView>(R.id.ivPhoto)

        val tvName =
            itemView.findViewById<TextView>(R.id.tvName)

        val tvSpecialty =
            itemView.findViewById<TextView>(R.id.tvSpecialty)

        val tvCity =
            itemView.findViewById<TextView>(R.id.tvCity)

        val tvRating =
            itemView.findViewById<TextView>(R.id.tvRating)

        val tvVirtual =
            itemView.findViewById<TextView>(R.id.tvVirtual)

        val btnWhatsapp =
            itemView.findViewById<Button>(R.id.btnWhatsapp)
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

        holder.tvName.text =
            psychologist.name

        holder.tvSpecialty.text =
            "🧠 ${psychologist.specialty}"

        holder.tvCity.text =
            "📍 ${psychologist.city}"

        holder.tvRating.text =
            "⭐ ${psychologist.rating}"

        holder.tvVirtual.text =
            if (psychologist.virtualAttention)
                "💻 Atención virtual"
            else
                "🏥 Presencial"

        holder.btnWhatsapp.setOnClickListener {

            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://wa.me/${psychologist.phone}"
                    )
                )

            holder.itemView.context.startActivity(intent)
        }
    }
}