package com.othman.tripbuddies.ui.fragments


import com.othman.tripbuddies.adapters.ProfileCitiesAdapter
import com.othman.tripbuddies.adapters.ProfileTripsAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.othman.tripbuddies.R
import com.othman.tripbuddies.ui.activities.AddEditActivity
import com.othman.tripbuddies.ui.activities.MainActivity
import com.othman.tripbuddies.data.model.City
import com.othman.tripbuddies.data.model.Trip
import com.othman.tripbuddies.data.model.User
import com.othman.tripbuddies.util.Connection
import com.othman.tripbuddies.util.FirebaseUserHelper
import com.othman.tripbuddies.util.Utils.generateId
import com.othman.tripbuddies.viewmodel.FirestoreCityViewModel
import com.othman.tripbuddies.viewmodel.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodel.FirestoreUserViewModel
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(R.layout.fragment_profile) {


    private val galleryCode = 1
    private val galleryPermissionCode = 11
    private val profilePictureCode = 101
    private val coverPictureCode = 201
    private var buttonClicked: Int? = null

    private var profileUser = User()
    private lateinit var profileCitiesAdapter: ProfileCitiesAdapter
    private lateinit var profileTripsAdapter: ProfileTripsAdapter
    private lateinit var tripViewModel: FirestoreTripViewModel
    private lateinit var userViewModel: FirestoreUserViewModel
    private lateinit var cityViewModel: FirestoreCityViewModel


    companion object {
        fun newInstance(userId: String): ProfileFragment {

            val args = Bundle()
            args.putString("USER_ID", userId)

            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }


    /*-----------------------------

    USER INTERFACE

    ---------------------------- */


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewModels()
        configureUI()
    }


    private fun configureUI() {

        // Retrieve user data
        userViewModel.getUser(arguments!!.getString("USER_ID")!!)
            .observe(viewLifecycleOwner, Observer {

                if (it != null) {

                    profileUser = it

                    // Fill user data into views
                    configureTripsRecyclerView()
                    getTripList(it)
                    configureButtons(it)

                    if (it.displayEmail) email_address.text = it.email else email_address.text = ""
                    username.text = it.name
                    setProfilePicture(it)
                    setCoverPicture(it)
                }
            })

    }


    @SuppressLint("RestrictedApi")
    private fun configureButtons(user: User) {

        // Last trips button
        profile_last_trips_button.setOnClickListener {
            configureTripsRecyclerView()
            getTripList(user)
        }
        // Wish list button
        profile_wish_list_button.setOnClickListener {
            configureCitiesRecyclerView()
            getWishList(user)
        }


        // Display buttons depending on user rights
        if (user.userId == FirebaseUserHelper.getCurrentUser()!!.uid) {

            // Add new trip
            add_floating_action_button.show()
            add_floating_action_button.setOnClickListener {
                startActivity(
                    Intent(
                        activity,
                        AddEditActivity::class.java
                    )
                )
            }

            // Activate cover and profile picture change if network is available
            if (Connection.checkNetworkState(activity!!)) {

                cover_profile_change_button.visibility = View.VISIBLE
                cover_profile_change_button.setOnClickListener {
                    buttonClicked = coverPictureCode
                    checkPermissionForGallery()
                }

                profile_picture.setOnClickListener {
                    buttonClicked = profilePictureCode
                    checkPermissionForGallery()
                }
            } else {

                cover_profile_change_button.visibility = View.GONE
                profile_picture.setOnClickListener{

                    Toast.makeText(activity, "No network available", Toast.LENGTH_SHORT).show()
                }
            }

            // Logout
            logout_button.visibility = View.VISIBLE
            logout_button.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(activity!!).addOnSuccessListener { activity!!.finish() }
            }

            // Open settings
            settings_button.visibility = View.VISIBLE
            settings_button.setOnClickListener { displaySettingsFragment() }

        } else {

            add_floating_action_button.hide()
            cover_profile_change_button.visibility = View.GONE
            logout_button.visibility = View.GONE
            settings_button.visibility = View.GONE
        }


    }


    private fun configureTripsRecyclerView() {

        profile_cities_recycler_view.visibility = View.GONE
        profile_trips_recycler_view.visibility = View.VISIBLE

        // Configure trips RecyclerView
        profileTripsAdapter =
            ProfileTripsAdapter(requireContext()) { trip: Trip -> openTripFragmentOnClick(trip) }
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

        profile_trips_recycler_view.visibility = View.GONE
        profile_cities_recycler_view.visibility = View.VISIBLE

        // Configure cities RecyclerView
        profileCitiesAdapter =
            ProfileCitiesAdapter(requireContext()) { city: City -> openCityFragmentOnClick(city) }
        profile_cities_recycler_view.adapter = profileCitiesAdapter
        profile_cities_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        profile_cities_recycler_view.addItemDecoration(
            DividerItemDecoration(
                profile_cities_recycler_view.context, DividerItemDecoration.VERTICAL
            )
        )
    }


    // Load profile picture into view
    private fun setProfilePicture(user: User) {

        Glide.with(this)
            .load(user.urlPicture)
            .apply(RequestOptions.circleCropTransform())
            .into(profile_picture)
    }

    // Load cover picture into view
    private fun setCoverPicture(user: User) {

        Glide.with(this)
            .load(user.urlCoverPicture)
            .into(cover_picture)
    }


    // Open Trip details fragment when clicked
    private fun openTripFragmentOnClick(trip: Trip) {

        (activity as MainActivity).displayFragment(TripFragment.newInstance(trip))
    }


    // Open City details fragment when clicked
    private fun openCityFragmentOnClick(city: City) {

        (activity as MainActivity).displayFragment(CityFragment.newInstance(city))
    }


    // Create settings dialog fragment
    private fun displaySettingsFragment() {

        val fragmentTransaction = childFragmentManager.beginTransaction()
        val prev = childFragmentManager.findFragmentByTag("dialog")
        if (prev != null) fragmentTransaction.remove(prev)

        fragmentTransaction.addToBackStack(null)

        val dialogFragment = SettingsFragment()
        dialogFragment.show(fragmentTransaction, "dialog")
    }


    /*-----------------------------

    DATA QUERIES

    ---------------------------- */


    private fun configureViewModels() {

        tripViewModel = ViewModelProvider(this).get(FirestoreTripViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(FirestoreUserViewModel::class.java)
        cityViewModel = ViewModelProvider(this).get(FirestoreCityViewModel::class.java)
    }


    // Get trip list from user
    private fun getTripList(user: User) {

        val list: MutableList<Trip> = ArrayList()

        for (doc in user.tripList) {

            tripViewModel.getTrip(doc).observe(viewLifecycleOwner, Observer {
                if (it != null && !list.contains(it)) {
                    list.add(it)
                    list.sortByDescending { it.departDate }
                    profileTripsAdapter.updateData(list)
                }
            })
        }
    }


    // Get wish list from user
    private fun getWishList(user: User) {

        val list: MutableList<City> = ArrayList()

        for (doc in user.wishList) {

            cityViewModel.getCity(doc).observe(viewLifecycleOwner, Observer { it ->
                if (it != null && !list.contains(it)) {
                    list.add(it)
                    list.sortBy { it.name }
                    profileCitiesAdapter.updateData(list)
                }
            })
        }
    }


    // Ask for permission to pick image from gallery
    private fun checkPermissionForGallery() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
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
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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


            // Upload image and get Firebase new URI
            val imageId = generateId()
            val imageRef: StorageReference = FirebaseStorage.getInstance().getReference(imageId)
            val uploadTask = imageRef.putFile(data?.data!!)

            // Change and save cover picture
            if (buttonClicked == coverPictureCode) {

                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    imageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result

                        // Update data and save it
                        profileUser.urlCoverPicture = downloadUri.toString()
                        userViewModel.updateUserIntoFirestore(profileUser)

                        // Load image into view
                        Glide.with(this)
                            .load(profileUser.urlCoverPicture).into(cover_picture)
                    }
                }

                // Change and save profile picture
            } else if (buttonClicked == profilePictureCode) {

                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    imageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result

                        // Update data and save it
                        profileUser.urlPicture = downloadUri.toString()
                        userViewModel.updateUserIntoFirestore(profileUser)

                        // Load image into view
                        Glide.with(this)
                            .load(profileUser.urlPicture).into(profile_picture)
                    }
                }
            }


        }
    }
}


