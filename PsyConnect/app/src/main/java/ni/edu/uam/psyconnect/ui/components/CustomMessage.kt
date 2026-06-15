package ni.edu.uam.psyconnect.ui.components

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

object CustomMessage {

    fun success(
        view: View,
        message: String
    ) {

        val snackbar =
            Snackbar.make(
                view,
                "✓ $message",
                Snackbar.LENGTH_LONG
            )

        snackbar.setBackgroundTint(
            Color.parseColor("#14B8A6")
        )

        snackbar.setTextColor(
            Color.WHITE
        )

        snackbar.show()
    }

    fun error(
        view: View,
        message: String
    ) {

        val snackbar =
            Snackbar.make(
                view,
                "✕ $message",
                Snackbar.LENGTH_LONG
            )

        snackbar.setBackgroundTint(
            Color.parseColor("#EF4444")
        )

        snackbar.setTextColor(
            Color.WHITE
        )

        snackbar.show()
    }
}