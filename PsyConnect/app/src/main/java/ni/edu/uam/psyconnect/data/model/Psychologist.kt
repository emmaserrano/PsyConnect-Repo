package ni.edu.uam.psyconnect.data.model

data class Psychologist(

    val id: Long?,

    val name: String,

    val specialty: String,

    val phone: String,

    val city: String,

    val email: String,

    val description: String,

    val photoUrl: String,

    val virtualAttention: Boolean,

    val rating: Double,

    val featured: Boolean
)