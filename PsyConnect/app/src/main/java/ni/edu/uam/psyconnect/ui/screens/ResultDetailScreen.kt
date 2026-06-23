package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import ni.edu.uam.psyconnect.ui.theme.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import ni.edu.uam.psyconnect.ui.helper.TestInterpreter
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultDetailScreen(
    category: String,
    percentage: Int,
    trend: Int,
    date: String,
    onBack: () -> Unit
) {
    val feedback = TestInterpreter.generate(category, percentage)
    
    val categoryName = when (category) {
        "WELLNESS" -> "Bienestar emocional"
        "STRESS" -> "Manejo del estrés"
        "SLEEP" -> "Sueño"
        "MOOD" -> "Estado de ánimo"
        "SELF_ESTEEM" -> "Autoestima"
        "RELATIONSHIPS" -> "Relaciones"
        else -> "Bienestar"
    }

    val statusColor = when {
        percentage >= 80 -> Color(0xFF16A34A)
        percentage >= 60 -> Color(0xFFCA8A04)
        percentage >= 40 -> Color(0xFFEA580C)
        else -> Color(0xFFDC2626)
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(feedback.animation))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle de Evaluación", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(Modifier.height(24.dp))
                
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(180.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = categoryName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "$percentage%",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = statusColor
                )

                Text(
                    text = feedback.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Evaluación del $date",
                    fontSize = 12.sp,
                    color = GrisSuave
                )

                Spacer(Modifier.height(24.dp))

                // Tarjeta de Tendencia
                TrendCard(trend)

                Spacer(Modifier.height(24.dp))

                // Recomendaciones
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Recomendaciones para ti",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp
                        )
                        Spacer(Modifier.height(16.dp))
                        feedback.recommendations.forEach { rec ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text("•", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.width(8.dp))
                                Text(text = rec, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp)
                            }
                        }
                    }
                }
                
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun TrendCard(trend: Int) {
    val (trendText, trendColor, icon) = when {
        trend > 0 -> Triple("Mejoraste $trend% respecto a la anterior", Color(0xFF16A34A), Icons.Default.TrendingUp)
        trend < 0 -> Triple("Bajó ${abs(trend)}% respecto a la anterior", Color(0xFFDC2626), Icons.Default.TrendingDown)
        else -> Triple("Sin cambios respecto a la anterior", Color(0xFF6B7280), Icons.Default.TrendingFlat)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = trendColor.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = trendColor)
            Spacer(Modifier.width(12.dp))
            Text(text = trendText, color = trendColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
