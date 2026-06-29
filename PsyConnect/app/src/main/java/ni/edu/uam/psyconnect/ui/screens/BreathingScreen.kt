package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay
import ni.edu.uam.psyconnect.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreathingScreen(onBack: () -> Unit) {
    var isRunning by remember { mutableStateOf(false) }
    var phase by remember { mutableStateOf("Prepárate") }
    var secondsLeft by remember { mutableStateOf(0) }
    
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.breathinganimation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isRunning,
        speed = 0.8f
    )

    // Lógica del ciclo de respiración (Caja: 4s inhala, 4s mantiene, 4s exhala)
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (true) {
                phase = "Inhala"
                secondsLeft = 4
                while (secondsLeft > 0) { delay(1000); secondsLeft-- }
                
                phase = "Mantén"
                secondsLeft = 4
                while (secondsLeft > 0) { delay(1000); secondsLeft-- }
                
                phase = "Exhala"
                secondsLeft = 4
                while (secondsLeft > 0) { delay(1000); secondsLeft-- }

                phase = "Mantén"
                secondsLeft = 4
                while (secondsLeft > 0) { delay(1000); secondsLeft-- }
            }
        } else {
            phase = "Toca iniciar para comenzar"
            secondsLeft = 0
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Respiración Guiada", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Técnica de Respiración Cuadrada",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(40.dp))

            // Animación Lottie
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(250.dp)
                )
                
                // Texto de fase en el centro
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = phase,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (isRunning) {
                        Text(
                            text = secondsLeft.toString(),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(Modifier.height(60.dp))

            Text(
                text = "Encuentra una posición cómoda y sigue el ritmo de la animación para reducir el estrés.",
                modifier = Modifier.padding(horizontal = 40.dp),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(48.dp))

            Button(
                onClick = { isRunning = !isRunning },
                modifier = Modifier
                    .height(56.dp)
                    .width(200.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) MaterialTheme.colorScheme.error.copy(alpha = 0.8f) else MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(if (isRunning) Icons.Default.Refresh else Icons.Default.PlayArrow, null)
                Spacer(Modifier.width(8.dp))
                Text(if (isRunning) "Detener" else "Comenzar", fontWeight = FontWeight.Bold)
            }
        }
    }
}
