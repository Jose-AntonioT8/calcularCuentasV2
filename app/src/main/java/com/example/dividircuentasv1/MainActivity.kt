package com.example.dividircuentasv1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dividircuentasv1.ui.theme.DividirCuentasV1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DividirCuentasV1Theme {
                Calculadora()
            }
        }
    }
}

@Composable
fun Calculadora() {
    var cantidad by remember { mutableStateOf("") }
    var comensales by remember { mutableStateOf("") }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var resultado by remember { mutableStateOf("") }
    var total by remember { mutableStateOf("") }
    var redondearPropina by remember { mutableStateOf(true) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = stringResource(R.string.upperName),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            TextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text(stringResource(R.string.accAmo)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                 .height(56.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = comensales,
                onValueChange = { comensales = it },
                label = { Text(stringResource(R.string.comNum)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                 .height(56.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.RouTip),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = redondearPropina,
                    onCheckedChange = {
                        redondearPropina = it
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.ratPor)+"${sliderPosition.toInt() * 5}%",
                    style = MaterialTheme.typography.bodyLarge
                )
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 0f..5f,
                    steps = 4,
                    enabled = redondearPropina,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            val test : String = stringResource(R.string.invVal)
            FilledTonalButton(
                onClick = {
                    resultado = dividirCuenta(
                        cantidad = cantidad,
                        comensales = comensales,
                        sliderPosition = sliderPosition,
                        propina = redondearPropina,
                        inVal = test
                    )

                    total = totalCuenta(
                        cantidad = cantidad,
                        sliderPosition = sliderPosition,
                        propina = redondearPropina,
                        inVal = test
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(stringResource(R.string.cal))
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (resultado.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.total)+" $total €",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.each)+" $resultado €",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
fun dividirCuenta(
    cantidad: String,
    comensales: String,
    sliderPosition: Float,
    propina: Boolean,
    inVal: String
    ): String {
    val total = cantidad.toFloatOrNull()
    val personas = comensales.toIntOrNull()
    if (total == null || personas == null || personas == 0) {
        return inVal
    }
    val porcentajePropina = sliderPosition * 0.05f
    val totalConPropina : Float = total + (total * porcentajePropina)
    val resultadoPorPersona : Float = totalConPropina / personas
    val resultadoPorPersonaSinPropina : Float = total / personas
    return if (propina) {
        "%.2f".format(resultadoPorPersona)
    } else {
        "%.2f".format(resultadoPorPersonaSinPropina)
    }

}
fun totalCuenta(
    cantidad: String,
    sliderPosition: Float,
    propina: Boolean,
    inVal: String
    ): String {
    val total = cantidad.toFloatOrNull()
    if (total == null ) {
        return inVal
    }
    val porcentajePropina : Float= sliderPosition * 0.05f
    val totalConPropina : Float = total + (total * porcentajePropina)
    return if (propina) {
        "%.2f".format(totalConPropina)
    } else {
        "%.2f".format(total)
    }
}
@Preview(showBackground = true, name = "Light Mode Preview")
@Composable
fun CalculadoraPreviewLight() {
    DividirCuentasV1Theme(darkTheme = false) {
        Calculadora()
    }
}

@Preview(showBackground = true, name = "Dark Mode Preview")
@Composable
fun CalculadoraPreviewDark() {
    DividirCuentasV1Theme(darkTheme = true) {
        Calculadora()
    }
}