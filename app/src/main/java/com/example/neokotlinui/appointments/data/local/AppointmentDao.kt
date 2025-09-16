package com.example.neokotlinui.appointments.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {

    @Query("SELECT * FROM appointment")
    fun getAllAppointments(): Flow<List<Appointment>>

    @Query("SELECT * FROM appointment WHERE status = :status")
    fun getAppointmentsByStatus(status: String): Flow<List<Appointment>>

    @Query("SELECT * FROM appointment WHERE id = :id LIMIT 1")
    suspend fun getAppointmentById(id: Long): Appointment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment)

    @Update
    suspend fun updateAppointment(appointment: Appointment)

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

    @Query("DELETE FROM appointment WHERE id = :id")
    suspend fun deleteAppointmentById(id: Long)

    @Query("UPDATE appointment SET status = :newStatus WHERE id = :appointmentId")
    suspend fun updateAppointmentStatus(appointmentId: Long, newStatus: String)
}