package com.example.neokotlinui.appointments.data.repository

import com.example.neokotlinui.appointments.data.local.Appointment
import com.example.neokotlinui.appointments.data.local.AppointmentDao
import kotlinx.coroutines.flow.Flow

// Interface for the repository (optional, but good practice for testability/abstraction)
interface IAppointmentRepository {
    fun getAllAppointments(): Flow<List<Appointment>>
    fun getAppointmentsByStatus(status: String): Flow<List<Appointment>>
    suspend fun getAppointmentById(id: Long): Appointment?
    suspend fun insertAppointment(appointment: Appointment)
    suspend fun updateAppointment(appointment: Appointment)
    suspend fun deleteAppointment(appointment: Appointment)
    suspend fun deleteAppointmentById(id: Long)
    suspend fun updateAppointmentStatus(appointmentId: Long, newStatus: String)
}

// Implementation of the repository
class AppointmentRepository(private val appointmentDao: AppointmentDao) : IAppointmentRepository {

    override fun getAllAppointments(): Flow<List<Appointment>> {
        return appointmentDao.getAllAppointments()
    }

    override fun getAppointmentsByStatus(status: String): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsByStatus(status)
    }

    override suspend fun getAppointmentById(id: Long): Appointment? {
        return appointmentDao.getAppointmentById(id)
    }

    override suspend fun insertAppointment(appointment: Appointment) {
        appointmentDao.insertAppointment(appointment)
    }

    override suspend fun updateAppointment(appointment: Appointment) {
        appointmentDao.updateAppointment(appointment)
    }

    override suspend fun deleteAppointment(appointment: Appointment) {
        appointmentDao.deleteAppointment(appointment)
    }

    override suspend fun deleteAppointmentById(id: Long) {
        appointmentDao.deleteAppointmentById(id)
    }

    override suspend fun updateAppointmentStatus(appointmentId: Long, newStatus: String) {
        appointmentDao.updateAppointmentStatus(appointmentId, newStatus)
    }
}
