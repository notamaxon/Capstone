package com.example.relativelocationapp

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.tasks.await
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

data class MeasurementData(
    val measurementNumber: Int = 0, // Auto-incremented value
    val distance: Double = 0.0, // Distance in cm
    val angle: Double = 0.0, // Angle in degrees
    val session: String = "", // Session ID/Name
    val timestamp: Long = 0L // Timestamp in milliseconds
)

class FirebaseRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val lock = ReentrantLock() // Ensures thread-safe sequential execution

    // Add a measurement to a specific session
    suspend fun addMeasurement(session: String, measurement: MeasurementData) {
        // Fetch the highest measurementNumber outside the lock
        val sessionRef = database.child("measurements").child(session)
        val maxMeasurementNumber = try {
            sessionRef.get().await()
                .children
                .mapNotNull { it.child("measurementNumber").getValue(Int::class.java) }
                .maxOrNull() ?: 0
        } catch (e: Exception) {
            0 // Default to 0 if no measurements exist or query fails
        }

        // Use lock to ensure synchronized access to adding the measurement
        lock.withLock {
            // Create a new measurement with incremented measurementNumber
            val newMeasurement = measurement.copy(measurementNumber = maxMeasurementNumber + 1)

            // Add the measurement to the session folder
            sessionRef.push().setValue(newMeasurement)
        }
    }

    // Fetch all measurements for a specific session
    suspend fun fetchMeasurements(session: String): List<MeasurementData> {
        val sessionRef = database.child("measurements").child(session)
        val snapshot = sessionRef.get().await()
        return snapshot.children.mapNotNull { it.getValue(MeasurementData::class.java) }
    }
}