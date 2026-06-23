package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.psyconnect.ui.viewmodel.ResetPasswordUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    state: ResetPasswordUiState,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nueva Contraseña", fontWeight = FontWeight.Bold, color = TurquesaOscuro) },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(Modifier.height(24.dp)) }

            item {
                Text(
                    text = "🔑",
                    fontSize = 64.sp
                )
            }

            item {
                Text(
                    text = "Crea tu nueva contraseña",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TurquesaOscuro,
                    textAlign = TextAlign.Center
                )
            }

            item {
                Text(
                    text = "Asegúrate de que sea segura y fácil de recordar para ti.",
                    fontSize = 14.sp,
                    color = GrisTexto,
                    textAlign = TextAlign.Center
                )
            }

            item { Spacer(Modifier.height(16.dp)) }

            // Nueva Contraseña
            item {
                Column {
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = onPasswordChange,
                        label = { Text("Nueva Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = TurquesaPrincipal) },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TurquesaPrincipal),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    PasswordStrengthIndicator(state.password)
                }
            }

            // Confirmar Contraseña
            item {
                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = { Text("Confirmar Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = TurquesaPrincipal) },
                    trailingIcon = {
                        IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                            Icon(if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                        }
                    },
                    visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TurquesaPrincipal),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }

            item { Spacer(Modifier.weight(1f)) }

            // Botón de Guardar
            item {
                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TurquesaPrincipal),
                    enabled = !state.isLoading && 
                             state.password.isNotBlank() && 
                             state.password == state.confirmPassword &&
                             calculatePasswordStrength(state.password) >= 0.8f
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Actualizar Contraseña", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
            
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}
