package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import ni.edu.uam.psyconnect.R

class OnboardingFragment : Fragment() {

    companion object {

        fun newInstance(
            title: String,
            description: String,
            animation: String
        ): OnboardingFragment {

            val fragment =
                OnboardingFragment()

            val args =
                Bundle()

            args.putString(
                "title",
                title
            )

            args.putString(
                "description",
                description
            )

            args.putString(
                "animation",
                animation
            )

            fragment.arguments =
                args

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view =
            inflater.inflate(
                R.layout.item_onboarding,
                container,
                false
            )

        view.findViewById<TextView>(
            R.id.tvTitle
        ).text =
            arguments?.getString("title")

        view.findViewById<TextView>(
            R.id.tvDescription
        ).text =
            arguments?.getString("description")

        val animation =
            arguments?.getString("animation")

        val lottie =
            view.findViewById<LottieAnimationView>(
                R.id.lottieAnimation
            )

        when(animation){

            "welcome" ->
                lottie.setAnimation(
                    R.raw.welcome
                )

            "security" ->
                lottie.setAnimation(
                    R.raw.security
                )

            "progress" ->
                lottie.setAnimation(
                    R.raw.progress
                )
        }

        return view
    }
}