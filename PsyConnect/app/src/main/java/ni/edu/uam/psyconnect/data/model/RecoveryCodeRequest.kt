package ni.edu.uam.psyconnect.data.model

data class RecoveryCodeRequest(
    val email: String,
    val code: String
)