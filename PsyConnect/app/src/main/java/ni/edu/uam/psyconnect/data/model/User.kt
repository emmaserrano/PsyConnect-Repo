package ni.edu.uam.psyconnect.data.model

data class User(

    val id: Long? = null,

    val name: String,

    val username: String,

    val email: String,

    val password: String,

    val birthdate: String,

    val profileImage: String? = null,

    val description: String = ""

)