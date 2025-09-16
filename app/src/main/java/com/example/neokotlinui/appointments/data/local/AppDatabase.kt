package com.example.neokotlinui.appointments.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Make sure you have a data class Appointment annotated with @Entity
@Database(entities = [Appointment::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appointment_database"
                )
                .fallbackToDestructiveMigration() // Only use during development
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}