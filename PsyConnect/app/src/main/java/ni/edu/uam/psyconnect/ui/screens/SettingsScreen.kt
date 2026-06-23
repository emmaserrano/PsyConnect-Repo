package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onAppearanceClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onAboutClick: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Configuración", 
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
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            item {
                Text(
                    text = "Personalización y Cuenta",
                    style = MaterialTheme.typography.titleSmall,
                    color = GrisTexto,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }

            item {
                ProfileMenuItem(
                    title = "Apariencia",
                    icon = Icons.Default.ColorLens,
                    onClick = onAppearanceClick
                )
            }

            item {
                ProfileMenuItem(
                    title = "Seguridad",
                    icon = Icons.Default.Lock,
                    onClick = onSecurityClick
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Más información",
                    style = MaterialTheme.typography.titleSmall,
                    color = GrisTexto,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }

            item {
                ProfileMenuItem(
                    title = "Acerca de PsyConnect",
                    icon = Icons.Default.Info,
                    onClick = onAboutClick
                )
            }
        }
    }
}
