package com.example.neokotlinui.appointments.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.neokotlinui.appointments.data.AppointmentRepository
import com.example.neokotlinui.appointments.data.local.Appointment
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
import org.mockito.kotlin.any // For any() matcher

@ExperimentalCoroutinesApi
class AppointmentViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule() // For LiveData if directly used, or for main Looper needs

    private lateinit var repository: AppointmentRepository
    private lateinit var viewModel: AppointmentViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        // Mock the behavior of repository.allAppointments to return an empty flow by default for most tests
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
        // ... other params

        viewModel.event.test { // Using Turbine to test events
            viewModel.insertAppointment(
                patientFullName, "123", "a@b.c", "Spec", "Doc", 1L, "BOOKED", null
            )
            verify(repository).insertAppointment(any()) // Use any() or capture the argument if specific checks needed

            val event = awaitItem()
            assertTrue(event is AppointmentEvent.ShowToast)
            assertEquals("Appointment for Test User Booked Successfully.", (event as AppointmentEvent.ShowToast).message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `updateAppointmentStatus calls repository update and posts success event`() = runTest(testDispatcher) {
        val appointmentId = 1L
        val newStatus = "CANCELLED"

        viewModel.event.test {
            viewModel.updateAppointmentStatus(appointmentId, newStatus)
            verify(repository).updateAppointmentStatus(appointmentId, newStatus)

            val event = awaitItem()
            assertTrue(event is AppointmentEvent.ShowToast)
            assertEquals("Appointment status updated to CANCELLED.", (event as AppointmentEvent.ShowToast).message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `deleteAppointmentById calls repository delete and posts success event`() = runTest(testDispatcher) {
        val appointmentId = 1L

        viewModel.event.test {
            viewModel.deleteAppointmentById(appointmentId)
            verify(repository).deleteAppointmentById(appointmentId)

            val event = awaitItem()
            assertTrue(event is AppointmentEvent.ShowToast)
            assertEquals("Appointment deleted.", (event as AppointmentEvent.ShowToast).message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `allAppointments LiveData is updated from repository flow`() = runTest(testDispatcher) {
         val mockAppointments = listOf(
            Appointment(id = 1, patientFullName = "User One", speciality = "Spec1", doctorName = "Doc1", appointmentDateTime = 1L, status = "BOOKED", patientContactNumber = "", patientEmailAddress = "")
        )
        // Override the default mock for this specific test
        whenever(repository.allAppointments).thenReturn(flowOf(mockAppointments))

        // Re-initialize ViewModel to pick up the new mock behavior for allAppointments
        // or ensure the LiveData/StateFlow collection starts after this whenever()
        viewModel = AppointmentViewModel(repository)


        // Observe the LiveData (or collect StateFlow)
        // For LiveData, you might use LiveDataTestUtil.getOrAwaitValue or observe it directly
        // For StateFlow, you can collect it or use Turbine
        viewModel.allAppointments.test {
            val appointments = awaitItem() // First emission from the flow
            assertNotNull(appointments)
            assertEquals(1, appointments.size)
            assertEquals("User One", appointments[0].patientFullName)
            // cancelAndIgnoreRemainingEvents() if you don't expect more in this test
        }
    }
}
