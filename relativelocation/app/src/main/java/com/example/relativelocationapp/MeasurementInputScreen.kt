package com.example.relativelocationapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Angles in degrees
val ANGLES = listOf(
    0, 30, 60, 90, 120, 150,
    180, 210, 240, 270, 300, 330
)

// Distances in meters
val DISTANCES = listOf(0.5, 1.0, 2.0, 3.0, 5.0, 8.0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasurementInputScreen(
    modifier: Modifier = Modifier
) {
    var selectedDistance by remember { mutableStateOf(DISTANCES[0]) }
    var selectedAngle by remember { mutableStateOf(ANGLES[0]) }
    var sessionId by remember { mutableStateOf("Session_${System.currentTimeMillis()}") }
    var measurements by remember { mutableStateOf(listOf<String>()) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            text = "Relative Location Measurement",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Session Management
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Session ID:",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = sessionId,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    sessionId = "Session_${System.currentTimeMillis()}"
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Reset Session"
                )
            }
        }

        // Distance Dropdown
        var distanceExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = distanceExpanded,
            onExpandedChange = { distanceExpanded = !distanceExpanded }
        ) {
            TextField(
                value = "$selectedDistance m",
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Distance") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = distanceExpanded
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = distanceExpanded,
                onDismissRequest = { distanceExpanded = false }
            ) {
                DISTANCES.forEach { distance ->
                    DropdownMenuItem(
                        text = { Text("$distance m") },
                        onClick = {
                            selectedDistance = distance
                            distanceExpanded = false
                        }
                    )
                }
            }
        }

        // Angle Dropdown
        var angleExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = angleExpanded,
            onExpandedChange = { angleExpanded = !angleExpanded }
        ) {
            TextField(
                value = "$selectedAngle°",
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Angle") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = angleExpanded
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = angleExpanded,
                onDismissRequest = { angleExpanded = false }
            ) {
                ANGLES.forEach { angle ->
                    DropdownMenuItem(
                        text = { Text("$angle°") },
                        onClick = {
                            selectedAngle = angle
                            angleExpanded = false
                        }
                    )
                }
            }
        }

        // Start Measurement Button
        Button(
            onClick = {
                val timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

                val measurementLabel = "[$sessionId] Distance: $selectedDistance m, Angle: $selectedAngle°, Time: $timestamp"
                measurements = measurements + measurementLabel
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Measurement")
        }

        // Clear Measurements Button
        Button(
            onClick = { measurements = emptyList() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear Measurements")
        }

        // Measurements Section
        Text(
            text = "Measurements:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Measurements List
        if (measurements.isEmpty()) {
            Text(
                text = "No measurements recorded",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        } else {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                measurements.reversed().forEachIndexed { index, measurement ->
                    Text(
                        text = "${index + 1}. $measurement",
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}