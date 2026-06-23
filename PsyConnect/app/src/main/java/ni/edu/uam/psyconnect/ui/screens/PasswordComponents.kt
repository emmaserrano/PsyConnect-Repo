package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ni.edu.uam.psyconnect.ui.theme.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordRequirementsView(password: String) {
    val requirements = listOf(
        "Mínimo 8 caracteres" to (password.length >= 8),
        "Una mayúscula" to password.any { it.isUpperCase() },
        "Una minúscula" to password.any { it.isLowerCase() },
        "Un número" to password.any { it.isDigit() },
        "Un carácter especial" to password.any { !it.isLetterOrDigit() }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 8.dp)
    ) {
        requirements.forEach { (text, met) ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                Text(
                    text = if (met) "✔" else "✖",
                    color = if (met) Color(0xFF2E7D32) else Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = text,
                    color = if (met) Color(0xFF2E7D32) else Color.Red,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun PasswordStrengthIndicator(password: String) {
    if (password.isEmpty()) return
    
    val strength = calculatePasswordStrength(password)
    val color = when {
        strength <= 0.2f -> Color.Red
        strength <= 0.4f -> Color(0xFFFF9800)
        strength <= 0.6f -> Color(0xFFFBC02D)
        strength <= 0.8f -> Color(0xFF4CAF50)
        else -> Color(0xFF2E7D32)
    }
    
    Column(modifier = Modifier.padding(top = 8.dp)) {
        LinearProgressIndicator(
            progress = { strength },
            modifier = Modifier.fillMaxWidth().height(4.dp),
            color = color,
            trackColor = Color.LightGray.copy(alpha = 0.3f)
        )
        Text(
            text = when {
                strength <= 0.2f -> "Muy débil"
                strength <= 0.4f -> "Débil"
                strength <= 0.6f -> "Aceptable"
                strength <= 0.8f -> "Fuerte"
                else -> "Muy fuerte"
            },
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

fun calculatePasswordStrength(password: String): Float {
    var score = 0f
    if (password.length >= 8) score += 0.2f
    if (password.any { it.isUpperCase() }) score += 0.2f
    if (password.any { it.isLowerCase() }) score += 0.2f
    if (password.any { it.isDigit() }) score += 0.2f
    if (password.any { !it.isLetterOrDigit() }) score += 0.2f
    return score
}
