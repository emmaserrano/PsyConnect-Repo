package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.*
import ni.edu.uam.psyconnect.ui.theme.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.psyconnect.ui.viewmodel.ForgotPasswordUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordUiState,
    onEmailChange: (String) -> Unit,
    onCodeChange: (String) -> Unit,
    onSendCode: () -> Unit,
    onVerifyCode: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Recuperar Cuenta", fontWeight = FontWeight.Bold, color = TurquesaOscuro) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás", tint = TurquesaOscuro)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            Text(
                text = "🔐",
                fontSize = 64.sp
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "¿Olvidaste tu contraseña?",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = if (!state.isCodeSent) 
                    "Ingresa tu correo electrónico y te enviaremos un código para restablecer tu contraseña."
                else 
                    "Hemos enviado un código a ${state.email}. Por favor, ingrésalo a continuación.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(40.dp))

            // Campo de Email
            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = { Text("Correo Electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, null, tint = TurquesaPrincipal) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isCodeSent,
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TurquesaPrincipal,
                    unfocusedBorderColor = GrisSuave.copy(alpha = 0.5f)
                )
            )

            Spacer(Modifier.height(16.dp))

            // Campo de Código (Aparece cuando se envía)
            AnimatedVisibility(visible = state.isCodeSent && !state.isCodeVerified) {
                Column {
                    OutlinedTextField(
                        value = state.code,
                        onValueChange = onCodeChange,
                        label = { Text("Código de Verificación") },
                        leadingIcon = { Icon(Icons.Default.VpnKey, null, tint = TurquesaPrincipal) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TurquesaPrincipal,
                            unfocusedBorderColor = GrisSuave.copy(alpha = 0.5f)
                        )
                    )
                    
                    Spacer(Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (state.isTimerActive) "Vence en: ${state.timerText}" else "Código expirado",
                            fontSize = 12.sp,
                            color = if (state.isTimerActive) TurquesaPrincipal else Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                        
                        if (!state.isTimerActive) {
                            Text(
                                text = "Reenviar código",
                                fontSize = 12.sp,
                                color = TurquesaPrincipal,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onSendCode() }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Botón de Acción Principal
            Button(
                onClick = { if (!state.isCodeSent) onSendCode() else onVerifyCode() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                enabled = !state.isLoading && (
                    (!state.isCodeSent && state.email.isNotBlank()) || 
                    (state.isCodeSent && state.code.isNotBlank())
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = if (!state.isCodeSent) "Enviar Código" else "Verificar Código",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            
            Spacer(Modifier.height(16.dp))
        }
    }
}
