package ni.edu.uam.psyconnect.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.psyconnect.R

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: (String) -> Unit
) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TurquesaFondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo o Icono de la App
        Icon(
            painter = painterResource(id = R.mipmap.ic_launcher_round),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp),
            tint = Color.Unspecified
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Bienvenido de nuevo",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TurquesaOscuro,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Inicia sesión para continuar cuidando tu bienestar",
            fontSize = 14.sp,
            color = GrisTexto,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Campo de Correo / Usuario
        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it },
            label = { Text("Email o Usuario") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TurquesaPrincipal) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TurquesaPrincipal,
                unfocusedBorderColor = GrisSuave.copy(alpha = 0.5f),
                focusedLabelColor = TurquesaPrincipal
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TurquesaPrincipal) },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = GrisSuave
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TurquesaPrincipal,
                unfocusedBorderColor = GrisSuave.copy(alpha = 0.5f),
                focusedLabelColor = TurquesaPrincipal
            ),
            singleLine = true
        )

        // Olvidé mi contraseña
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = TurquesaPrincipal,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .clickable { onForgotPasswordClick(identifier) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Inicio de Sesión
        Button(
            onClick = { onLoginClick(identifier, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TurquesaPrincipal),
            enabled = identifier.isNotBlank() && password.isNotBlank()
        ) {
            Text("Iniciar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Registro
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("¿No tienes una cuenta?", color = GrisTexto, fontSize = 14.sp)
            Text(
                text = " Regístrate",
                color = TurquesaPrincipal,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onRegisterClick() }
            )
        }
    }
}
