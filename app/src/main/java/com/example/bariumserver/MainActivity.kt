package com.example.bariumserver

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bariumserver.ui.theme.BariumServerTheme

class MainActivity : ComponentActivity() {

    private val smsViewModel: SmsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ViewModelProvider singleton
        SmsViewModelProvider.initialize(smsViewModel)

        requestSmsPermissions()

        setContent {
            BariumServerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SmsDetailsScreen(smsViewModel)
                }
            }
        }
    }

    private fun requestSmsPermissions() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions.entries.all { it.value == true }
            if (granted) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }

        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS
            )
        )
    }
}

@Composable
fun SmsDetailsScreen(viewModel: SmsViewModel) {
    val smsDetailsList = viewModel.smsDetailsList.observeAsState(emptyList())

    if (smsDetailsList.value.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 25.dp), // Add padding to the top of the screen
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No requests yet",
                fontSize = 20.sp,
                color = Color.Gray
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Adjust gap between cards
        ) {
            items(smsDetailsList.value) { smsDetail ->
                SmsDetailItem(smsDetail)
            }
        }
    }
}

@Composable
fun SmsDetailItem(smsDetail: String) {
    // Extract information using regular expressions
    val phoneNumberRegex = "Phone number: ([\\d+]+)".toRegex()
    val signalStrengthRegex = "Signal Strength: ([\\-\\d]+ dBm)".toRegex()
    val technologyRegex = "Technology: (\\w+)".toRegex()
    val locationLatRegex = "Location: Lat: ([\\d.]+)".toRegex()
    val locationLongRegex = "Long: ([\\d.]+)".toRegex()

    val phoneNumber = phoneNumberRegex.find(smsDetail)?.groups?.get(1)?.value ?: "N/A"
    val signalStrength = signalStrengthRegex.find(smsDetail)?.groups?.get(1)?.value ?: "N/A"
    val technology = technologyRegex.find(smsDetail)?.groups?.get(1)?.value ?: "N/A"
    val latitude = locationLatRegex.find(smsDetail)?.groups?.get(1)?.value ?: "N/A"
    val longitude = locationLongRegex.find(smsDetail)?.groups?.get(1)?.value ?: "N/A"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        border = BorderStroke(2.dp, Color.Gray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "New request",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(bottom = 8.dp))
            Column {
                SmsDetailRow(label = "Phone Number:", value = phoneNumber)
                SmsDetailRow(label = "Signal Strength:", value = signalStrength)
                SmsDetailRow(label = "Technology:", value = technology)
                SmsDetailRow(label = "Location Lat:", value = latitude)
                SmsDetailRow(label = "Location Long:", value = longitude)
            }
        }
    }
}

@Composable
fun SmsDetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val viewModel = SmsViewModel()
    BariumServerTheme {
        SmsDetailsScreen(viewModel)
    }
}
