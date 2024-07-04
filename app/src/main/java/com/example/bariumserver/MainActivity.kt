package com.example.bariumserver

import android.Manifest.permission.READ_SMS
import android.Manifest.permission.RECEIVE_SMS
import android.Manifest.permission.SEND_SMS
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                RECEIVE_SMS,
                READ_SMS,
                SEND_SMS
            )
        )
    }
}

@Composable
fun SmsDetailsScreen(viewModel: SmsViewModel) {
    val smsDetails = viewModel.smsDetails.observeAsState("")

    Column {
        Text(text = "New request")
        Text(text = smsDetails.value)
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
