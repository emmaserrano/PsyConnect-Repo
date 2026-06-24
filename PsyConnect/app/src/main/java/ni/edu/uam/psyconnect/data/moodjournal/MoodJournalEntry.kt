package ni.edu.uam.psyconnect.data.moodjournal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_journal")
data class MoodJournalEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Long, // Asociado al usuario actual
    val mood: String,
    val reflection: String,
    val date: String,
    val timestamp: Long,
    val activities: String = ""
)
