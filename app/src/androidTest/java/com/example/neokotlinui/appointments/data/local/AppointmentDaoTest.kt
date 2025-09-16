package com.example.neokotlinui.appointments.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.neokotlinui.getOrAwaitValue // Utility for LiveData testing
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AppointmentDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule() // For LiveData

    private lateinit var database: AppDatabase
    private lateinit var appointmentDao: AppointmentDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // Only for testing
            .build()
        appointmentDao = database.appointmentDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetAppointmentById() = runTest {
        val appointment = Appointment(
            id = 1,
            patientFullName = "John Doe",
            patientContactNumber = "1234567890",
            patientEmailAddress = "john.doe@example.com",
            speciality = "Cardiology",
            doctorName = "Dr. Smith",
            appointmentDateTime = System.currentTimeMillis(),
            status = "BOOKED",
            notes = "Test notes"
        )
        appointmentDao.insert(appointment)

        val retrievedAppointment = appointmentDao.getAppointmentById(1).first() // Using Flow.first()
        assertNotNull(retrievedAppointment)
        assertEquals(appointment.patientFullName, retrievedAppointment?.patientFullName)
    }

    @Test
    @Throws(Exception::class)
    fun getAllAppointments() = runTest {
        val appointment1 = Appointment(patientFullName = "Jane Doe", speciality = "Dental", doctorName = "Dr. Tooth", appointmentDateTime = System.currentTimeMillis() + 1000, status = "BOOKED", patientContactNumber = "", patientEmailAddress = "")
        val appointment2 = Appointment(patientFullName = "Peter Pan", speciality = "Pediatrics", doctorName = "Dr. Child", appointmentDateTime = System.currentTimeMillis() + 2000, status = "BOOKED", patientContactNumber = "", patientEmailAddress = "")
        appointmentDao.insert(appointment1)
        appointmentDao.insert(appointment2)

        val allAppointments = appointmentDao.getAllAppointmentsSortedByDate().first() // Using Flow.first()
        assertEquals(2, allAppointments.size)
        // Add more assertions based on order or content if needed
    }

    @Test
    @Throws(Exception::class)
    fun updateAppointmentStatus() = runTest {
        val appointment = Appointment(id = 1, patientFullName = "Update Test", speciality = "Ortho", doctorName = "Dr. Bone", appointmentDateTime = System.currentTimeMillis(), status = "BOOKED", patientContactNumber = "", patientEmailAddress = "")
        appointmentDao.insert(appointment)

        appointmentDao.updateAppointmentStatus(1, "CANCELLED")
        val updatedAppointment = appointmentDao.getAppointmentById(1).first()
        assertNotNull(updatedAppointment)
        assertEquals("CANCELLED", updatedAppointment?.status)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAppointment() = runTest {
        val appointment = Appointment(id = 1, patientFullName = "Delete Test", speciality = "Neuro", doctorName = "Dr. Brain", appointmentDateTime = System.currentTimeMillis(), status = "BOOKED", patientContactNumber = "", patientEmailAddress = "")
        appointmentDao.insert(appointment)
        assertNotNull(appointmentDao.getAppointmentById(1).first())

        appointmentDao.delete(appointment) // Assuming you have a direct delete(Appointment) method
                                         // If you only have deleteById, adjust this test
        assertNull(appointmentDao.getAppointmentById(1).first())
    }

    @Test
    @Throws(Exception::class)
    fun deleteAppointmentById() = runTest {
        val appointment = Appointment(id = 1, patientFullName = "Delete ID Test", speciality = "General", doctorName = "Dr. General", appointmentDateTime = System.currentTimeMillis(), status = "BOOKED", patientContactNumber = "", patientEmailAddress = "")
        appointmentDao.insert(appointment)
        assertNotNull(appointmentDao.getAppointmentById(1).first())

        appointmentDao.deleteAppointmentById(1)
        assertNull(appointmentDao.getAppointmentById(1).first())
    }
}
