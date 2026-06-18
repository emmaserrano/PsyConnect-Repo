package ni.edu.uam.psyconnect.ui.helper

import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.TestFeedback

object TestFeedbackGenerator {

    fun generate(
        category: String,
        score: Int,
        maxScore: Int
    ): TestFeedback {

        val percentage =
            score * 100 / maxScore

        return when (category) {

            "STRESS" ->
                stressFeedback(percentage)

            "SLEEP" ->
                sleepFeedback(percentage)

            "MOOD" ->
                moodFeedback(percentage)

            "SOCIAL" ->
                socialFeedback(percentage)

            else ->
                wellnessFeedback(percentage)
        }
    }

    private fun stressFeedback(
        percentage: Int
    ): TestFeedback {

        return when {

            percentage <= 40 ->

                TestFeedback(

                    "Estrés bajo",

                    "Actualmente manejas adecuadamente las situaciones que generan tensión.",

                    listOf(

                        "Continúa con tus hábitos saludables.",

                        "Dedica tiempo a actividades recreativas.",

                        "Mantén un equilibrio entre estudio y descanso."
                    ),

                    R.raw.happy
                )

            percentage <= 70 ->

                TestFeedback(

                    "Estrés moderado",

                    "Presentas algunos signos de tensión emocional.",

                    listOf(

                        "Realiza pausas durante el día.",

                        "Practica respiración profunda.",

                        "Evita la sobrecarga de actividades."
                    ),

                    R.raw.meditation
                )

            else ->

                TestFeedback(

                    "Estrés elevado",

                    "Has mostrado niveles importantes de estrés.",

                    listOf(

                        "Prioriza el descanso.",

                        "Reduce las fuentes de presión.",

                        "Considera buscar apoyo profesional."
                    ),

                    R.raw.social
                )
        }
    }

    private fun sleepFeedback(
        percentage: Int
    ): TestFeedback {

        return when {

            percentage <= 40 ->

                TestFeedback(

                    "Sueño saludable",

                    "Tu descanso parece ser adecuado.",

                    listOf(

                        "Mantén horarios regulares.",

                        "Continúa con una rutina de sueño saludable."
                    ),

                    R.raw.sleep
                )

            percentage <= 70 ->

                TestFeedback(

                    "Alteraciones moderadas",

                    "Tu descanso podría mejorar.",

                    listOf(

                        "Evita pantallas antes de dormir.",

                        "Reduce el consumo de cafeína.",

                        "Intenta acostarte a la misma hora."
                    ),

                    R.raw.meditation
                )

            else ->

                TestFeedback(

                    "Problemas importantes de sueño",

                    "Has reportado dificultades significativas para descansar.",

                    listOf(

                        "Mantén una rutina nocturna.",

                        "Evita estimulantes.",

                        "Busca orientación profesional si persiste."
                    ),

                    R.raw.social
                )
        }
    }

    private fun moodFeedback(
        percentage: Int
    ): TestFeedback {

        return when {

            percentage <= 40 ->

                TestFeedback(

                    "Estado de ánimo positivo",

                    "Tus respuestas reflejan estabilidad emocional.",

                    listOf(

                        "Continúa fortaleciendo tus hábitos positivos.",

                        "Comparte tiempo con las personas que aprecias."
                    ),

                    R.raw.happy
                )

            percentage <= 70 ->

                TestFeedback(

                    "Altibajos emocionales",

                    "Has experimentado algunos cambios en tu estado de ánimo.",

                    listOf(

                        "Practica actividades que disfrutes.",

                        "Mantén contacto con tus seres queridos."
                    ),

                    R.raw.meditation
                )

            else ->

                TestFeedback(

                    "Estado de ánimo vulnerable",

                    "Tus respuestas indican malestar emocional importante.",

                    listOf(

                        "Habla con alguien de confianza.",

                        "Evita aislarte.",

                        "Considera buscar ayuda profesional."
                    ),

                    R.raw.social
                )
        }
    }

    private fun socialFeedback(
        percentage: Int
    ): TestFeedback {

        return TestFeedback(

            "Relaciones sociales",

            "Tus vínculos personales son una parte importante de tu bienestar.",

            listOf(

                "Fortalece tus relaciones positivas.",

                "Expresa tus emociones con confianza.",

                "Busca apoyo cuando lo necesites."
            ),

            R.raw.social
        )
    }

    private fun wellnessFeedback(
        percentage: Int
    ): TestFeedback {

        return TestFeedback(

            "Bienestar emocional",

            "Tu equilibrio emocional es un aspecto valioso para tu salud.",

            listOf(

                "Continúa cuidando tu salud mental.",

                "Practica actividades que te generen bienestar.",

                "Dedica tiempo al autocuidado."
            ),

            R.raw.wellbeing
        )
    }
}