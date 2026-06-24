package ni.edu.uam.psyconnect.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Long,
    val title: String,
    val description: String,
    val unlockedAt: String?,
    val serverId: Long? = null // Para sincronizar con el backend
)
