package ni.edu.uam.psyconnect.data.model

data class TestResult(

    val id: Long? = null,

    val userId: Long,

    val percentage: Int,

    val level: String,

    val createdAt: String? = null
)