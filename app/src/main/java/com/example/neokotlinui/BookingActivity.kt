package com.example.neokotlinui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.neokotlinui.appointments.ui.MyAppointmentsActivity
import com.example.neokotlinui.appointments.viewmodel.AppointmentEvent
import com.example.neokotlinui.appointments.viewmodel.AppointmentViewModel
import com.example.neokotlinuiconverted.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel // Added Koin import
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
// Removed import kotlin.properties.ReadOnlyProperty as it's no longer needed


class BookingActivity : AppCompatActivity() {

    // ViewModel - Now correctly uses Koin's delegate
    private val appointmentViewModel: AppointmentViewModel by viewModel()

    // Declare TextInputLayouts
    private lateinit var tilFullName: TextInputLayout
    private lateinit var tilContactNumber: TextInputLayout
    private lateinit var tilEmailAddress: TextInputLayout
    private lateinit var tilSpeciality: TextInputLayout
    private lateinit var tilPreferredDoctor: TextInputLayout
    private lateinit var tilPreferredDate: TextInputLayout
    private lateinit var tilPreferredTime: TextInputLayout

    // Declare EditTexts
    private lateinit var etFullName: TextInputEditText
    private lateinit var etContactNumber: TextInputEditText
    private lateinit var etEmailAddress: TextInputEditText
    private lateinit var etMedicalConcern: TextInputEditText
    private lateinit var etSpeciality: AutoCompleteTextView
    private lateinit var etPreferredDoctor: AutoCompleteTextView
    private lateinit var etPreferredDate: TextInputEditText
    private lateinit var etPreferredTime: AutoCompleteTextView

    // Date formats
    private val displayDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    private val dateTimeParseFormat = SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        // Initialize Views
        initializeViews()

        val backButton: ImageView = findViewById(R.id.iv_booking_back_button)
        backButton.setOnClickListener {
            finish()
        }

        val btnBookAppointment: MaterialButton = findViewById(R.id.btn_book_appointment)

        setupSpinnersAndPickers()
        setupViewModelObservers()

