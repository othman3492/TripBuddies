package com.othman.tripbuddies.controllers.fragments


import ProfileCitiesAdapter
import ProfileTripsAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.othman.tripbuddies.R
import com.othman.tripbuddies.controllers.activities.AddEditActivity
import com.othman.tripbuddies.controllers.activities.MainActivity
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.viewmodels.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var profileUser = User()
    private lateinit var profileCitiesAdapter: ProfileCitiesAdapter
    private lateinit var profileTripsAdapter: ProfileTripsAdapter
    private lateinit var tripViewModel: FirestoreTripViewModel
    private lateinit var userViewModel: FirestoreUserViewModel


    companion object {
        fun newInstance(userId: String): ProfileFragment {

            val args = Bundle()
            args.putString("USER_ID", userId)

            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewModels()
        configureUI()
    }


    private fun configureUI() {

        // Retrieve user data
        userViewModel.getUser(arguments!!.getString("USER_ID")!!)
            .observe(viewLifecycleOwner, Observer {

                // Fill user data into views
                configureTripsRecyclerView()
                getTripList(it)
                configureButtons()

                username.text = it.name
                user_presentation.text = it.presentation
                setProfilePicture(it)
                setCoverPicture(it)

                configureFloatingButton(it)

            })

    }


    private fun configureButtons() {


        profile_last_trips_button.setOnClickListener { configureTripsRecyclerView() }
        profile_wish_list_button.setOnClickListener { configureCitiesRecyclerView() }
    }


    @SuppressLint("RestrictedApi")
    private fun configureFloatingButton(user: User) {


        if (user.userId == FirebaseUserHelper.getCurrentUser()!!.uid) {

            add_floating_action_button.setOnClickListener {

                val userIntent = Intent(activity, AddEditActivity::class.java)
                userIntent.putExtra("USER", user)
                startActivity(userIntent)
            }
        } else {

            add_floating_action_button.visibility = View.GONE
            chat_floating_action_button.visibility = View.VISIBLE

            chat_floating_action_button.setOnClickListener {
                // display chat fragment //
            }

        }

    }


    private fun configureTripsRecyclerView() {

        // Configure trips RecyclerView
        profileTripsAdapter = ProfileTripsAdapter(requireContext()) { trip: Trip -> openTripFragmentOnClick(trip) }
        profile_trips_recycler_view.adapter = profileTripsAdapter
        profile_trips_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        profile_trips_recycler_view.addItemDecoration(
            DividerItemDecoration(
                profile_trips_recycler_view.context, DividerItemDecoration.VERTICAL
            )
        )
    }


    private fun configureCitiesRecyclerView() {

        // Configure cities RecyclerView
        profileCitiesAdapter = ProfileCitiesAdapter(requireContext(), profileUser)
        profile_cities_recycler_view.adapter = profileCitiesAdapter
        profile_cities_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        profile_cities_recycler_view.addItemDecoration(
            DividerItemDecoration(
                profile_cities_recycler_view.context, DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun configureViewModels() {

        tripViewModel = ViewModelProviders.of(this).get(FirestoreTripViewModel::class.java)
        userViewModel = ViewModelProviders.of(this).get(FirestoreUserViewModel::class.java)
    }


    // Load profile picture into view
    private fun setProfilePicture(user: User) {

        Glide.with(this)
            .load(user.urlPicture)
            .apply(RequestOptions.circleCropTransform())
            .into(profile_picture)
    }

    // Load cover picture into view if not null
    private fun setCoverPicture(user: User) {

        if (user.urlCoverPicture != null) {

            Glide.with(this)
                .load(user.urlCoverPicture)
                .into(cover_picture)
        }

    }


    private fun updateTripList(list: List<Trip>) {

        this.profileTripsAdapter.updateData(list)
    }


    // Get trips list from Firestore
    private fun getTripList(user: User) {

        tripViewModel.getAllTripsFromUser(user.userId).observe(viewLifecycleOwner,
            Observer<List<Trip>> { updateTripList(it) })
    }


    // Open Trip details fragment when clicked
    private fun openTripFragmentOnClick(trip: Trip) {

        val isTablet = resources.getBoolean(R.bool.isTablet)
        val fragment = TripFragment.newInstance(trip)

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)

        if (isTablet) {
            transaction.replace(R.id.second_fragment_container, fragment).commit()
        } else {
            transaction.replace(R.id.fragment_container, fragment).commit()
        }
    }


}
