package com.othman.tripbuddies.controllers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.othman.tripbuddies.R
import com.othman.tripbuddies.controllers.fragments.InboxFragment
import com.othman.tripbuddies.controllers.fragments.CityFragment
import com.othman.tripbuddies.controllers.fragments.ProfileFragment
import com.othman.tripbuddies.controllers.fragments.TripFragment
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private var isTablet = false
    private var fragmentId = 0

    private var user = User()
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
            fragmentId = 3
        }

        configureBottomNavigationView()
        configureUI(fragmentId)

        // Verify if device is a tablet
        val secondFragment = findViewById<View>(R.id.second_fragment_container)
        isTablet = secondFragment?.visibility == View.VISIBLE
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

            0 -> displayFragment(ProfileFragment.newInstance(FirebaseUserHelper.getCurrentUser()!!.uid))
            1 -> displayFragment(CityFragment.newInstance())
            2 -> displayFragment(InboxFragment.newInstance())
            3 -> displayFragment(TripFragment.newInstance(trip))
        }
    }



    private fun configureBottomNavigationView() {

        // Configure bottom buttons
        bottom_nav_view.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.bottom_menu_profile -> displayFragment(ProfileFragment.newInstance(FirebaseUserHelper.getCurrentUser()!!.uid))
                R.id.bottom_menu_places -> displayFragment(CityFragment.newInstance())
                R.id.bottom_menu_inbox -> displayFragment(InboxFragment.newInstance())
            }

            return@setOnNavigationItemSelectedListener true
        }
    }


    private fun displayFragment(fragment: Fragment) {

        // Set fragmentId to save it into SavedInstanceState
        when (fragment) {
            is ProfileFragment -> fragmentId = 0

            is CityFragment -> fragmentId = 1

            is InboxFragment -> fragmentId = 2

            is TripFragment -> fragmentId = 3
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.fragment_container, fragment).commit()

    }


    // Display second fragment is device is a tablet
    private fun displaySecondFragment(fragment: Fragment) {

        if (isTablet) {

            val secondTransaction = supportFragmentManager.beginTransaction()
            secondTransaction.addToBackStack(null)
            secondTransaction.replace(R.id.second_fragment_container, fragment).commit()
        }
    }
}
