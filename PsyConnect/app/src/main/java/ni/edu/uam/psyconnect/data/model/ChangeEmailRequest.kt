package ni.edu.uam.psyconnect.data.model

data class ChangeEmailRequest(

    val userId: Long,

    val newEmail: String
)