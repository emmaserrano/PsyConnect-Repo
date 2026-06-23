package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.psyconnect.ui.viewmodel.RegisterUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onBirthdateChange: (String) -> Unit,
    onTermsChange: (Boolean) -> Unit,
    onVerificationCodeChange: (String) -> Unit,
    onSendCode: () -> Unit,
    onVerifyCode: () -> Unit,
    onRegister: () -> Unit,
    onBack: () -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val showDatePicker = remember { mutableStateOf(false) }

    if (showDatePicker.value) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                        onBirthdateChange(date)
                    }
                    showDatePicker.value = false
                }) { Text("Confirmar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Crear Cuenta", fontWeight = FontWeight.Bold, color = TurquesaOscuro) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)) }

            // Nombre Completo
            item {
                RegisterTextField(
                    value = state.name,
                    onValueChange = onNameChange,
                    label = "Nombre Completo",
                    icon = Icons.Default.Person
                )
            }

            // Nombre de Usuario
            item {
                Column {
                    RegisterTextField(
                        value = state.username,
                        onValueChange = onUsernameChange,
                        label = "Nombre de Usuario",
                        icon = Icons.Default.AccountCircle
                    )
                    if (state.username.isNotEmpty()) {
                        state.isUsernameAvailable?.let { available ->
                            Text(
                                text = if (available) "✅ Usuario disponible" else "❌ Usuario ocupado",
                                color = if (available) Color(0xFF2E7D32) else Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Email
            item {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RegisterTextField(
                            value = state.email,
                            onValueChange = onEmailChange,
                            label = "Correo Electrónico",
                            icon = Icons.Default.Email,
                            enabled = !state.isEmailVerified,
                            modifier = Modifier.weight(1f)
                        )
                        if (!state.isEmailVerified && state.isEmailAvailable == true) {
                            TextButton(onClick = onSendCode, enabled = !state.isLoading && !state.isTimerActive) {
                                Text(if (state.isTimerActive) state.timerText else "Enviar", color = TurquesaPrincipal)
                            }
                        }
                    }
                    if (state.email.isNotEmpty()) {
                        state.isEmailAvailable?.let { available ->
                            Text(
                                text = if (available) "✅ Correo disponible" else "❌ Correo ya registrado",
                                color = if (available) Color(0xFF2E7D32) else Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Código de Verificación
            if (state.codeSent && !state.isEmailVerified) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RegisterTextField(
                            value = state.verificationCode,
                            onValueChange = onVerificationCodeChange,
                            label = "Código",
                            icon = Icons.Default.VpnKey,
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Number
                        )
                        Button(
                            onClick = onVerifyCode,
                            enabled = !state.isLoading && state.verificationCode.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = TurquesaPrincipal),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text("Validar")
                        }
                    }
                }
            }

            // Fecha de Nacimiento
            item {
                OutlinedTextField(
                    value = state.birthdate,
                    onValueChange = {},
                    label = { Text("Fecha de Nacimiento") },
                    leadingIcon = { Icon(Icons.Default.DateRange, null, tint = TurquesaPrincipal) },
                    modifier = Modifier.fillMaxWidth().clickable { showDatePicker.value = true },
                    enabled = false,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = GrisSuave.copy(alpha = 0.5f),
                        disabledLabelColor = GrisTexto,
                        disabledTextColor = Color.Black
                    )
                )
            }

            // Contraseña con Requisitos
            item {
                Column {
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = onPasswordChange,
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = TurquesaPrincipal) },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TurquesaPrincipal)
                    )
                    
                    if (state.password.isNotEmpty()) {
                        PasswordRequirementsView(state.password)
                    }
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = state.termsAccepted,
                        onCheckedChange = onTermsChange,
                        colors = CheckboxDefaults.colors(checkedColor = TurquesaPrincipal)
                    )
                    Text("Acepto los términos y condiciones", fontSize = 14.sp, color = GrisTexto)
                }
            }

            item {
                Button(
                    onClick = onRegister,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TurquesaPrincipal),
                    enabled = isRegisterEnabled(state)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Registrarse", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun PasswordRequirementsView(password: String) {
    val requirements = listOf(
        "Mínimo 8 caracteres" to (password.length >= 8),
        "Una mayúscula" to password.any { it.isUpperCase() },
        "Una minúscula" to password.any { it.isLowerCase() },
        "Un número" to password.any { it.isDigit() },
        "Un carácter especial" to password.any { !it.isLetterOrDigit() }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 8.dp)
    ) {
        requirements.forEach { (text, met) ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                Text(
                    text = if (met) "✔" else "✖",
                    color = if (met) Color(0xFF2E7D32) else Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = text,
                    color = if (met) Color(0xFF2E7D32) else Color.Red,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null, tint = TurquesaPrincipal) },
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TurquesaPrincipal,
            unfocusedBorderColor = GrisSuave.copy(alpha = 0.5f)
        )
    )
}

fun isRegisterEnabled(state: RegisterUiState): Boolean {
    val passwordMet = state.password.length >= 8 &&
            state.password.any { it.isUpperCase() } &&
            state.password.any { it.isLowerCase() } &&
            state.password.any { it.isDigit() } &&
            state.password.any { !it.isLetterOrDigit() }

    return state.name.isNotBlank() &&
            state.username.isNotBlank() &&
            state.isUsernameAvailable == true &&
            state.email.isNotBlank() &&
            state.isEmailAvailable == true &&
            state.isEmailVerified &&
            passwordMet &&
            state.birthdate.isNotBlank() &&
            state.termsAccepted &&
            !state.isLoading
}
