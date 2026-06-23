package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Apariencia", 
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            item {
                Text(
                    text = "Personaliza tu experiencia visual en PsyConnect.",
                    fontSize = 14.sp,
                    color = GrisTexto,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(TurquesaPrincipal.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.DarkMode, null, tint = TurquesaPrincipal)
                            }
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(
                                    "Modo Oscuro",
                                    fontWeight = FontWeight.Bold,
                                    color = TurquesaOscuro
                                )
                                Text(
                                    "Reduce el brillo de la pantalla",
                                    fontSize = 12.sp,
                                    color = GrisTexto
                                )
                            }
                        }
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = onDarkModeChange,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = TurquesaPrincipal,
                                uncheckedThumbColor = GrisSuave,
                                uncheckedTrackColor = TurquesaFondo
                            )
                        )
                    }
                }
            }
        }
    }
}
