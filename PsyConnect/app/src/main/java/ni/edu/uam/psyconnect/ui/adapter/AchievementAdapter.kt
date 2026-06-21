package ni.edu.uam.psyconnect.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.Achievement
import java.text.SimpleDateFormat
import java.util.Locale

class AchievementAdapter(
    private val achievements: List<Achievement>
) : RecyclerView.Adapter<AchievementAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvIcon: TextView = itemView.findViewById(R.id.tvAchievementIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_achievement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val achievement = achievements[position]
        val context = holder.itemView.context

        holder.tvTitle.text = achievement.title
        holder.tvDescription.text = achievement.description
        
        // Mapeo dinámico de emojis según el logro
        holder.tvIcon.text = when {
            achievement.title.contains("Primer", true) -> "🏅"
            achievement.title.contains("Semana", true) -> "✨"
            achievement.title.contains("Bienestar", true) -> "🌿"
            achievement.title.contains("Constancia", true) -> "🔥"
            achievement.title.contains("Sueño", true) -> "😴"
            achievement.title.contains("Estrés", true) -> "😌"
            else -> "🏆"
        }

        // Formatear fecha usando recursos del sistema
        holder.tvDate.text = if (!achievement.unlockedAt.isNullOrBlank()) {
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd 'de' MMMM, yyyy", Locale.getDefault())
                val date = inputFormat.parse(achievement.unlockedAt)
                if (date != null) {
                    context.getString(R.string.achievement_date_format, outputFormat.format(date))
                } else {
                    context.getString(R.string.achievement_unlocked_placeholder)
                }
            } catch (e: Exception) {
                context.getString(R.string.achievement_unlocked_placeholder)
            }
        } else {
            context.getString(R.string.achievement_keep_going)
        }
    }

    override fun getItemCount(): Int = achievements.size
}
