package ni.edu.uam.psyconnect.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @SerializedName("email")
    val identifier: String,

    val password: String
)