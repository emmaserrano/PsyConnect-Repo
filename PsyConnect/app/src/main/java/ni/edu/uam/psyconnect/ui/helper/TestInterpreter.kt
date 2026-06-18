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

            "SELF_ESTEEM" ->
                interpretSelfEsteem(percentage)

            "RELATIONSHIPS" ->
                interpretRelationships(percentage)

            else ->
                interpretWellness(percentage)
        }
    }

    private fun interpretWellness(
        percentage: Int
    ): EmotionalFeedback {

        return when {

            percentage >= 75 ->

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

            percentage >= 50 ->

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

            percentage >= 75 ->

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

            percentage >= 50 ->

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

            percentage >= 75 ->

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

            percentage >= 50 ->

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

            percentage >= 75 ->

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

            percentage >= 50 ->

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

    private fun interpretSelfEsteem(
        percentage: Int
    ): EmotionalFeedback {

        return when {

            percentage >= 75 ->

                EmotionalFeedback(
                    "Autoestima saludable",
                    "Tienes una percepción positiva de ti mismo y de tus capacidades.",
                    listOf(
                        "Continúa reconociendo tus logros.",
                        "Mantén pensamientos positivos sobre ti.",
                        "Sigue fortaleciendo tu confianza."
                    ),
                    R.raw.happy
                )

            percentage >= 50 ->

                EmotionalFeedback(
                    "Autoestima moderada",
                    "Existen aspectos de tu autoconfianza que podrían fortalecerse.",
                    listOf(
                        "Reconoce tus fortalezas.",
                        "Evita compararte constantemente.",
                        "Celebra tus pequeños logros."
                    ),
                    R.raw.calm
                )

            else ->

                EmotionalFeedback(
                    "Autoestima baja",
                    "Podrías estar teniendo dificultades para valorar tus capacidades.",
                    listOf(
                        "Practica la autocompasión.",
                        "Identifica pensamientos negativos frecuentes.",
                        "Busca apoyo emocional si lo necesitas."
                    ),
                    R.raw.sad
                )
        }
    }

    private fun interpretRelationships(
        percentage: Int
    ): EmotionalFeedback {

        return when {

            percentage >= 75 ->

                EmotionalFeedback(
                    "Relaciones saludables",
                    "Cuentas con vínculos sociales positivos y de apoyo.",
                    listOf(
                        "Fortalece tus amistades.",
                        "Dedica tiempo a tus seres queridos.",
                        "Mantén una comunicación abierta."
                    ),
                    R.raw.happy
                )

            percentage >= 50 ->

                EmotionalFeedback(
                    "Relaciones moderadas",
                    "Existen oportunidades para fortalecer tus vínculos personales.",
                    listOf(
                        "Expresa tus emociones con confianza.",
                        "Comparte más tiempo con los demás.",
                        "Escucha activamente."
                    ),
                    R.raw.calm
                )

            else ->

                EmotionalFeedback(
                    "Dificultades relacionales",
                    "Podrías sentirte poco acompañado o desconectado socialmente.",
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