package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPsychologistScreen(
    name: String,
    specialty: String,
    city: String,
    email: String,
    phone: String,
    description: String,
    photo: String,
    onWhatsappClick: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil Profesional", fontWeight = FontWeight.Bold, color = TurquesaOscuro) },
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
        ) {
            // Cabecera con Foto y Nombre
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = "http://10.0.2.2:8080/uploads/$photo",
                            contentDescription = "Foto de $name",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(TurquesaFondo, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TurquesaOscuro,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "🧠 $specialty",
                            fontSize = 16.sp,
                            color = TurquesaPrincipal,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Información de Contacto
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Información de Contacto",
                        fontWeight = FontWeight.Bold,
                        color = TurquesaOscuro,
                        fontSize = 18.sp
                    )
                    
                    ContactInfoItem(Icons.Default.LocationOn, city)
                    ContactInfoItem(Icons.Default.Email, email)
                    ContactInfoItem(Icons.Default.Phone, phone)
                }
            }

            // Descripción
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(
                            "Sobre el especialista",
                            fontWeight = FontWeight.Bold,
                            color = TurquesaPrincipal,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = description,
                            color = GrisTexto,
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

            // Botón de WhatsApp
            item {
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = onWhatsappClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)) // Color oficial WhatsApp
                ) {
                    Text(
                        "Contactar por WhatsApp",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ContactInfoItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(TurquesaPrincipal.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = TurquesaPrincipal, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(16.dp))
        Text(text = text, color = GrisTexto, fontSize = 15.sp)
    }
}
