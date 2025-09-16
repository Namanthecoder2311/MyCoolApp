package com.example.neokotlinui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.neokotlinuiconverted.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.test.espresso.contrib.PickerActions // For DatePickerDialog


@RunWith(AndroidJUnit4::class)
@LargeTest
class BookingFlowTest {

    // Assumes HomeActivity is your launch activity and leads to BookingActivity
    // Adjust if your launch activity is different.
    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    // A unique name to check for in the appointments list later
    private val testPatientName = "Espresso Test User ${System.currentTimeMillis()}"

    @Test
    fun bookAppointmentAndVerifyInList() {
        // Step 1: Navigate from HomeActivity to BookingActivity
        // This depends on your app's navigation structure.
        // Example: If HomeActivity has a button to go to booking:
        // onView(withId(R.id.btn_navigate_to_booking_or_specialities)).perform(click()) // Replace with your actual ID

        // If you go through SpecialitiesActivity first:
        // onView(withId(R.id.rv_specialities)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())) // Click first speciality
        // onView(withId(R.id.btn_book_from_speciality_details)).perform(click()) // Click book button

        // We are now in BookingActivity (assumption)

        // Step 2: Fill out the booking form
        onView(withId(R.id.et_full_name)).perform(typeText(testPatientName), closeSoftKeyboard())
        onView(withId(R.id.et_contact_number)).perform(typeText("0123456789"), closeSoftKeyboard())
        onView(withId(R.id.et_email_address)).perform(typeText("espresso@test.com"), closeSoftKeyboard())

        // Select Speciality (assuming AutoCompleteTextView)
        onView(withId(R.id.et_speciality)).perform(typeText("General Dentistry")) // Start typing
        // onView(withText("General Dentistry")).inRoot(isPlatformPopup()).perform(click()) // Click the item from dropdown
        // Alternative for AutoCompleteTextView if direct text setting works and it's not a strict dropdown:
        onView(withId(R.id.et_speciality)).perform(click()) // Open dropdown
        onView(withText("General Dentistry")).inRoot(withDecorView(isFocusable())).perform(click()); // Select from list

        // Select Preferred Doctor
        onView(withId(R.id.et_preferred_doctor)).perform(click())
        onView(withText("Dr. Emily Carter (General Dentistry)")).inRoot(withDecorView(isFocusable())).perform(click());


        // Select Preferred Date
        onView(withId(R.id.et_preferred_date)).perform(click())
        // Get current calendar
        val calendar = Calendar.getInstance()
        // For testing, let's pick tomorrow or a fixed future date to avoid issues with past dates
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) // Month is 0-indexed in DatePicker
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        onView(withClassName(org.hamcrest.Matchers.equalTo(android.widget.DatePicker::class.java.name)))
            .perform(PickerActions.setDate(year, month + 1, day)) // month + 1 because PickerActions month is 1-indexed
        onView(withId(android.R.id.button1)).perform(click()) // "OK" button in DatePickerDialog

        // Select Preferred Time
        onView(withId(R.id.et_preferred_time)).perform(click())
        onView(withText("09:30 AM")).inRoot(withDecorView(isFocusable())).perform(click());


        // Step 3: Click the "Book Appointment" button
        onView(withId(R.id.btn_book_appointment)).perform(click())

        // Step 4: Verify success dialog and navigate to MyAppointmentsActivity
        onView(withText(R.string.booking_success_title)).check(matches(isDisplayed()))
        onView(withText(R.string.view_appointments_button)).perform(click())

        // Step 5: We are now in MyAppointmentsActivity. Verify the new appointment is listed.
        // This requires scrolling RecyclerView and checking for the text.
        // You might need a custom matcher for RecyclerView item content.
        // For simplicity, we'll just check if a view with the patient's name is displayed.
        // This is a basic check; a more robust check would target a specific TextView within a RecyclerView item.
        onView(withText(testPatientName)).check(matches(isDisplayed()))

        // Optional: Verify other details like doctor name or status within the item
        onView(allOf(withText("Dr. Emily Carter (General Dentistry)"), isDescendantOfA(hasDescendant(withText(testPatientName)))))
            .check(matches(isDisplayed()));
        onView(allOf(withText("BOOKED"), isDescendantOfA(hasDescendant(withText(testPatientName)))))
            .check(matches(isDisplayed()));
    }
}
