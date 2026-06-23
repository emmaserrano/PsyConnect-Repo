package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CategoryItem(val title: String, val code: String, val icon: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestCategoryScreen(
    onCategoryClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val categories = listOf(
        CategoryItem("Bienestar", "WELLNESS", "🌿", "Equilibrio emocional general"),
        CategoryItem("Estrés", "STRESS", "🌊", "Niveles de tensión actual"),
        CategoryItem("Ánimo", "MOOD", "☀️", "Tus emociones recientes"),
        CategoryItem("Sueño", "SLEEP", "🌙", "Calidad de tu descanso"),
        CategoryItem("Social", "SOCIAL", "🤝", "Tus relaciones personales")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Evaluaciones", 
                        fontWeight = FontWeight.Bold, 
                        color = TurquesaOscuro 
                    ) 
                },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            
            Text(
                text = "¿Qué te gustaría evaluar hoy?",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            Text(
                text = "Selecciona una categoría para comenzar tu test personalizado.",
                fontSize = 14.sp,
                color = GrisTexto
            )

            Spacer(Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories) { category ->
                    CategoryCard(category, onCategoryClick)
                }
            }
        }
    }
}

@Composable
fun CategoryCard(category: CategoryItem, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick(category.code) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = category.icon, fontSize = 40.sp)
            Spacer(Modifier.height(12.dp))
            Text(
                text = category.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TurquesaOscuro
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = category.description,
                fontSize = 11.sp,
                color = GrisSuave,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}
