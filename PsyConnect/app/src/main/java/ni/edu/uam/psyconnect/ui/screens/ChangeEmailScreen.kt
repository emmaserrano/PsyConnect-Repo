package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.psyconnect.ui.viewmodel.ChangeEmailUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeEmailScreen(
    state: ChangeEmailUiState,
    onEmailChange: (String) -> Unit,
    onCodeChange: (String) -> Unit,
    onSendCode: () -> Unit,
    onVerify: () -> Unit,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cambiar Correo", fontWeight = FontWeight.Bold, color = TurquesaOscuro) },
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
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))

                Text(
                    text = "📧",
                    fontSize = 64.sp
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "¿Quieres cambiar tu correo?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TurquesaOscuro,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Te enviaremos un código de seguridad a tu nueva dirección para confirmar el cambio.",
                    fontSize = 14.sp,
                    color = GrisTexto,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(32.dp))

                // Campo de Nuevo Email
                Column {
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = onEmailChange,
                        label = { Text("Nuevo Correo Electrónico") },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = TurquesaPrincipal) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isCodeSent,
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TurquesaPrincipal)
                    )
                    
                    state.isEmailAvailable?.let { available ->
                        Text(
                            text = if (available) "✅ Correo disponible" else "❌ Correo ya registrado",
                            color = if (available) Color(0xFF2E7D32) else Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Sección de Código (Visible solo tras enviar)
                AnimatedVisibility(visible = state.isCodeSent) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = state.code,
                            onValueChange = onCodeChange,
                            label = { Text("Código de Verificación") },
                            leadingIcon = { Icon(Icons.Default.VpnKey, null, tint = TurquesaPrincipal) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TurquesaPrincipal)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (state.isTimerActive) "Reenviar en: ${state.timerText}" else "Código expirado",
                                fontSize = 12.sp,
                                color = if (state.isTimerActive) TurquesaPrincipal else Color.Red,
                                fontWeight = FontWeight.Bold
                            )
                            
                            if (!state.isTimerActive) {
                                Text(
                                    text = "Reenviar ahora",
                                    fontSize = 12.sp,
                                    color = TurquesaPrincipal,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable { onSendCode() }
                                )
                            }
                        }
                    }
                }
                
                Spacer(Modifier.height(24.dp))
            }

            // Botón de Acción Principal
            Button(
                onClick = { if (!state.isCodeSent) onSendCode() else onVerify() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TurquesaPrincipal),
                enabled = !state.isLoading && (
                    (!state.isCodeSent && state.isEmailAvailable == true) || 
                    (state.isCodeSent && state.code.isNotBlank())
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = if (!state.isCodeSent) "Enviar Código" else "Confirmar Cambio",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
