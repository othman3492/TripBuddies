package com.othman.tripbuddies.controllers.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.Trip
import kotlinx.android.synthetic.main.fragment_trip.*

/**
 * A simple [Fragment] subclass.
 */
class TripFragment : Fragment(R.layout.fragment_trip) {

    private lateinit var trip: Trip


    companion object {
        fun newInstance(trip: Trip): TripFragment {

            val args = Bundle()
            args.putSerializable("TRIP", trip)

            val fragment = TripFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trip = arguments!!.getSerializable("TRIP") as Trip

        trip_username.text = trip.user.name
    }


}
