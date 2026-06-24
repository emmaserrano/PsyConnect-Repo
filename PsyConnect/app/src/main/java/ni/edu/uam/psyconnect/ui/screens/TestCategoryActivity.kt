package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ni.edu.uam.psyconnect.ui.theme.PsyConnectTheme

class TestCategoryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("psyconnect", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("darkMode", false)

        setContent {
            PsyConnectTheme(darkTheme = isDarkMode) {
                TestCategoryScreen(
                    onCategoryClick = { category ->
                        val intent = Intent(this, DynamicTestActivity::class.java).apply {
                            putExtra("category", category)
                        }
                        startActivity(intent)
                    },
                    onBack = { finish() }
                )
            }
        }
    }
}
