package com.example.relativelocationapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.relativelocationapp.ui.theme.RelativeLocationAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // Initialize the Firebase repository
    private val firebaseRepository = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RelativeLocationAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        lifecycleScope.launch {
            val sessionName = "session1" // Example session
            addMeasurement(sessionName)
//            addMeasurement(sessionName)
//            fetchMeasurements(sessionName)

//            val newSessionName = "session2" // Example session
//            addMeasurement(newSessionName)
//            fetchMeasurements(newSessionName)
        }
    }

    private suspend fun addMeasurement(session: String) {
        val measurement = MeasurementData(
            distance = 123.45, // Example distance in cm
            angle = 45.0, // Example angle in degrees
            session = session,
            timestamp = System.currentTimeMillis()
        )

        try {
            firebaseRepository.addMeasurement(session, measurement)
            Log.d("MainActivity", "Measurement added successfully!")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error adding measurement: ${e.message}")
        }
    }

    private suspend fun fetchMeasurements(session: String) {
        try {
            val measurements = firebaseRepository.fetchMeasurements(session)
            measurements.forEach { measurement ->
                Log.d("MainActivity", "Fetched measurement: $measurement")
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error fetching measurements: ${e.message}")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RelativeLocationAppTheme {
        Greeting("Android")
    }
}
