package ni.edu.uam.psyconnect.data.model

data class Mood(
    val id: Long? = null,
    val userId: Long,
    val mood: String,
    val date: String? = null
)