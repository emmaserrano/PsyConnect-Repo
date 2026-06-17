package ni.edu.uam.psyconnect.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import android.widget.TextView
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.EmotionalTest

class EmotionalTestAdapter(

    private val tests: List<EmotionalTest>,

    private val onClick: (EmotionalTest) -> Unit

) : RecyclerView.Adapter<EmotionalTestAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val animation =
            itemView.findViewById<LottieAnimationView>(
                R.id.lottieTest
            )

        val title =
            itemView.findViewById<TextView>(
                R.id.tvTitle
            )

        val description =
            itemView.findViewById<TextView>(
                R.id.tvDescription
            )

        val duration =
            itemView.findViewById<TextView>(
                R.id.tvDuration
            )

        val difficulty =
            itemView.findViewById<TextView>(
                R.id.tvDifficulty
            )

        val btnStart =
            itemView.findViewById<MaterialButton>(
                R.id.btnStart
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
                    R.layout.item_emotional_test,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun getItemCount() =
        tests.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val test =
            tests[position]

        holder.animation.setAnimation(
            test.animation
        )

        holder.title.text =
            test.title

        holder.description.text =
            test.description

        holder.duration.text =
            "⏱ ${test.duration}"

        holder.difficulty.text =
            "⭐ ${test.difficulty}"

        holder.btnStart.setOnClickListener {

            onClick(test)
        }
    }
}