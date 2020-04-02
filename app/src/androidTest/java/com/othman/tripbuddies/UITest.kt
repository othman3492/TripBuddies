package com.othman.tripbuddies

import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.othman.tripbuddies.controllers.activities.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UITest {
    @get:Rule
    val activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(
        MainActivity::class.java
    )

    @Test
    fun clickOnPlaces_openCityFragment() {
        Espresso.onView(withId(R.id.menu_bottom_places)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.city_original_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun clickOnProfile_openProfileFragment() {
        Espresso.onView(withId(R.id.menu_bottom_places)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.city_original_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.menu_bottom_profile)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.cover_profile))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun clickOnPlusButton_openAddEditActivity() {
        Espresso.onView(withId(R.id.add_floating_action_button)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.add_trip))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
