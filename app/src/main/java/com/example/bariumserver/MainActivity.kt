package com.example.bariumserver

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(smsDetailsList.value) { smsDetail ->
            SmsDetailItem(smsDetail)
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}

@Composable
fun SmsDetailItem(smsDetail: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = smsDetail)
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
