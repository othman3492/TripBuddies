package com.othman.tripbuddies.controllers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.othman.tripbuddies.R
import com.othman.tripbuddies.controllers.fragments.ChatFragment
import com.othman.tripbuddies.controllers.fragments.CityFragment
import com.othman.tripbuddies.controllers.fragments.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var fragmentId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {

            fragmentId = savedInstanceState.getInt("FRAGMENT_ID")
        }

        configureBottomNavigationView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("FRAGMENT_ID", fragmentId)
    }



    private fun configureBottomNavigationView() {

        // Configure bottom buttons
        bottom_nav_view.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.bottom_menu_profile -> displayFragment(ProfileFragment.newInstance())
                R.id.bottom_menu_places -> displayFragment(CityFragment.newInstance())
                R.id.bottom_menu_inbox -> displayFragment(ChatFragment.newInstance())
            }

            return@setOnNavigationItemSelectedListener true
        }
    }


    private fun displayFragment(fragment: Fragment) {

        // Set fragmentId to save it into SavedInstanceState
        when (fragment) {
            is ProfileFragment -> {
                fragmentId = 0
            }
            is CityFragment -> {
                fragmentId = 1
            }
            is ChatFragment -> {
                fragmentId = 2
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.fragment_container, fragment).commit()

    }
}
