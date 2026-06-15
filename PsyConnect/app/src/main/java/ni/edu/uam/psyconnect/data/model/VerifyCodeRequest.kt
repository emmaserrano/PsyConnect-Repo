package ni.edu.uam.psyconnect.data.model

data class VerifyCodeRequest(

    val email: String,

    val code: String
)