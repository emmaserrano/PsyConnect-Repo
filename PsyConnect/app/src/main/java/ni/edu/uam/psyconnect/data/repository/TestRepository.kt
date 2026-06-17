package ni.edu.uam.psyconnect.data.repository

import ni.edu.uam.psyconnect.data.model.Question

object TestRepository {

    private val defaultOptions =
        listOf(
            "Nunca",
            "Rara vez",
            "A veces",
            "Frecuentemente",
            "Siempre"
        )

    fun getQuestions(
        category: String
    ): List<Question> {

        return when (category) {

            "WELLNESS" -> listOf(

                Question(
                    "Me siento satisfecho con mi vida.",
                    defaultOptions
                ),

                Question(
                    "Disfruto mis actividades diarias.",
                    defaultOptions
                ),

                Question(
                    "Me siento emocionalmente estable.",
                    defaultOptions
                ),

                Question(
                    "Tengo energía para afrontar el día.",
                    defaultOptions
                )
            )

            "STRESS" -> listOf(

                Question(
                    "Me siento abrumado por mis responsabilidades.",
                    defaultOptions
                ),

                Question(
                    "Tengo dificultades para relajarme.",
                    defaultOptions
                ),

                Question(
                    "Siento tensión física frecuentemente.",
                    defaultOptions
                ),

                Question(
                    "Pienso constantemente en mis problemas.",
                    defaultOptions
                )
            )

            "MOOD" -> listOf(

                Question(
                    "Me siento optimista sobre mi futuro.",
                    defaultOptions
                ),

                Question(
                    "Disfruto pasar tiempo con otras personas.",
                    defaultOptions
                ),

                Question(
                    "Me siento motivado durante el día.",
                    defaultOptions
                ),

                Question(
                    "Tengo pensamientos positivos sobre mí.",
                    defaultOptions
                )
            )

            "SLEEP" -> listOf(

                Question(
                    "Duermo las horas necesarias.",
                    defaultOptions
                ),

                Question(
                    "Me despierto descansado.",
                    defaultOptions
                ),

                Question(
                    "Tengo dificultad para dormir.",
                    defaultOptions
                ),

                Question(
                    "Mi sueño es reparador.",
                    defaultOptions
                )
            )

            else -> listOf(

                Question(
                    "Me siento escuchado por las personas cercanas.",
                    defaultOptions
                ),

                Question(
                    "Tengo relaciones saludables.",
                    defaultOptions
                ),

                Question(
                    "Puedo expresar mis emociones.",
                    defaultOptions
                ),

                Question(
                    "Me siento acompañado cuando lo necesito.",
                    defaultOptions
                )
            )
        }
    }
}