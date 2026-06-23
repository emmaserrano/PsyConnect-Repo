package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ni.edu.uam.psyconnect.data.model.WellnessItem
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.Psychologist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    psychologists: List<Psychologist>,
    onTestClick: (String) -> Unit,
    onPsychologistClick: (Psychologist) -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToMoodJournal: () -> Unit
) {
    val wellnessItems = listOf(
        WellnessItem("🌿 Bienestar emocional", "Evalúa tu equilibrio emocional general.", R.raw.wellbeing, "WELLNESS"),
        WellnessItem("🌊 Estrés", "Conoce tu nivel actual de estrés.", R.raw.stress, "STRESS"),
        WellnessItem("☀️ Estado de ánimo", "Descubre cómo te has sentido últimamente.", R.raw.happy, "MOOD"),
        WellnessItem("🌙 Sueño y descanso", "Analiza la calidad de tu descanso.", R.raw.sleep, "SLEEP"),
        WellnessItem("🤝 Relaciones sociales", "Reflexiona sobre tu interacción social.", R.raw.social, "RELATIONSHIPS")
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White, toneElevation = 8.dp) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Inicio") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = TurquesaPrincipal, selectedTextColor = TurquesaPrincipal, indicatorColor = TurquesaPrincipal.copy(alpha = 0.1f))
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToHistory,
                    icon = { Icon(Icons.Default.Favorite, null) },
                    label = { Text("Historial") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToProfile,
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Perfil") }
                )
            }
        },
        containerColor = TurquesaFondo
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            // Header
            item {
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Hola, $userName 👋", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TurquesaOscuro)
                        Text("¿Cómo te sientes hoy?", fontSize = 16.sp, color = GrisTexto)
                    }
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(TurquesaPrincipal.copy(alpha = 0.1f), CircleShape)
                            .clickable { onNavigateToMoodJournal() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📔", fontSize = 20.sp)
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            // Sección Psicólogos
            item {
                Text("Especialistas para ti", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TurquesaOscuro)
                Spacer(Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(psychologists) { psych ->
                        PsychologistCard(psych, onPsychologistClick)
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            // Sección Evaluaciones
            item {
                Text("Evaluaciones de bienestar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TurquesaOscuro)
                Spacer(Modifier.height(12.dp))
            }

            items(wellnessItems) { item ->
                WellnessCard(item, onTestClick)
                Spacer(Modifier.height(12.dp))
            }
            
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun PsychologistCard(psych: Psychologist, onClick: (Psychologist) -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick(psych) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = psych.photo,
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(psych.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TurquesaOscuro, maxLines = 1)
            Text(psych.specialty, fontSize = 11.sp, color = TurquesaPrincipal, maxLines = 1)
        }
    }
}

@Composable
fun WellnessCard(item: WellnessItem, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(item.category) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(TurquesaPrincipal.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Aquí podrías usar Lottie, pero por ahora un emoji descriptivo
                Text(item.title.take(2), fontSize = 24.sp)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(item.title, fontWeight = FontWeight.Bold, color = TurquesaOscuro)
                Text(item.description, fontSize = 12.sp, color = GrisTexto)
            }
        }
    }
}