        btnBookAppointment.setOnClickListener {
            if (validateInputs()) {
                processBooking()
            }
        }
    }

    private fun initializeViews() {
        tilFullName = findViewById(R.id.til_full_name)
        tilContactNumber = findViewById(R.id.til_contact_number)
        tilEmailAddress = findViewById(R.id.til_email_address)
        tilSpeciality = findViewById(R.id.til_speciality)
        tilPreferredDoctor = findViewById(R.id.til_preferred_doctor)
        tilPreferredDate = findViewById(R.id.til_preferred_date)
        tilPreferredTime = findViewById(R.id.til_preferred_time)

        etFullName = findViewById(R.id.et_full_name)
        etContactNumber = findViewById(R.id.et_contact_number)
        etEmailAddress = findViewById(R.id.et_email_address)
        etMedicalConcern = findViewById(R.id.et_medical_concern)
        etSpeciality = findViewById(R.id.et_speciality)
        etPreferredDoctor = findViewById(R.id.et_preferred_doctor)
        etPreferredDate = findViewById(R.id.et_preferred_date)
        etPreferredTime = findViewById(R.id.et_preferred_time)
    }


    private fun setupViewModelObservers() {
        appointmentViewModel.event.observe(this) { event ->
            when (event) {
                is AppointmentEvent.ShowToast -> {
                    Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
                    if (event.message.contains("Successfully")) { // Check if it's the success message
                        showBookingConfirmationDialog()
                    }
                }
                // Handle other events if any
            }
        }
    }

    private fun processBooking() {
        val fullName = etFullName.text.toString()
        val contactNumber = etContactNumber.text.toString()
        val emailAddress = etEmailAddress.text.toString()
        val medicalConcernText = etMedicalConcern.text.toString()
        val speciality = etSpeciality.text.toString()
        val preferredDoctor = etPreferredDoctor.text.toString()
        val preferredDateStr = etPreferredDate.text.toString()
        val preferredTimeStr = etPreferredTime.text.toString()

        val appointmentDateTimeMillis = parseDateTimeToMillis(preferredDateStr, preferredTimeStr)

        if (appointmentDateTimeMillis == null) {
            Toast.makeText(this, "Invalid date or time format.", Toast.LENGTH_LONG).show()
            // Optionally set error on date/time fields
            tilPreferredDate.error = "Invalid date/time" // Consider using string resources
            tilPreferredTime.error = "Invalid date/time" // Consider using string resources
            return
        }

        appointmentViewModel.insertAppointment(
            patientFullName = fullName,
            patientContactNumber = contactNumber,
            patientEmailAddress = emailAddress,
            speciality = speciality,
            doctorName = preferredDoctor,
            appointmentDateTime = appointmentDateTimeMillis,
            status = "BOOKED", // Default status
            notes = if (medicalConcernText.isBlank()) null else medicalConcernText
        )
    }

    private fun parseDateTimeToMillis(dateStr: String, timeStr: String): Long? {
        if (dateStr.isBlank() || timeStr.isBlank()) {
            return null
        }
        return try {
            // It's good practice to set the time zone for parsing if it might differ from the device's default.
            // For simplicity, using default here, but consider UTC or a specific app-wide timezone.
            // dateTimeParseFormat.timeZone = TimeZone.getTimeZone("UTC") // Example for UTC
            val dateTimeString = "$dateStr $timeStr"
            val date = dateTimeParseFormat.parse(dateTimeString)
            date?.time
        } catch (e: ParseException) {
            Log.e("BookingActivity", "Error parsing date/time: $dateStr $timeStr", e)
            null
        }
    }


    private fun setupSpinnersAndPickers() {
        val specialities = listOf("General Dentistry", "Orthodontics", "Oral Surgery", "Periodontics", "Cosmetic Dentistry", "Pediatric Dentistry", "Endodontics", "Prosthodontics")
        val specialityAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, specialities)
        etSpeciality.setAdapter(specialityAdapter)

        etPreferredDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val selectedDate = Calendar.getInstance().apply {
                        set(selectedYear, selectedMonth, selectedDayOfMonth)
                    }
                    etPreferredDate.setText(displayDateFormat.format(selectedDate.time))
                    tilPreferredDate.error = null
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }

        val doctors = listOf("Dr. Emily Carter (General Dentistry)", "Dr. Johnathan Lee (Orthodontics)", "Dr. Sarah Green (Oral Surgery)", "Dr. Michael Blue (Pediatric Dentistry)")
        val doctorAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, doctors)
        etPreferredDoctor.setAdapter(doctorAdapter)

        val timeSlots = mutableListOf<String>().apply {
            val cal = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 9); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0) }
            val endCal = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 17); set(Calendar.MINUTE, 0) }
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
            while (cal.before(endCal)) {
                add(timeFormat.format(cal.time))
                cal.add(Calendar.MINUTE, 30)
            }
        }
        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, timeSlots)
        etPreferredTime.setAdapter(timeAdapter)

        etSpeciality.setOnItemClickListener { _, _, _, _ -> tilSpeciality.error = null }
        etPreferredDoctor.setOnItemClickListener { _, _, _, _ -> tilPreferredDoctor.error = null }
        etPreferredTime.setOnItemClickListener { _, _, _, _ -> tilPreferredTime.error = null }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        // Clear previous errors
        tilFullName.error = null
        tilContactNumber.error = null
        tilEmailAddress.error = null
        tilSpeciality.error = null
        tilPreferredDoctor.error = null
        tilPreferredDate.error = null
        tilPreferredTime.error = null

        // Consider moving these hardcoded strings to strings.xml for localization
        if (TextUtils.isEmpty(etFullName.text)) { tilFullName.error = "Full Name is required"; isValid = false }
        if (TextUtils.isEmpty(etContactNumber.text)) { tilContactNumber.error = "Contact Number is required"; isValid = false }
        if (TextUtils.isEmpty(etEmailAddress.text)) {
            tilEmailAddress.error = "Email Address is required"; isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailAddress.text.toString()).matches()) {
            tilEmailAddress.error = "Enter a valid email address"; isValid = false
        }
        if (TextUtils.isEmpty(etSpeciality.text)) { tilSpeciality.error = "Speciality is required"; isValid = false }
        if (TextUtils.isEmpty(etPreferredDoctor.text)) { tilPreferredDoctor.error = "Preferred Doctor is required"; isValid = false }
        if (TextUtils.isEmpty(etPreferredDate.text)) { tilPreferredDate.error = "Preferred Date is required"; isValid = false }
        if (TextUtils.isEmpty(etPreferredTime.text)) { tilPreferredTime.error = "Preferred Time is required"; isValid = false }
        return isValid
    }

    private fun showBookingConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.booking_success_title))
            .setMessage(getString(R.string.booking_success_message))
            .setPositiveButton(getString(R.string.view_appointments_button)) { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this, MyAppointmentsActivity::class.java))
                finish() // Optional: finish BookingActivity
            }
            .setNegativeButton(getString(R.string.ok_button)) { dialog, _ ->
                dialog.dismiss()
                clearForm() // Optional: clear form after booking
                // finish() // Or finish, depending on desired flow
            }
            .setCancelable(false) // Prevent dismissing by clicking outside
            .show()
    }

    private fun clearForm() {
        etFullName.text = null
        etContactNumber.text = null
        etEmailAddress.text = null
        etMedicalConcern.text = null
        etSpeciality.text = null
        etPreferredDoctor.text = null
        etPreferredDate.text = null
        etPreferredTime.text = null

        // Clear errors from TextInputLayouts
        tilFullName.error = null
        tilContactNumber.error = null
        tilEmailAddress.error = null // Corrected and ensured all are present
        tilSpeciality.error = null
        tilPreferredDoctor.error = null
        tilPreferredDate.error = null
        tilPreferredTime.error = null
        
        etFullName.requestFocus() // Set focus to the first field for better UX
    }
}
