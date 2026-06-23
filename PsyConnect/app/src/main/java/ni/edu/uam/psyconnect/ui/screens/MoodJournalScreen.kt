package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalEntry
import ni.edu.uam.psyconnect.data.moodjournal.MoodJournalRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Paleta de colores oficial de PsyConnect
val TurquesaPrincipal = Color(0xFF14B8A6)
val TurquesaOscuro = Color(0xFF0F766E)
val TurquesaFondo = Color(0xFFF8FAFC)
val GrisTexto = Color(0xFF6B7280)
val GrisSuave = Color(0xFF9CA3AF)

enum class MoodType(val emoji: String, val label: String, val color: Color) {
    EXCELLENT("🤩", "Increíble", Color(0xFFFACC15)),
    GOOD("😊", "Bien", Color(0xFF4ADE80)),
    NORMAL("😐", "Normal", Color(0xFF60A5FA)),
    SAD("😔", "Triste", Color(0xFFF97316)),
    VERY_BAD("😫", "Mal", Color(0xFFF87171))
}

data class ActivityItem(val emoji: String, val label: String)
val activitiesList = listOf(
    ActivityItem("💼", "Trabajo"),
    ActivityItem("🏃‍♂️", "Deporte"),
    ActivityItem("🍱", "Comida"),
    ActivityItem("🛌", "Descanso"),
    ActivityItem("🤝", "Social"),
    ActivityItem("🎨", "Hobby")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodJournalScreen(
    repository: MoodJournalRepository,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var reflection by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf<MoodType?>(null) }
    val selectedActivities = remember { mutableStateListOf<String>() }
    
    var entries by remember { mutableStateOf(emptyList<MoodJournalEntry>()) }
    var isSheetOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        entries = repository.getAll()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Mi Diario Emocional", 
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TurquesaOscuro
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Regresar", tint = TurquesaOscuro)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            if (!isSheetOpen) {
                FloatingActionButton(
                    onClick = { 
                        reflection = ""
                        selectedMood = null
                        selectedActivities.clear()
                        isSheetOpen = true 
                    },
                    containerColor = TurquesaPrincipal,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(Icons.Default.Add, "Nueva entrada", modifier = Modifier.size(30.dp))
                }
            }
        },
        containerColor = TurquesaFondo
    ) { padding ->
        
        if (isSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { isSheetOpen = false },
                sheetState = sheetState,
                containerColor = Color.White,
                dragHandle = { BottomSheetDefaults.DragHandle(color = TurquesaPrincipal.copy(alpha = 0.3f)) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 48.dp)
                ) {
                    Text(
                        text = "¿Cómo va tu día?",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold, color = TurquesaOscuro
                        )
                    )
                    
                    Spacer(Modifier.height(24.dp))

                    // Selector de Ánimo Estilo Premium
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MoodType.entries.forEach { mood ->
                            val isSelected = selectedMood == mood
                            val scale by animateFloatAsState(if (isSelected) 1.2f else 1f, label = "")
                            val bgCircle by animateColorAsState(if (isSelected) mood.color.copy(alpha = 0.2f) else Color.Transparent, label = "")
                            
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(bgCircle)
                                    .clickable { selectedMood = mood }
                                    .padding(8.dp)
                                    .scale(scale)
                            ) {
                                Text(mood.emoji, fontSize = 34.sp)
                                Text(
                                    text = mood.label, 
                                    fontSize = 10.sp, 
                                    color = if (isSelected) TurquesaOscuro else GrisTexto,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(32.dp))
                    Text("¿Qué has estado haciendo?", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TurquesaOscuro)
                    Spacer(Modifier.height(12.dp))

                    // Selector de Actividades (Chips)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(activitiesList) { act ->
                            val isSelected = selectedActivities.contains(act.label)
                            FilterChip(
                                selected = isSelected,
                                onClick = { if (isSelected) selectedActivities.remove(act.label) else selectedActivities.add(act.label) },
                                label = { Text("${act.emoji} ${act.label}") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TurquesaPrincipal,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                    
                    OutlinedTextField(
                        value = reflection,
                        onValueChange = { reflection = it },
                        placeholder = { Text("Escribe una breve reflexión...", color = GrisSuave) },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TurquesaPrincipal,
                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                            focusedContainerColor = TurquesaFondo,
                            unfocusedContainerColor = TurquesaFondo
                        )
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (selectedMood == null) return@Button
                            scope.launch {
                                val entry = MoodJournalEntry(
                                    mood = selectedMood?.name ?: "NORMAL",
                                    reflection = reflection,
                                    activities = selectedActivities.joinToString(","),
                                    date = SimpleDateFormat("EEEE, d MMMM", Locale("es")).format(Date()).replaceFirstChar { it.uppercase() },
                                    timestamp = System.currentTimeMillis()
                                )
                                repository.insert(entry)
                                entries = repository.getAll()
                                isSheetOpen = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TurquesaPrincipal),
                        shape = RoundedCornerShape(16.dp),
                        enabled = selectedMood != null
                    ) {
                        Text("Guardar Momento", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Tu Trayectoria", 
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold, color = Color.Black
                    ),
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            if (entries.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Face, 
                            null, 
                            tint = GrisSuave.copy(alpha = 0.5f), 
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Tu diario está vacío.\n¡Cuéntame cómo va tu camino!", 
                            color = GrisSuave, 
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            itemsIndexed(entries) { _, entry ->
                val mood = try { MoodType.valueOf(entry.mood) } catch (e: Exception) { MoodType.NORMAL }
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(mood.color.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(mood.emoji, fontSize = 28.sp)
                        }
                        
                        Spacer(Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                entry.date, 
                                fontSize = 11.sp, 
                                color = TurquesaPrincipal, 
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = entry.reflection.ifEmpty { "Pura calma y reflexión..." },
                                fontSize = 15.sp, 
                                color = TurquesaOscuro, 
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                            if (entry.activities.isNotEmpty()) {
                                Text(
                                    text = entry.activities.split(",").joinToString(" • "),
                                    fontSize = 11.sp,
                                    color = GrisSuave
                                )
                            }
                        }
                        
                        IconButton(
                            onClick = { 
                                scope.launch { repository.delete(entry); entries = repository.getAll() } 
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete, 
                                null, 
                                tint = Color.Red.copy(0.3f), 
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}
