package com.example.neokotlinui.appointments.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow // <<< ADD THIS IMPORT
import app.cash.turbine.test
import com.example.neokotlinui.appointments.data.AppointmentRepository
import com.example.neokotlinui.appointments.model.Appointment
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
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class AppointmentViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: AppointmentRepository
    private lateinit var viewModel: AppointmentViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        whenever(repository.allAppointments).thenReturn(flowOf(emptyList()))
        viewModel = AppointmentViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `insertAppointment calls repository insert and posts success event`() = runTest(testDispatcher) {
        val patientFullName = "Test User"

        viewModel.event.asFlow().test { // <<< USE ASFLOW() HERE
            viewModel.insertAppointment(
                patientFullName, "123", "a@b.c", "Spec", "Doc", 1L, "BOOKED", null
            )
            verify(repository).insertAppointment(any())

            val event = awaitItem()
            assertTrue(event is AppointmentEvent.ShowToast)
            assertEquals("Appointment Booked Successfully", (event as AppointmentEvent.ShowToast).message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `updateAppointmentStatus calls repository update and posts success event`() = runTest(testDispatcher) {
        val appointmentId = 1L
        val newStatus = "CANCELLED"

        viewModel.event.asFlow().test { // <<< USE ASFLOW() HERE
            viewModel.updateAppointmentStatus(appointmentId, newStatus)
            verify(repository).updateAppointmentStatus(appointmentId, newStatus)

            val event = awaitItem()
            assertTrue(event is AppointmentEvent.ShowToast)
            assertEquals("Appointment status updated.", (event as AppointmentEvent.ShowToast).message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `deleteAppointmentById calls repository delete and posts success event`() = runTest(testDispatcher) {
        val appointmentId = 1L

        viewModel.event.asFlow().test { // <<< USE ASFLOW() HERE
            viewModel.deleteAppointmentById(appointmentId)
            verify(repository).deleteAppointmentById(appointmentId)

            val event = awaitItem()
            assertTrue(event is AppointmentEvent.ShowToast)
            assertEquals("Appointment deleted by ID.", (event as AppointmentEvent.ShowToast).message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `allAppointments LiveData is updated from repository flow`() = runTest(testDispatcher) {
         val mockAppointments = listOf(
            Appointment(id = 1, patientFullName = "User One", speciality = "Spec1", doctorName = "Doc1", appointmentDateTime = 1L, status = "BOOKED", patientContactNumber = "", patientEmailAddress = "")
        )
        whenever(repository.allAppointments).thenReturn(flowOf(mockAppointments))
        viewModel = AppointmentViewModel(repository) // Re-initialize to pick up new flow

        viewModel.allAppointments.asFlow().test { // <<< USE ASFLOW() HERE
            val appointments = awaitItem()
            assertNotNull(appointments)
            assertEquals(1, appointments.size)
            assertEquals("User One", appointments[0].patientFullName)
            cancelAndConsumeRemainingEvents() // Important if the LiveData might emit an initial empty list then the actual data
        }
    }
}
