package ni.edu.uam.psyconnect.ui.screens

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.FragmentActivity

class OnboardingAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(
        position: Int
    ): Fragment {

        return when(position) {

            0 ->
                OnboardingFragment.newInstance(
                    "Bienvenido a PsyConnect",
                    "Un espacio seguro para cuidar tu bienestar emocional.",
                    "welcome"
                )

            1 ->
                OnboardingFragment.newInstance(
                    "Privacidad Garantizada",
                    "Tus datos y evaluaciones se almacenan de forma segura y confidencial.",
                    "security"
                )

            else ->
                OnboardingFragment.newInstance(
                    "Conoce Tu Progreso",
                    "Realiza evaluaciones y monitorea tu bienestar emocional a lo largo del tiempo.",
                    "progress"
                )
        }
    }
}