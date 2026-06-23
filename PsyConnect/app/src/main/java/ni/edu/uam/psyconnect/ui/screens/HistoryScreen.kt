package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.psyconnect.data.model.TestResultEntity
import ni.edu.uam.psyconnect.ui.viewmodel.TestResultViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: TestResultViewModel,
    onBack: () -> Unit
) {
    val results by viewModel.results.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val filteredResults = if (selectedCategory == null) {
        results
    } else {
        results.filter { it.category == selectedCategory }
    }

    val categories = listOf(
        "WELLNESS" to "🌿 Bienestar",
        "STRESS" to "😌 Estrés",
        "SLEEP" to "😴 Sueño",
        "MOOD" to "😊 Ánimo",
        "RELATIONSHIPS" to "🤝 Social"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Historial", fontWeight = FontWeight.Bold, color = TurquesaOscuro) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = TurquesaOscuro)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = TurquesaFondo
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Resumen de Progreso",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // Filtros
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text("Todos") }
                        )
                    }
                    items(categories) { (code, label) ->
                        FilterChip(
                            selected = selectedCategory == code,
                            onClick = { selectedCategory = code },
                            label = { Text(label) }
                        )
                    }
                }
            }

            if (filteredResults.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(top = 64.dp), contentAlignment = Alignment.Center) {
                        Text("No hay resultados en esta categoría", color = Color.Gray)
                    }
                }
            }

            items(filteredResults) { result ->
                ResultCard(result)
            }
            
            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun ResultCard(result: TestResultEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.category,
                    fontSize = 12.sp,
                    color = TurquesaPrincipal,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = result.level,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TurquesaOscuro
                )
                Text(
                    text = result.createdAt,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(TurquesaPrincipal.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${result.percentage}%",
                    fontWeight = FontWeight.ExtraBold,
                    color = TurquesaPrincipal
                )
            }
        }
    }
}
