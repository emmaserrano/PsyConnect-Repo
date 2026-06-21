package ni.edu.uam.psyconnect.data.model

data class Achievement(

    val id: Long? = null,

    val userId: Long,

    val title: String,
    val unlockedAt: String?,

    val description: String
)