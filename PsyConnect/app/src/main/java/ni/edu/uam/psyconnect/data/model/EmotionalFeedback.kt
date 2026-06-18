package ni.edu.uam.psyconnect.data.model

data class EmotionalFeedback(

    val title: String,

    val description: String,

    val recommendations: List<String>,

    val animation: Int
)