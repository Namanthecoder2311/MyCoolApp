package com.example.neokotlinui.appointments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.neokotlinui.appointments.model.Appointment // Updated import
import com.example.neokotlinui.appointments.data.AppointmentRepository // Updated import
import kotlinx.coroutines.launch

// Define possible UI events (for one-time actions like showing a toast)
sealed class AppointmentEvent {
    data class ShowToast(val message: String) : AppointmentEvent()
    // Add other events like navigation if needed
}

class AppointmentViewModel(private val repository: AppointmentRepository) : ViewModel() { // Updated constructor

    // Expose a Flow of appointments as LiveData
    val allAppointments: LiveData<List<Appointment>> = repository.allAppointments.asLiveData() // Changed to use allAppointments from refactored repo

    // For one-time events
    private val _event = MutableLiveData<AppointmentEvent>()
    val event: LiveData<AppointmentEvent> = _event

    fun insertAppointment(
        patientFullName: String,
        patientContactNumber: String,
        patientEmailAddress: String,
        speciality: String,
        doctorName: String,
        appointmentDateTime: Long, // Epoch time
        status: String,
        notes: String? // This parameter is now unused for Appointment creation
    ) {
        if (patientFullName.isBlank() || speciality.isBlank() || doctorName.isBlank() || status.isBlank()) {
            _event.value = AppointmentEvent.ShowToast("Required fields are missing.")
            return
        }

        val newAppointment = Appointment(
            patientFullName = patientFullName,
            patientContactNumber = patientContactNumber,
            patientEmailAddress = patientEmailAddress,
            speciality = speciality,
            doctorName = doctorName,
            appointmentDateTime = appointmentDateTime,
            status = status
            // 'notes' field removed as it's not in the new model.Appointment
        )
        viewModelScope.launch {
            repository.insertAppointment(newAppointment)
            _event.value = AppointmentEvent.ShowToast("Appointment Booked Successfully")
        }
    }

    fun updateAppointmentStatus(appointmentId: Long, newStatus: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(appointmentId, newStatus)
            _event.value = AppointmentEvent.ShowToast("Appointment status updated.")
        }
    }

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repository.deleteAppointmentById(appointment.id) // Changed to use deleteAppointmentById
            _event.value = AppointmentEvent.ShowToast("Appointment deleted.")
        }
    }

    fun deleteAppointmentById(appointmentId: Long) {
        viewModelScope.launch {
            repository.deleteAppointmentById(appointmentId)
            _event.value = AppointmentEvent.ShowToast("Appointment deleted by ID.")
        }
    }
}
