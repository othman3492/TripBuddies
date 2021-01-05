package com.othman.tripbuddies.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.othman.tripbuddies.R
import com.othman.tripbuddies.ui.fragments.CityFragment
import com.othman.tripbuddies.ui.fragments.ProfileFragment
import com.othman.tripbuddies.ui.fragments.TripFragment
import com.othman.tripbuddies.data.model.Trip
import com.othman.tripbuddies.util.FirebaseUserHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val profileFragmentId = 0
    private val cityFragmentId = 1
    private val tripFragmentId = 2

    private var fragmentId = profileFragmentId

    private var trip = Trip()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get savedInstanceState in case of screen rotation
        if (savedInstanceState != null) {

            when (supportFragmentManager.findFragmentByTag("TRIPBUDDIES")) {
                is ProfileFragment -> fragmentId = profileFragmentId
                is CityFragment -> fragmentId = cityFragmentId
                is TripFragment -> fragmentId = tripFragmentId
            }

            configureBottomNavigationView()

        } else {

            // Display TripFragment if an intent exists
            if (intent.getSerializableExtra("TRIP") != null) {

                trip = intent.getSerializableExtra("TRIP") as Trip
                fragmentId = tripFragmentId
            }

            configureBottomNavigationView()
            configureUI(fragmentId)
        }
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

            profileFragmentId -> displayFragment(ProfileFragment.newInstance(FirebaseUserHelper.getCurrentUser()!!.uid))
            cityFragmentId -> displayFragment(CityFragment.newInstance(null))
            tripFragmentId -> displayFragment(TripFragment.newInstance(trip))
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
            is ProfileFragment -> fragmentId = profileFragmentId

            is CityFragment -> fragmentId = cityFragmentId

            is TripFragment -> fragmentId = tripFragmentId
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.fragment_container, fragment, "TRIPBUDDIES").commit()
    }


}
