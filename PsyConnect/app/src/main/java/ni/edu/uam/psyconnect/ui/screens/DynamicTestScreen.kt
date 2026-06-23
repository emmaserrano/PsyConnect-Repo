package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import ni.edu.uam.psyconnect.ui.viewmodel.DynamicTestUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicTestScreen(
    state: DynamicTestUiState,
    onOptionSelected: (Int) -> Unit,
    onNextClick: () -> Unit
) {
    val progress by animateFloatAsState(
        targetValue = if (state.totalQuestions > 0) (state.currentIndex + 1).toFloat() / state.totalQuestions else 0f,
        label = "progress"
    )

    Scaffold(
        containerColor = TurquesaFondo
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Contador y Progreso
            Text(
                text = "Pregunta ${state.currentIndex + 1} de ${state.totalQuestions}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TurquesaPrincipal
            )
            
            Spacer(Modifier.height(12.dp))
            
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color.White, RoundedCornerShape(4.dp)),
                color = TurquesaPrincipal,
                trackColor = Color.LightGray.copy(alpha = 0.2f)
            )

            Spacer(Modifier.height(40.dp))

            // Emoji de categoría (Simulado, puedes pasarlo por el estado)
            Text(
                text = "💭",
                fontSize = 64.sp
            )

            Spacer(Modifier.height(24.dp))

            // Texto de la Pregunta
            Text(
                text = state.currentQuestion?.text ?: "",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TurquesaOscuro,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))

            // Opciones
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(state.currentQuestion?.options ?: emptyList()) { index, option ->
                    val isSelected = state.selectedOptionIndex == index
                    OptionItem(
                        text = option,
                        isSelected = isSelected,
                        onClick = { onOptionSelected(index) }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Botón Siguiente
            Button(
                onClick = onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TurquesaPrincipal,
                    disabledContainerColor = GrisSuave.copy(alpha = 0.3f)
                ),
                enabled = state.selectedOptionIndex != null
            ) {
                Text(
                    text = if (state.currentIndex + 1 < state.totalQuestions) "Siguiente →" else "Ver resultados ✓",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun OptionItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) TurquesaPrincipal else Color.White,
        border = if (isSelected) null else BorderStroke(2.dp, TurquesaFondo.copy(alpha = 0.8f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(20.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else TurquesaOscuro
        )
    }
}
