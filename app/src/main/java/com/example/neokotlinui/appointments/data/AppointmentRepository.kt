package com.example.neokotlinui.appointments.data

import com.example.neokotlinui.appointments.model.Appointment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class AppointmentRepository {

    private val items = mutableListOf<Appointment>()
    private val state = MutableStateFlow<List<Appointment>>(emptyList())

    val allAppointments: Flow<List<Appointment>> = state.asStateFlow()

    suspend fun insertAppointment(appointment: Appointment) {
        val id = if (appointment.id == 0L) (items.maxOfOrNull { it.id } ?: 0L) + 1L else appointment.id
        val a = appointment.copy(id = id)
        items.add(a)
        state.value = items.toList() // Ensure a new list is emitted for StateFlow to update
    }

    suspend fun updateAppointmentStatus(appointmentId: Long, status: String) {
        val idx = items.indexOfFirst { it.id == appointmentId }
        if (idx >= 0) {
            items[idx] = items[idx].copy(status = status)
            state.value = items.toList() // Emit a new list
        }
    }

    suspend fun deleteAppointmentById(appointmentId: Long) {
        items.removeAll { it.id == appointmentId }
        state.value = items.toList() // Emit a new list
    }

    fun getAppointmentById(appointmentId: Long): Flow<Appointment?> =
        state.map { list -> list.firstOrNull { it.id == appointmentId } }
}