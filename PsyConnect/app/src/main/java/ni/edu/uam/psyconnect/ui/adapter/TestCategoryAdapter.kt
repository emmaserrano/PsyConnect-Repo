package ni.edu.uam.psyconnect.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.TestCategory
import ni.edu.uam.psyconnect.ui.screens.Test

class TestCategoryAdapter(

    private val categories: List<TestCategory>,

    private val context: Context

) : RecyclerView.Adapter<TestCategoryAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val tvTitle =
            itemView.findViewById<TextView>(
                R.id.tvTitle
            )

        val lottie =
            itemView.findViewById<LottieAnimationView>(
                R.id.lottieCategory
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
                    R.layout.item_test_category,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int =
        categories.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val category =
            categories[position]

        holder.tvTitle.text =
            "${category.emoji} ${category.title}"

        when(category){

            TestCategory.WELLNESS ->
                holder.lottie.setAnimation(
                    R.raw.wellness
                )

            TestCategory.STRESS ->
                holder.lottie.setAnimation(
                    R.raw.stress
                )

            TestCategory.MOOD ->
                holder.lottie.setAnimation(
                    R.raw.mood
                )

            TestCategory.SLEEP ->
                holder.lottie.setAnimation(
                    R.raw.sleep
                )

            TestCategory.SOCIAL ->
                holder.lottie.setAnimation(
                    R.raw.social
                )

            TestCategory.SELF_ESTEEM ->
                holder.lottie.setAnimation(
                    R.raw.self_esteem
                )
        }

        holder.btnStart.setOnClickListener {

            val intent =
                Intent(
                    context,
                    Test::class.java
                )

            intent.putExtra(
                "category",
                category.name
            )

            context.startActivity(
                intent
            )
        }
    }
}