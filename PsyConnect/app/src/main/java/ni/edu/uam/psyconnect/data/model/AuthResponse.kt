package ni.edu.uam.psyconnect.data.model

data class AuthResponse(

    val success: Boolean,

    val message: String,

    val userId: Long?
)