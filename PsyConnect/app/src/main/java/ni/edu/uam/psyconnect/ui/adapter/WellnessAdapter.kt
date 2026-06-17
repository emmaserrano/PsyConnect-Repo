package ni.edu.uam.psyconnect.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.WellnessItem

class WellnessAdapter(

    private val items: List<WellnessItem>,

    private val onClick: (WellnessItem) -> Unit

) : RecyclerView.Adapter<WellnessAdapter.ViewHolder>() {

    inner class ViewHolder(
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(

        LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_wellness,
                parent,
                false
            )
    ) {

        val animation =
            itemView.findViewById<LottieAnimationView>(
                R.id.lottieWellness
            )

        val btnStart =
            itemView.findViewById<MaterialButton>(
                R.id.btnStartTest
            )

        val tvTitle =
            itemView.findViewById<android.widget.TextView>(
                R.id.tvTitle
            )

        val tvDescription =
            itemView.findViewById<android.widget.TextView>(
                R.id.tvDescription
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder(parent)
    }

    override fun getItemCount(): Int {

        return items.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val item =
            items[position]

        holder.tvTitle.text =
            item.title

        holder.tvDescription.text =
            item.description

        holder.animation.setAnimation(
            item.animation
        )

        holder.animation.repeatCount = -1

        holder.animation.playAnimation()

        holder.btnStart.setOnClickListener {

            onClick(item)
        }
    }
}