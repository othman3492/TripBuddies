package com.othman.tripbuddies.controllers.fragments


import ProfileCitiesAdapter
import ProfileTripsAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(R.layout.fragment_profile) {


    private val galleryCode = 1
    private val galleryPermissionCode = 11

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

                profileUser = it

                // Fill user data into views
                configureTripsRecyclerView()
                getTripList(it)
                configureButtons(it)

                username.text = it.name
                user_presentation.text = it.presentation
                setProfilePicture(it)
                setCoverPicture(it)

                configureFloatingButton(it)

            })

    }


    private fun configureButtons(user: User) {


        profile_last_trips_button.setOnClickListener {
            configureTripsRecyclerView()
            getTripList(user)
        }
        profile_wish_list_button.setOnClickListener { configureCitiesRecyclerView() }
        cover_profile_change_button.setOnClickListener { checkPermissionForGallery() }
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



    // Ask for permission to pick image from gallery
    private fun checkPermissionForGallery() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                // Permission denied
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                // Request permission
                requestPermissions(permissions, galleryPermissionCode)
            } else {
                // Permission granted
                pickPhotoFromGallery()
            }
        } else {
            // No permission needed if API < 23
            pickPhotoFromGallery()
        }
    }

    // Handle request permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {

            galleryPermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    pickPhotoFromGallery()
                } else {
                    // Permission denied
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Open gallery to select cover photo
    private fun pickPhotoFromGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, galleryCode)
    }

    // Handle picked image result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK && requestCode == galleryCode) {

            // Load image into view
            Picasso.get().load(data?.data.toString()).into(cover_picture)

            // Update data and save new cover picture
            profileUser.urlCoverPicture = data?.data.toString()
            userViewModel.updateUserIntoFirestore(profileUser)
        }
    }



}
