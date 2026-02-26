package com.example.thecostofelectricity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thecostofelectricity.ui.theme.THECOSTOFELECTRICITYTheme
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            THECOSTOFELECTRICITYTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ElectricityCostCalculator(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ElectricityCostCalculator(modifier: Modifier = Modifier) {
    var consumptionInput by remember { mutableStateOf("1000") }
    var pricePerKwh by remember { mutableFloatStateOf(0.25f) }
    var isVatReduced by remember { mutableStateOf(true) }

    val consumption = consumptionInput.toDoubleOrNull() ?: 0.0
    // Normal VAT is 24%, reduced is 10% if checkbox is selected
    val vatRate = if (isVatReduced) 0.10 else 0.24
    
    // Calculate cost and payment
    val cost = consumption * pricePerKwh
    val totalPayment = cost * (1 + vatRate)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Cost of electricity",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = consumptionInput,
            onValueChange = { consumptionInput = it },
            label = { Text("Consumption in kWh") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Column {
            Text(
                text = "Price/kWh ${String.format(Locale.US, "%.2f", pricePerKwh)}",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Slider(
                value = pricePerKwh,
                onValueChange = { pricePerKwh = it },
                valueRange = 0f..1.0f,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isVatReduced,
                onCheckedChange = { isVatReduced = it }
            )
            Text(text = "VAT 10%")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color(0xFF3F51B5), // Indigo blue color as seen in the screenshot
                modifier = Modifier.size(width = 100.dp, height = 60.dp),
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "${totalPayment.roundToInt()}€",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    THECOSTOFELECTRICITYTheme {
        ElectricityCostCalculator()
    }
}
