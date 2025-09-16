package com.example.neokotlinui.appointments.data

import com.example.neokotlinui.appointments.data.local.Appointment
import com.example.neokotlinui.appointments.data.local.AppointmentDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AppointmentRepositoryTest {

    private lateinit var appointmentDao: AppointmentDao
    private lateinit var repository: AppointmentRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Set main dispatcher for coroutines
        appointmentDao = mock()
        repository = AppointmentRepository(appointmentDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset main dispatcher
    }

    @Test
    fun `insertAppointment calls dao insert`() = runTest(testDispatcher) {
        val appointment = Appointment(
            patientFullName = "Test User",
            patientContactNumber = "123",
            patientEmailAddress = "test@example.com",
            speciality = "Test Spec",
            doctorName = "Dr. Test",
            appointmentDateTime = 1L,
            status = "BOOKED"
        )
        repository.insertAppointment(appointment)
        verify(appointmentDao).insert(appointment)
    }

    @Test
    fun `updateAppointmentStatus calls dao updateAppointmentStatus`() = runTest(testDispatcher) {
        val appointmentId = 1L
        val newStatus = "CANCELLED"
        repository.updateAppointmentStatus(appointmentId, newStatus)
        verify(appointmentDao).updateAppointmentStatus(appointmentId, newStatus)
    }

    @Test
    fun `deleteAppointmentById calls dao deleteAppointmentById`() = runTest(testDispatcher) {
        val appointmentId = 1L
        repository.deleteAppointmentById(appointmentId)
        verify(appointmentDao).deleteAppointmentById(appointmentId)
    }

    @Test
    fun `allAppointments returns flow from dao`() = runTest(testDispatcher) {
        val mockAppointments = listOf(
            Appointment(id = 1, patientFullName = "User One", speciality = "Spec1", doctorName = "Doc1", appointmentDateTime = 1L, status = "BOOKED", patientContactNumber = "", patientEmailAddress = ""),
            Appointment(id = 2, patientFullName = "User Two", speciality = "Spec2", doctorName = "Doc2", appointmentDateTime = 2L, status = "COMPLETED", patientContactNumber = "", patientEmailAddress = "")
        )
        // Mock the DAO to return a Flow of these appointments
        whenever(appointmentDao.getAllAppointmentsSortedByDate()).thenReturn(flowOf(mockAppointments))

        val resultFlow = repository.allAppointments
        resultFlow.collect { appointments ->
            assertEquals(2, appointments.size)
            assertEquals("User One", appointments[0].patientFullName)
        }
        // Verify that the DAO method was called
        verify(appointmentDao).getAllAppointmentsSortedByDate()
    }

     @Test
    fun `getAppointmentById calls dao getAppointmentById`() = runTest(testDispatcher) {
        val appointmentId = 1L
        val mockAppointment = Appointment(id = appointmentId, patientFullName = "User One", speciality = "Spec1", doctorName = "Doc1", appointmentDateTime = 1L, status = "BOOKED", patientContactNumber = "", patientEmailAddress = "")

        whenever(appointmentDao.getAppointmentById(appointmentId)).thenReturn(flowOf(mockAppointment))

        val resultFlow = repository.getAppointmentById(appointmentId)
        resultFlow.collect { appointment ->
            assertNotNull(appointment)
            assertEquals(appointmentId, appointment?.id)
        }
        verify(appointmentDao).getAppointmentById(appointmentId)
    }
}
