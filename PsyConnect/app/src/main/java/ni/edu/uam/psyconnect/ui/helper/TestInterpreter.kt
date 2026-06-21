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
                    "Estrés elevado",
                    "Actualmente podrías estar experimentando altos niveles de estrés que están afectando tu bienestar emocional.",
                    listOf(
                        "Practica ejercicios de respiración profunda.",
                        "Reduce cargas innecesarias cuando sea posible.",
                        "Programa momentos de descanso durante el día.",
                        "Considera buscar apoyo profesional si el estrés persiste."
                    ),
                    R.raw.stress
                )

            percentage >= 50 ->

                EmotionalFeedback(
                    "Estrés moderado",
                    "Se observan algunas señales de tensión emocional que conviene atender.",
                    listOf(
                        "Organiza tus actividades por prioridades.",
                        "Haz pausas breves durante tus jornadas.",
                        "Dedica tiempo a actividades relajantes."
                    ),
                    R.raw.calm
                )

            else ->

                EmotionalFeedback(
                    "Buen manejo del estrés",
                    "Actualmente manejas adecuadamente las situaciones de presión y tensión.",
                    listOf(
                        "Mantén tus hábitos saludables.",
                        "Continúa equilibrando trabajo y descanso.",
                        "Sigue practicando actividades que favorezcan tu bienestar."
                    ),
                    R.raw.happy
                )
        }
    }

    private fun interpretSleep(
        percentage: Int
    ): EmotionalFeedback {

        return when {

            percentage >= 90 ->

                EmotionalFeedback(
                    "Descanso excelente",
                    "Tu calidad de sueño favorece tu bienestar físico y emocional.",
                    listOf(
                        "Mantén tus horarios.",
                        "Continúa cuidando tu higiene del sueño.",
                        "Evita cambios bruscos de rutina."
                    ),
                    R.raw.sleep
                )

            percentage >= 70 ->

                EmotionalFeedback(
                    "Buen descanso",
                    "En general duermes adecuadamente.",
                    listOf(
                        "Mantén una rutina estable.",
                        "Evita cafeína antes de dormir.",
                        "Realiza ejercicio regularmente."
                    ),
                    R.raw.sleep
                )

            percentage >= 50 ->

                EmotionalFeedback(
                    "Sueño regular",
                    "Existen algunos hábitos que podrían mejorar tu descanso.",
                    listOf(
                        "Reduce el uso del celular antes de dormir.",
                        "Respeta horarios.",
                        "Evita cenas pesadas."
                    ),
                    R.raw.calm
                )

            percentage >= 30 ->

                EmotionalFeedback(
                    "Sueño deficiente",
                    "La calidad de tu descanso podría afectar tu estado de ánimo.",
                    listOf(
                        "Implementa una rutina nocturna.",
                        "Evita pantallas.",
                        "Consulta a un especialista si continúa."
                    ),
                    R.raw.sad
                )

            else ->

                EmotionalFeedback(
                    "Alteración importante del sueño",
                    "Los resultados indican dificultades significativas para descansar.",
                    listOf(
                        "Busca orientación profesional.",
                        "No normalices el cansancio constante.",
                        "Prioriza el descanso."
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

            percentage >= 90 ->

                EmotionalFeedback(
                    "Autoestima muy saludable",
                    "Confías en tus capacidades y mantienes una imagen positiva de ti.",
                    listOf(
                        "Sigue fortaleciendo tus fortalezas.",
                        "Mantén una actitud positiva.",
                        "Apoya también a quienes te rodean."
                    ),
                    R.raw.happy
                )

            percentage >= 70 ->

                EmotionalFeedback(
                    "Buena autoestima",
                    "Generalmente tienes confianza en ti mismo.",
                    listOf(
                        "Reconoce tus logros.",
                        "Acepta los errores como aprendizaje.",
                        "Continúa creciendo."
                    ),
                    R.raw.happy
                )

            percentage >= 50 ->

                EmotionalFeedback(
                    "Autoestima media",
                    "Existen momentos en los que dudas de tus capacidades.",
                    listOf(
                        "Celebra pequeños logros.",
                        "Evita compararte.",
                        "Practica pensamientos positivos."
                    ),
                    R.raw.calm
                )

            percentage >= 30 ->

                EmotionalFeedback(
                    "Autoestima baja",
                    "Tu percepción personal podría estar afectando tu bienestar.",
                    listOf(
                        "Practica la autocompasión.",
                        "Reconoce tus fortalezas.",
                        "Busca apoyo si lo necesitas."
                    ),
                    R.raw.sad
                )

            else ->

                EmotionalFeedback(
                    "Autoestima muy baja",
                    "Es recomendable trabajar activamente en fortalecer tu confianza personal.",
                    listOf(
                        "Habla con un profesional.",
                        "No enfrentes esto en soledad.",
                        "Recuerda que tu valor no depende de tus errores."
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