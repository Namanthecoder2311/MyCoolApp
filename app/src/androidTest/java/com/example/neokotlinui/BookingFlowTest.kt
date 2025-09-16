package com.example.neokotlinui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.neokotlinuiconverted.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar
import androidx.test.espresso.contrib.PickerActions
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import android.widget.DatePicker

@RunWith(AndroidJUnit4::class)
@LargeTest
class BookingFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    private val testPatientName = "Espresso Test User ${System.currentTimeMillis()}"

    @Test
    fun bookAppointmentAndVerifyInList() {
        // Fill the booking form
        onView(withId(R.id.et_full_name)).perform(typeText(testPatientName), closeSoftKeyboard())
        onView(withId(R.id.et_contact_number)).perform(typeText("0123456789"), closeSoftKeyboard())
        onView(withId(R.id.et_email_address)).perform(typeText("espresso@test.com"), closeSoftKeyboard())

        // Speciality: type and select from dropdown (use platform popup matcher)
        onView(withId(R.id.et_speciality)).perform(typeText("General Dentistry"), closeSoftKeyboard())
        onView(withText("General Dentistry"))
            .inRoot(isPlatformPopup())
            .perform(click())

        // Preferred doctor selection (assumes similar popup)
        onView(withId(R.id.et_preferred_doctor)).perform(click())
        onView(withText("Dr. Emily Carter (General Dentistry)"))
            .inRoot(isPlatformPopup())
            .perform(click())

        // DatePicker: pick tomorrow
        onView(withId(R.id.et_preferred_date)).perform(click())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) // 0-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        onView(withClassName(equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(year, month + 1, day)) // PickerActions month is 1-based
        onView(withId(android.R.id.button1)).perform(click()) // OK

        // Time: open and select (assumes time options appear in a popup)
        onView(withId(R.id.et_preferred_time)).perform(click())
        onView(withText("09:30 AM"))
            .inRoot(isPlatformPopup())
            .perform(click())

        // Book and verify
        onView(withId(R.id.btn_book_appointment)).perform(click())
        onView(withText(R.string.booking_success_title)).check(matches(isDisplayed()))
        onView(withText(R.string.view_appointments_button)).perform(click())

        // Verify appointment appears in list
        onView(withText(testPatientName)).check(matches(isDisplayed()))

        // Optionally verify doctor and status inside the same item (basic approach)
        onView(
            allOf(
                withText("Dr. Emily Carter (General Dentistry)"),
                isDescendantOfA(hasDescendant(withText(testPatientName)))
            )
        ).check(matches(isDisplayed()))

        onView(
            allOf(
                withText("BOOKED"),
                isDescendantOfA(hasDescendant(withText(testPatientName)))
            )
        ).check(matches(isDisplayed()))
    }
}