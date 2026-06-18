package ni.edu.uam.psyconnect.ui.helper

import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.EmotionalFeedback

object TestInterpreter {

    fun generate(
        category: String,
        percentage: Int
    ): EmotionalFeedback {

        return when (category) {

            "WELLNESS" ->
                interpretWellness(percentage)

            "STRESS" ->
                interpretStress(percentage)

            "SLEEP" ->
                interpretSleep(percentage)

            "MOOD" ->
                interpretMood(percentage)

            else ->
                interpretSocial(percentage)
        }
    }

    private fun interpretWellness(
        percentage: Int
    ): EmotionalFeedback {

        return when {

            percentage >= 80 ->

                EmotionalFeedback(
                    "Excelente bienestar",
                    "Presentas un equilibrio emocional saludable y estable.",
                    listOf(
                        "Continúa con tus hábitos positivos.",
                        "Mantén actividades recreativas.",
                        "Dedica tiempo a tus relaciones personales."
                    ),
                    R.raw.happy
                )

            percentage >= 60 ->

                EmotionalFeedback(
                    "Bienestar moderado",
                    "Existen áreas emocionales que podrían fortalecerse.",
                    listOf(
                        "Realiza ejercicio regularmente.",
                        "Practica técnicas de relajación.",
                        "Reserva tiempo para ti."
                    ),
                    R.raw.calm
                )

            else ->

                EmotionalFeedback(
                    "Bienestar bajo",
                    "Tu bienestar emocional podría estar viéndose afectado.",
                    listOf(
                        "Busca apoyo en personas cercanas.",
                        "Evita aislarte.",
                        "Considera recibir orientación profesional."
                    ),
                    R.raw.sad
                )
        }
    }

    private fun interpretStress(
        percentage: Int
    ): EmotionalFeedback {

        return when {

            percentage >= 80 ->

                EmotionalFeedback(
                    "Buen manejo del estrés",
                    "Actualmente manejas adecuadamente las situaciones de tensión.",
                    listOf(
                        "Continúa con tus hábitos saludables.",
                        "Mantén momentos de descanso.",
                        "Dedica tiempo al ocio."
                    ),
                    R.raw.happy
                )

            percentage >= 60 ->

                EmotionalFeedback(
                    "Estrés moderado",
                    "Existen señales de tensión que sería conveniente atender.",
                    listOf(
                        "Organiza tus actividades.",
                        "Haz pausas durante el día.",
                        "Evita sobrecargarte."
                    ),
                    R.raw.calm
                )

            else ->

                EmotionalFeedback(
                    "Estrés elevado",
                    "Actualmente podrías estar experimentando altos niveles de estrés.",
                    listOf(
                        "Practica respiración profunda.",
                        "Reduce cargas innecesarias.",
                        "Descansa adecuadamente."
                    ),
                    R.raw.stress
                )
        }
    }

    private fun interpretSleep(
        percentage: Int
    ): EmotionalFeedback {

        return when {

            percentage >= 80 ->

                EmotionalFeedback(
                    "Buen descanso",
                    "Tu calidad de sueño es favorable.",
                    listOf(
                        "Mantén horarios regulares.",
                        "Conserva una rutina saludable.",
                        "Continúa priorizando tu descanso."
                    ),
                    R.raw.sleep
                )

            percentage >= 60 ->

                EmotionalFeedback(
                    "Sueño moderado",
                    "Hay aspectos que podrían mejorar tu descanso.",
                    listOf(
                        "Evita cafeína en la noche.",
                        "Procura dormir más horas.",
                        "Reduce el estrés diario."
                    ),
                    R.raw.calm
                )

            else ->

                EmotionalFeedback(
                    "Problemas de sueño",
                    "Tu descanso podría estar afectando tu bienestar.",
                    listOf(
                        "Establece horarios fijos.",
                        "Evita pantallas antes de dormir.",
                        "Consulta con un especialista si el problema persiste."
                    ),
                    R.raw.sad
                )
        }
    }

    private fun interpretMood(
        percentage: Int
    ): EmotionalFeedback {

        return when {

            percentage >= 80 ->

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

            percentage >= 60 ->

                EmotionalFeedback(
                    "Estado de ánimo variable",
                    "Has experimentado algunos cambios emocionales recientes.",
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
        percentage: Int
    ): EmotionalFeedback {

        return when {

            percentage >= 80 ->

                EmotionalFeedback(
                    "Relaciones saludables",
                    "Cuentas con vínculos sociales positivos.",
                    listOf(
                        "Fortalece tus amistades.",
                        "Dedica tiempo a tus seres queridos.",
                        "Mantén una comunicación abierta."
                    ),
                    R.raw.happy
                )

            percentage >= 60 ->

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