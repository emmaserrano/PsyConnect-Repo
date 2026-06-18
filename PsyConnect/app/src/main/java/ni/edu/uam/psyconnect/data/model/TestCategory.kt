package ni.edu.uam.psyconnect.data.model

enum class TestCategory(
    val title: String,
    val emoji: String
) {

    WELLNESS(
        "Bienestar emocional",
        "🌿"
    ),

    STRESS(
        "Estrés",
        "🌊"
    ),

    MOOD(
        "Estado de ánimo",
        "☀"
    ),

    SLEEP(
        "Sueño y descanso",
        "🌙"
    ),

    SOCIAL(
        "Relaciones sociales",
        "🤝"
    ),

    SELF_ESTEEM(
        "Autoestima",
        "💙"
    )
}