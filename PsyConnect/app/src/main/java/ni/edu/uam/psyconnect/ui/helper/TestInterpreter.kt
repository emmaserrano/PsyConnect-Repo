package ni.edu.uam.psyconnect.ui.helper

import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.EmotionalFeedback

object TestInterpreter {

    fun generate(
        category: String,
        score: Int
    ): EmotionalFeedback {

        return when (category) {

            "WELLNESS" ->
                interpretWellness(score)

            "STRESS" ->
                interpretStress(score)

            "SLEEP" ->
                interpretSleep(score)

            "MOOD" ->
                interpretMood(score)

            else ->
                interpretSocial(score)
        }
    }

    private fun interpretWellness(
        score: Int
    ): EmotionalFeedback {

        return when {

            score >= 16 ->

                EmotionalFeedback(
                    "Excelente bienestar",
                    "Presentas un equilibrio emocional saludable.",
                    listOf(
                        "Continúa con tus hábitos positivos.",
                        "Mantén actividades recreativas.",
                        "Dedica tiempo a tus relaciones personales."
                    ),
                    R.raw.happy
                )

            score >= 10 ->

                EmotionalFeedback(
                    "Bienestar moderado",
                    "Existen áreas emocionales que pueden fortalecerse.",
                    listOf(
                        "Realiza ejercicio regularmente.",
                        "Practica técnicas de relajación.",
                        "Dedica tiempo para ti."
                    ),
                    R.raw.calm
                )

            else ->

                EmotionalFeedback(
                    "Bienestar bajo",
                    "Has experimentado dificultades emocionales recientemente.",
                    listOf(
                        "Busca apoyo en personas cercanas.",
                        "Evita aislarte.",
                        "Considera hablar con un profesional."
                    ),
                    R.raw.sad
                )
        }
    }

    private fun interpretStress(
        score: Int
    ): EmotionalFeedback {

        return when {

            score >= 16 ->

                EmotionalFeedback(
                    "Estrés elevado",
                    "Actualmente presentas niveles altos de estrés.",
                    listOf(
                        "Practica respiración profunda.",
                        "Reduce cargas innecesarias.",
                        "Descansa adecuadamente."
                    ),
                    R.raw.stress
                )

            score >= 10 ->

                EmotionalFeedback(
                    "Estrés moderado",
                    "Existen señales de tensión que deben atenderse.",
                    listOf(
                        "Organiza tus actividades.",
                        "Haz pausas durante el día.",
                        "Evita sobrecargarte."
                    ),
                    R.raw.calm
                )

            else ->

                EmotionalFeedback(
                    "Estrés bajo",
                    "Manejas adecuadamente las situaciones cotidianas.",
                    listOf(
                        "Continúa con tus hábitos saludables.",
                        "Mantén momentos de descanso.",
                        "Dedica tiempo al ocio."
                    ),
                    R.raw.happy
                )
        }
    }

    private fun interpretSleep(
        score: Int
    ): EmotionalFeedback {

        return when {

            score >= 16 ->

                EmotionalFeedback(
                    "Buen descanso",
                    "Tu calidad de sueño es favorable.",
                    listOf(
                        "Mantén horarios regulares.",
                        "Evita pantallas antes de dormir.",
                        "Conserva una rutina saludable."
                    ),
                    R.raw.sleep
                )

            score >= 10 ->

                EmotionalFeedback(
                    "Sueño moderado",
                    "Hay aspectos que podrían mejorar tu descanso.",
                    listOf(
                        "Evita cafeína en la noche.",
                        "Procura dormir más horas.",
                        "Reduce el estrés."
                    ),
                    R.raw.calm
                )

            else ->

                EmotionalFeedback(
                    "Problemas de sueño",
                    "Tu descanso podría estar afectando tu bienestar.",
                    listOf(
                        "Establece horarios fijos.",
                        "Evita usar el teléfono antes de dormir.",
                        "Consulta con un especialista si persiste."
                    ),
                    R.raw.sad
                )
        }
    }

    private fun interpretMood(
        score: Int
    ): EmotionalFeedback {

        return when {

            score >= 16 ->

                EmotionalFeedback(
                    "Estado de ánimo positivo",
                    "Predominan emociones agradables y optimismo.",
                    listOf(
                        "Continúa cultivando relaciones positivas.",
                        "Mantén actividades que disfrutes.",
                        "Sigue cuidando tu bienestar."
                    ),
                    R.raw.happy
                )

            score >= 10 ->

                EmotionalFeedback(
                    "Estado de ánimo variable",
                    "Has experimentado cambios emocionales recientes.",
                    listOf(
                        "Habla sobre tus emociones.",
                        "Practica gratitud.",
                        "Realiza actividades recreativas."
                    ),
                    R.raw.calm
                )

            else ->

                EmotionalFeedback(
                    "Estado de ánimo bajo",
                    "Podrías estar atravesando una etapa emocional difícil.",
                    listOf(
                        "Busca apoyo emocional.",
                        "Evita el aislamiento.",
                        "Considera recibir orientación profesional."
                    ),
                    R.raw.sad
                )
        }
    }

    private fun interpretSocial(
        score: Int
    ): EmotionalFeedback {

        return when {

            score >= 16 ->

                EmotionalFeedback(
                    "Relaciones saludables",
                    "Cuentas con vínculos sociales positivos.",
                    listOf(
                        "Fortalece tus amistades.",
                        "Dedica tiempo a tus seres queridos.",
                        "Continúa comunicándote de manera abierta."
                    ),
                    R.raw.happy
                )

            score >= 10 ->

                EmotionalFeedback(
                    "Relaciones moderadas",
                    "Existen oportunidades para fortalecer tus vínculos.",
                    listOf(
                        "Expresa tus emociones.",
                        "Comparte más tiempo con los demás.",
                        "Escucha activamente."
                    ),
                    R.raw.calm
                )

            else ->

                EmotionalFeedback(
                    "Aislamiento social",
                    "Podrías sentirte poco acompañado emocionalmente.",
                    listOf(
                        "Busca apoyo en familiares o amigos.",
                        "Participa en actividades sociales.",
                        "No enfrentes las dificultades en soledad."
                    ),
                    R.raw.sad
                )
        }
    }
}