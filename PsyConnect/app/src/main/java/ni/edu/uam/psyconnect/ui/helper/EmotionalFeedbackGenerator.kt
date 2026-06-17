package ni.edu.uam.psyconnect.ui.helper

import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.EmotionalFeedback

object EmotionalFeedbackGenerator {

    fun generate(
        score: Int
    ): EmotionalFeedback {

        return when {

            score <= 5 ->

                EmotionalFeedback(

                    title =
                        "🌿 Bienestar emocional saludable",

                    description =
                        "Tus respuestas reflejan un buen equilibrio emocional. Continúa cuidando tus hábitos saludables.",

                    recommendations = listOf(

                        "Mantén tus rutinas de descanso.",

                        "Continúa realizando actividades que disfrutas.",

                        "Practica gratitud diariamente."

                    ),

                    animation =
                        R.raw.high_wellbeing
                )

            score <= 10 ->

                EmotionalFeedback(

                    title =
                        "🌤 Bienestar emocional moderado",

                    description =
                        "Has mostrado algunas señales de cansancio emocional. Pequeños cambios pueden ayudarte a sentirte mejor.",

                    recommendations = listOf(

                        "Dormir entre 7 y 8 horas.",

                        "Realizar pausas durante el día.",

                        "Practicar respiraciones profundas.",

                        "Hablar con alguien de confianza."

                    ),

                    animation =
                        R.raw.moderate_wellbeing
                )

            else ->

                EmotionalFeedback(

                    title =
                        "💙 Bienestar emocional delicado",

                    description =
                        "Tus respuestas indican un nivel importante de malestar emocional. Recuerda que pedir ayuda también es una forma de cuidarte.",

                    recommendations = listOf(

                        "Buscar apoyo emocional.",

                        "Evitar aislarte.",

                        "Descansar adecuadamente.",

                        "Considerar hablar con un profesional."

                    ),

                    animation =
                        R.raw.low_wellbeing
                )
        }
    }
}