package ni.edu.uam.psyconnect.data.model

data class ChangePasswordRequest(

    val userId: Long,

    val currentPassword: String,

    val newPassword: String
)