package ni.edu.uam.psyconnect.data.model

data class User(

    val id: Long? = null,

    val name: String,

    val email: String,

    val password: String,

    val age: Int
)