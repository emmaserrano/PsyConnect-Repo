package ni.edu.uam.psyconnect.data.model

data class Achievement(
    val id: Long? = null,
    val userId: Long,
    val badge: String,
    val description: String,
    val unlockedAt: String
)