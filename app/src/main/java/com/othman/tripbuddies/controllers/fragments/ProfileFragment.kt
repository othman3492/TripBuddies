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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.othman.tripbuddies.R
import com.othman.tripbuddies.controllers.activities.AddEditActivity
import com.othman.tripbuddies.controllers.activities.MainActivity
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.utils.Utils.generateId
import com.othman.tripbuddies.viewmodels.FirestoreCityViewModel
import com.othman.tripbuddies.viewmodels.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(R.layout.fragment_profile) {


    private val galleryCode = 1
    private val galleryPermissionCode = 11

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

                    username.text = it.name
                    setProfilePicture(it)
                    setCoverPicture(it)

                    configureFloatingButton(it)
                }
            })

    }


    private fun configureButtons(user: User) {


        profile_last_trips_button.setOnClickListener {
            configureTripsRecyclerView()
            getTripList(user)
        }
        profile_wish_list_button.setOnClickListener {
            configureCitiesRecyclerView()
            getWishList(user)
        }
        cover_profile_change_button.setOnClickListener { checkPermissionForGallery() }
    }


    @SuppressLint("RestrictedApi")
    private fun configureFloatingButton(user: User) {


        if (user.userId == FirebaseUserHelper.getCurrentUser()!!.uid) {

            add_floating_action_button.show()
            add_floating_action_button.setOnClickListener {

                startActivity(Intent(activity, AddEditActivity::class.java))
            }
        } else {

            add_floating_action_button.hide()
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

    // Load cover picture into view if not null
    private fun setCoverPicture(user: User) {

        if (user.urlCoverPicture != null) {

            Glide.with(this)
                .load(user.urlCoverPicture)
                .into(cover_picture)
        }
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

            tripViewModel.getTrip(doc).observe(viewLifecycleOwner, Observer { it ->
                if (it != null && !list.contains(it)) {
                    list.add(it)
                    list.sortByDescending { it.creationDate }
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
                    profileUser.urlCoverPicture = downloadUri.toString()
                    userViewModel.updateUserIntoFirestore(profileUser)

                    // Load image into view
                    Glide.with(this)
                        .load(profileUser.urlCoverPicture).into(cover_picture)
                }
            }


        }
    }
}


