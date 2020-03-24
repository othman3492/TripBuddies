package com.othman.tripbuddies.controllers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.othman.tripbuddies.R
import com.othman.tripbuddies.controllers.fragments.CityFragment
import com.othman.tripbuddies.controllers.fragments.ProfileFragment
import com.othman.tripbuddies.controllers.fragments.TripFragment
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.utils.FirebaseUserHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val PROFILE_FRAGMENT = 0
    private val CITY_FRAGMENT = 1
    private val TRIP_FRAGMENT = 2

    private var fragmentId = PROFILE_FRAGMENT

    private var trip = Trip()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get savedInstanceState in case of screen rotation
        if (savedInstanceState != null) {

            fragmentId = savedInstanceState.getInt("FRAGMENT_ID")
        }

        // Display TripFragment if an intent exists
        if (intent.getSerializableExtra("TRIP") != null) {

            trip = intent.getSerializableExtra("TRIP") as Trip
            fragmentId = TRIP_FRAGMENT
        }

        configureBottomNavigationView()
        configureUI(fragmentId)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("FRAGMENT_ID", fragmentId)
    }


    // Enable back button if fragment isn't home screen
    override fun onBackPressed() {

        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }


    private fun configureUI(fragmentId: Int) {

        when (fragmentId) {

            PROFILE_FRAGMENT -> displayFragment(ProfileFragment.newInstance(FirebaseUserHelper.getCurrentUser()!!.uid))
            CITY_FRAGMENT -> displayFragment(CityFragment.newInstance(null))
            TRIP_FRAGMENT -> displayFragment(TripFragment.newInstance(trip))
        }
    }


    private fun configureBottomNavigationView() {

        // Configure bottom buttons
        bottom_nav_view.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.bottom_menu_profile -> displayFragment(
                    ProfileFragment.newInstance(
                        FirebaseUserHelper.getCurrentUser()!!.uid
                    )
                )
                R.id.bottom_menu_places -> displayFragment(CityFragment.newInstance(null))
            }

            return@setOnNavigationItemSelectedListener true
        }
    }


    fun displayFragment(fragment: Fragment) {

        // Set fragmentId to save it into SavedInstanceState
        when (fragment) {
            is ProfileFragment -> fragmentId = PROFILE_FRAGMENT

            is CityFragment -> fragmentId = CITY_FRAGMENT

            is TripFragment -> fragmentId = TRIP_FRAGMENT
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.fragment_container, fragment).commit()

    }


}
