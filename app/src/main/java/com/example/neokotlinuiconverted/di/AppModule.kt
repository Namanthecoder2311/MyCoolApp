package com.example.neokotlinuiconverted.di

import com.example.neokotlinui.appointments.data.local.AppDatabase
import com.example.neokotlinui.appointments.data.local.AppointmentDao
import com.example.neokotlinui.appointments.data.repository.AppointmentRepository
import com.example.neokotlinui.appointments.data.repository.IAppointmentRepository
import com.example.neokotlinui.appointments.viewmodel.AppointmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // ViewModel definition
    viewModel { AppointmentViewModel(repository = get()) }

    // Repository definition: Provide AppointmentRepository for IAppointmentRepository
    // Koin will automatically resolve the AppointmentDao dependency from the definition below.
    single<IAppointmentRepository> { AppointmentRepository(appointmentDao = get()) }

    // DAO definition: Provide AppointmentDao from AppDatabase
    single<AppointmentDao> { AppDatabase.getDatabase(androidApplication()).appointmentDao() }

    // Database definition (optional, but good for explicitness if you need AppDatabase itself elsewhere)
    // If you only need the DAO, the line above is sufficient.
    // single<AppDatabase> { AppDatabase.getDatabase(androidApplication()) }
}
