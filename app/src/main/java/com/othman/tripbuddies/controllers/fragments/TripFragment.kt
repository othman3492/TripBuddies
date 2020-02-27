package com.othman.tripbuddies.controllers.fragments


import TripBuddiesAdapter
import TripDestinationsAdapter
import TripPhotoAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.othman.tripbuddies.R
import com.othman.tripbuddies.controllers.activities.AddEditActivity
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.viewmodels.FirestoreCityViewModel
import com.othman.tripbuddies.viewmodels.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_trip.*


class TripFragment : Fragment(R.layout.fragment_trip) {

    private lateinit var photoAdapter: TripPhotoAdapter
    private lateinit var buddiesAdapter: TripBuddiesAdapter
    private lateinit var destinationsAdapter: TripDestinationsAdapter
    private lateinit var trip: Trip

    private lateinit var tripViewModel: FirestoreTripViewModel
    private lateinit var userViewModel: FirestoreUserViewModel
    private lateinit var cityViewModel: FirestoreCityViewModel


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

        configureViewModel()
        configureRecyclerViews()
        configureButtons()

        getDataAndUpdateUI(trip)
    }


    private fun configureRecyclerViews() {

        // Configure photos RecyclerView
        photoAdapter = TripPhotoAdapter(requireContext()) { photo: String -> displayPhotoOnClick(photo) }

        trip_photos_recycler_view.adapter = photoAdapter
        trip_photos_recycler_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        trip_photos_recycler_view.addItemDecoration(DividerItemDecoration(
            trip_photos_recycler_view.context, DividerItemDecoration.HORIZONTAL))

        // Configure buddies RecyclerView
        buddiesAdapter = TripBuddiesAdapter(requireContext()) { user: User -> openProfileFragmentOnClick(user) }
        trip_buddies_recycler_view.adapter = buddiesAdapter
        trip_buddies_recycler_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        trip_buddies_recycler_view.addItemDecoration(DividerItemDecoration(
            trip_buddies_recycler_view.context, DividerItemDecoration.VERTICAL))

        // Configure destinations RecyclerView
        destinationsAdapter = TripDestinationsAdapter(requireContext()) { city: City -> openCityFragmentOnClick(city) }
        trip_destinations_recycler_view.adapter = destinationsAdapter
        trip_destinations_recycler_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        trip_destinations_recycler_view.addItemDecoration(DividerItemDecoration(
            trip_destinations_recycler_view.context, DividerItemDecoration.VERTICAL))

    }


    private fun configureButtons() {

        // Set edit button
        edit_floating_action_button.setOnClickListener {
            val editIntent = Intent(activity, AddEditActivity::class.java)
            editIntent.putExtra("TRIP_TO_EDIT", trip)

            startActivity(editIntent)
        }

        // Set delete button
        delete_floating_action_button.setOnClickListener { deleteTrip(trip) }
    }


    private fun getDataAndUpdateUI(trip: Trip) {

        this.trip = trip

        // Set trip cover picture
        if (trip.imagesList.isNotEmpty())
            Picasso.get().load(trip.imagesList[0]).into(cover_picture)

        // Load photos
        configureRecyclerViews()

        // Load data into views
        trip_name.text = trip.name
        trip_username.text = String.format(this.resources.getString(R.string.by_name), trip.username)
        trip_description.text = trip.description
        trip_dates.text = String.format(context!!.resources.getString(R.string.dates_from_to), trip.departDate, trip.returnDate)
        nb_photos_textview.text = trip.nbImages.toString()
        nb_buddies_textview.text = trip.nbBuddies.toString()
    }


    private fun configureViewModel() {

        tripViewModel = ViewModelProviders.of(this).get(FirestoreTripViewModel::class.java)
        userViewModel = ViewModelProviders.of(this).get(FirestoreUserViewModel::class.java)
        cityViewModel = ViewModelProviders.of(this).get(FirestoreCityViewModel::class.java)

    }


    private fun deleteTrip(trip: Trip) {

        tripViewModel.deleteTripFromFirestore(trip).addOnSuccessListener {

            userViewModel.removeTripFromUser(trip.userId, trip).addOnSuccessListener {

                Toast.makeText(activity, "Trip deleted !", Toast.LENGTH_SHORT).show()
            }
        }

        // Close fragment and get back to home screen
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.fragment_container, ProfileFragment.newInstance(FirebaseUserHelper.getCurrentUser()!!.uid)).commit()
    }


    // Open User profile fragment when clicked
    private fun displayPhotoOnClick(photo: String) {

        Glide.with(this).load(photo).into(trip_cover_picture)
    }


    // Open User profile fragment when clicked
    private fun openProfileFragmentOnClick(user: User) {

        val isTablet = resources.getBoolean(R.bool.isTablet)
        val fragment = ProfileFragment.newInstance(user.userId)

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)

        if (isTablet) {
            transaction.replace(R.id.second_fragment_container, fragment).commit()
        } else {
            transaction.replace(R.id.fragment_container, fragment).commit()
        }
    }

    // Open City details fragment when clicked
    private fun openCityFragmentOnClick(city: City) {

        val isTablet = resources.getBoolean(R.bool.isTablet)
        val fragment = CityFragment.newInstance(city)

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)

        if (isTablet) {
            transaction.replace(R.id.second_fragment_container, fragment).commit()
        } else {
            transaction.replace(R.id.fragment_container, fragment).commit()
        }
    }





}
