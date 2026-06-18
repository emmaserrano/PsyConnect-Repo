package ni.edu.uam.psyconnect.data.repository

import ni.edu.uam.psyconnect.data.model.Question

object TestRepository {

    private val defaultOptions =
        listOf(
            "Siempre",
            "Frecuentemente",
            "A veces",
            "Rara vez",
            "Nunca"
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
                    "Disfruto las actividades que realizo diariamente.",
                    defaultOptions
                ),

                Question(
                    "Me siento emocionalmente equilibrado.",
                    defaultOptions
                ),

                Question(
                    "Tengo energía para afrontar mis responsabilidades.",
                    defaultOptions
                )
            )

            "STRESS" -> listOf(

                Question(
                    "Logro mantener la calma ante situaciones difíciles.",
                    defaultOptions
                ),

                Question(
                    "Encuentro momentos para relajarme durante el día.",
                    defaultOptions
                ),

                Question(
                    "Me siento capaz de manejar mis responsabilidades.",
                    defaultOptions
                ),

                Question(
                    "Puedo controlar mis preocupaciones de forma adecuada.",
                    defaultOptions
                )
            )

            "MOOD" -> listOf(

                Question(
                    "Me siento optimista sobre mi futuro.",
                    defaultOptions
                ),

                Question(
                    "Disfruto compartir tiempo con otras personas.",
                    defaultOptions
                ),

                Question(
                    "Me siento motivado durante el día.",
                    defaultOptions
                ),

                Question(
                    "Tengo pensamientos positivos acerca de mí mismo.",
                    defaultOptions
                )
            )

            "SLEEP" -> listOf(

                Question(
                    "Duermo las horas necesarias para descansar.",
                    defaultOptions
                ),

                Question(
                    "Me despierto con sensación de descanso.",
                    defaultOptions
                ),

                Question(
                    "Logro dormir con facilidad.",
                    defaultOptions
                ),

                Question(
                    "Mi sueño suele ser reparador.",
                    defaultOptions
                )
            )

            else -> listOf(

                Question(
                    "Me siento escuchado por las personas cercanas.",
                    defaultOptions
                ),

                Question(
                    "Mantengo relaciones saludables con otras personas.",
                    defaultOptions
                ),

                Question(
                    "Puedo expresar mis emociones con confianza.",
                    defaultOptions
                ),

                Question(
                    "Siento apoyo cuando lo necesito.",
                    defaultOptions
                )
            )
        }
    }
}