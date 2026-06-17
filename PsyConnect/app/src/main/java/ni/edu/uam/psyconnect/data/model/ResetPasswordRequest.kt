package ni.edu.uam.psyconnect.data.model

data class ResetPasswordRequest(
    val email: String,
    val newPassword: String
)