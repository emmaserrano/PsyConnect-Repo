package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import ni.edu.uam.psyconnect.ui.helper.TestInterpreter

@Composable
fun ResultsScreen(
    category: String,
    percentage: Int,
    onNavigateToHistory: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val feedback = TestInterpreter.generate(category, percentage)
    
    val scoreText = when(category) {
        "STRESS" -> "Manejo del estrés: $percentage%"
        "SLEEP" -> "Calidad del sueño: $percentage%"
        "SELF_ESTEEM" -> "Nivel de autoestima: $percentage%"
        "RELATIONSHIPS" -> "Calidad de relaciones: $percentage%"
        "MOOD" -> "Estado de ánimo positivo: $percentage%"
        else -> "Salud emocional: $percentage%"
    }

    val statusColor = when {
        percentage >= 75 -> Color(0xFF2E7D32)
        percentage >= 50 -> Color(0xFFF9A825)
        percentage >= 30 -> Color(0xFFEF6C00)
        else -> Color(0xFFC62828)
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(feedback.animation))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    Scaffold(
        containerColor = TurquesaFondo
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(Modifier.height(40.dp))
                
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(200.dp)
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = scoreText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = feedback.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = statusColor,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = feedback.description,
                            fontSize = 16.sp,
                            color = TurquesaOscuro,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(Modifier.height(24.dp))
                        
                        Text(
                            text = "Recomendaciones",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TurquesaPrincipal
                        )
                        
                        Spacer(Modifier.height(12.dp))
                        
                        feedback.recommendations.forEach { recommendation ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("✔", color = TurquesaPrincipal, modifier = Modifier.padding(end = 8.dp))
                                Text(
                                    text = recommendation,
                                    fontSize = 14.sp,
                                    color = GrisTexto
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = onNavigateToHistory,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TurquesaPrincipal)
                ) {
                    Text("Ver Historial", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onNavigateToHome,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TurquesaPrincipal),
                    border = androidx.compose.foundation.BorderStroke(2.dp, TurquesaPrincipal)
                ) {
                    Text("Volver al Inicio", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}
