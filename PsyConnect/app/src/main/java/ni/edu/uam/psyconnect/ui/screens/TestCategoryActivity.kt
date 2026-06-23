package ni.edu.uam.psyconnect.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class TestCategoryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
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
