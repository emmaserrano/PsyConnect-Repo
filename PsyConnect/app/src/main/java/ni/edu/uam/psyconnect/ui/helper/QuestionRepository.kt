package ni.edu.uam.psyconnect.ui.helper

import ni.edu.uam.psyconnect.data.model.Question
import ni.edu.uam.psyconnect.data.model.TestCategory

object QuestionRepository {

    fun getQuestions(
        category: TestCategory
    ): List<Question> {

        return when(category){

            TestCategory.WELLNESS ->
                wellnessQuestions()

            TestCategory.STRESS ->
                stressQuestions()

            TestCategory.MOOD ->
                moodQuestions()

            TestCategory.SLEEP ->
                sleepQuestions()

            TestCategory.SOCIAL ->
                socialQuestions()

            TestCategory.SELF_ESTEEM ->
                selfEsteemQuestions()
        }
    }

    private fun wellnessQuestions() =
        listOf(

            Question(
                "¿Te has sentido tranquilo(a) durante la última semana?",
                listOf(
                    "Nunca",
                    "Rara vez",
                    "A veces",
                    "Frecuentemente"
                )
            ),

            Question(
                "¿Has disfrutado tus actividades diarias?",
                listOf(
                    "Nunca",
                    "Rara vez",
                    "A veces",
                    "Frecuentemente"
                )
            )
        )

    private fun stressQuestions() =
        listOf(

            Question(
                "¿Te has sentido abrumado(a) por las responsabilidades?",
                listOf(
                    "Nunca",
                    "Rara vez",
                    "A veces",
                    "Frecuentemente"
                )
            ),

            Question(
                "¿Has tenido dificultad para relajarte?",
                listOf(
                    "Nunca",
                    "Rara vez",
                    "A veces",
                    "Frecuentemente"
                )
            )
        )

    private fun moodQuestions() =
        listOf(

            Question(
                "¿Te has sentido optimista recientemente?",
                listOf(
                    "Nunca",
                    "Rara vez",
                    "A veces",
                    "Frecuentemente"
                )
            ),

            Question(
                "¿Has sentido entusiasmo por tus actividades?",
                listOf(
                    "Nunca",
                    "Rara vez",
                    "A veces",
                    "Frecuentemente"
                )
            )
        )

    private fun sleepQuestions() =
        listOf(

            Question(
                "¿Has dormido suficientes horas?",
                listOf(
                    "Nunca",
                    "Rara vez",
                    "A veces",
                    "Frecuentemente"
                )
            ),

            Question(
                "¿Te despiertas descansado(a)?",
                listOf(
                    "Nunca",
                    "Rara vez",
                    "A veces",
                    "Frecuentemente"
                )
            )
        )

    private fun socialQuestions() =
        listOf(

            Question(
                "¿Te sientes apoyado(a) por las personas cercanas?",
                listOf(
                    "Nunca",
                    "Rara vez",
                    "A veces",
                    "Frecuentemente"
                )
            ),

            Question(
                "¿Disfrutas pasar tiempo con otras personas?",
                listOf(
                    "Nunca",
                    "Rara vez",
                    "A veces",
                    "Frecuentemente"
                )
            )
        )

    private fun selfEsteemQuestions() =
        listOf(

            Question(
                "¿Te sientes satisfecho(a) contigo mismo(a)?",
                listOf(
                    "Nunca",
                    "Rara vez",
                    "A veces",
                    "Frecuentemente"
                )
            ),

            Question(
                "¿Reconoces tus logros personales?",
                listOf(
                    "Nunca",
                    "Rara vez",
                    "A veces",
                    "Frecuentemente"
                )
            )
        )
}