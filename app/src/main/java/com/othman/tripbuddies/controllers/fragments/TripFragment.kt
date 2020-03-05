package com.othman.tripbuddies.controllers.fragments


import TripBuddiesAdapter
import TripDestinationsAdapter
import TripPhotosAdapter
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.othman.tripbuddies.BuildConfig

import com.othman.tripbuddies.R
import com.othman.tripbuddies.controllers.activities.AddEditActivity
import com.othman.tripbuddies.extensions.getCountry
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.utils.Utils
import com.othman.tripbuddies.viewmodels.FirestoreCityViewModel
import com.othman.tripbuddies.viewmodels.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_trip.*


class TripFragment : Fragment(R.layout.fragment_trip) {

    private lateinit var photoAdapter: TripPhotosAdapter
    private lateinit var buddiesAdapter: TripBuddiesAdapter
    private lateinit var destinationsAdapter: TripDestinationsAdapter
    private lateinit var trip: Trip

    private lateinit var tripViewModel: FirestoreTripViewModel
    private lateinit var userViewModel: FirestoreUserViewModel
    private lateinit var cityViewModel: FirestoreCityViewModel

    private val autocompleteRequestCode = 21
    private val galleryCode = 1
    private val galleryPermissionCode = 11


    companion object {
        fun newInstance(trip: Trip): TripFragment {

            val args = Bundle()
            args.putSerializable("TRIP", trip)

            val fragment = TripFragment()
            fragment.arguments = args
            return fragment
        }
    }


    /*-----------------------------

    USER INTERFACE

    ---------------------------- */


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trip = arguments!!.getSerializable("TRIP") as Trip

        if (!Places.isInitialized()) Places.initialize(context!!, BuildConfig.google_apikey)


        configureViewModel()
        configureButtons()

        configureUI(trip)
    }


    private fun configureUI(trip: Trip) {

        this.trip = trip

        // Load data into views
        configureRecyclerViews()
        getDestinationsList(trip)
        getBuddiesList(trip)


        trip_name.text = trip.name
        trip_username.text =
            String.format(this.resources.getString(R.string.by_name), trip.username)
        trip_description.text = trip.description
        trip_dates.text = String.format(
            context!!.resources.getString(R.string.dates_from_to),
            trip.departDate,
            trip.returnDate
        )
        nb_photos_textview.text = trip.nbPhotos.toString()
        nb_buddies_textview.text = trip.nbBuddies.toString()
        nb_destinations_textview.text = trip.nbDestinations.toString()

        // Display first trip photo if image list isn't empty
        if (trip.photosList.isNotEmpty()) {
            Glide.with(activity!!).load(trip.photosList[0]).into(trip_cover_picture)
        } else {
            Glide.with(activity!!).load(R.drawable.blank_picture).into(trip_cover_picture)
        }
    }


    private fun configureRecyclerViews() {

        // Configure photos RecyclerView
        photoAdapter = TripPhotosAdapter(
            requireContext(),
            trip,
            { photo: String -> displayPhotoOnClick(photo) },
            { checkPermissionForGallery() })

        trip_photos_recycler_view.adapter = photoAdapter
        trip_photos_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        trip_photos_recycler_view.addItemDecoration(
            DividerItemDecoration(
                trip_photos_recycler_view.context, DividerItemDecoration.HORIZONTAL
            )
        )

        // Configure buddies RecyclerView
        buddiesAdapter = TripBuddiesAdapter(
            requireContext(),
            { user: User -> openProfileFragmentOnClick(user) },
            { Toast.makeText(activity, "User clicked", Toast.LENGTH_SHORT).show() })

        trip_buddies_recycler_view.adapter = buddiesAdapter
        trip_buddies_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        trip_buddies_recycler_view.addItemDecoration(
            DividerItemDecoration(
                trip_buddies_recycler_view.context, DividerItemDecoration.HORIZONTAL
            )
        )

        // Configure destinations RecyclerView
        destinationsAdapter = TripDestinationsAdapter(
            requireContext(),
            { city: City -> openCityFragmentOnClick(city) },
            { configurePlaceAutocomplete() })

        trip_destinations_recycler_view.adapter = destinationsAdapter
        trip_destinations_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        trip_destinations_recycler_view.addItemDecoration(
            DividerItemDecoration(
                trip_destinations_recycler_view.context, DividerItemDecoration.HORIZONTAL
            )
        )

    }


    private fun configureButtons() {

        // Set edit button
        edit_trip_button.setOnClickListener {
            val editIntent = Intent(activity, AddEditActivity::class.java)
            editIntent.putExtra("TRIP_TO_EDIT", trip)

            startActivity(editIntent)
        }

        // Set delete button
        delete_trip_button.setOnClickListener {

            AlertDialog.Builder(activity)
                .setMessage(R.string.delete_trip_confirmation)
                .setPositiveButton(R.string.yes) { _, _ -> deleteTrip(trip) }
                .setNegativeButton(R.string.no, null)
                .show()
        }
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


    // Retrieve city data from Autocomplete search
    private fun configurePlaceAutocomplete() {

        val fields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.PHOTO_METADATAS
        )

        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setTypeFilter(TypeFilter.CITIES)
            .build(context!!)
        startActivityForResult(intent, autocompleteRequestCode)

    }


    /*-----------------------------

    DATA QUERIES

    ---------------------------- */


    private fun configureViewModel() {

        tripViewModel = ViewModelProviders.of(this).get(FirestoreTripViewModel::class.java)
        userViewModel = ViewModelProviders.of(this).get(FirestoreUserViewModel::class.java)
        cityViewModel = ViewModelProviders.of(this).get(FirestoreCityViewModel::class.java)

    }


    // Get destinations list from trip
    private fun getDestinationsList(trip: Trip): List<City> {

        val list: MutableList<City> = ArrayList()

        tripViewModel.getAllCitiesFromTrip(trip.tripId).observe(viewLifecycleOwner, Observer { it ->

            for (doc in it) {

                cityViewModel.getCity(doc).observe(viewLifecycleOwner, Observer {
                    if (it != null && !list.contains(it))
                        list.add(it)
                })
            }

            destinationsAdapter.updateData(list)
        })

        return list
    }


    // Get destinations list from trip
    private fun getBuddiesList(trip: Trip): List<User> {

        val list: MutableList<User> = ArrayList()

        tripViewModel.getAllBuddiesFromTrip(trip.tripId)
            .observe(viewLifecycleOwner, Observer { it ->

                for (doc in it) {

                    userViewModel.getUser(doc).observe(viewLifecycleOwner, Observer {
                        if (it != null && !list.contains(it))
                            list.add(it)
                    })
                }

                buddiesAdapter.updateData(list)
            })

        return list
    }


    // Ask for permission to pick image from gallery
    private fun checkPermissionForGallery() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
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
        super.onActivityResult(requestCode, resultCode, data)

        // Handle image request
        if (resultCode == Activity.RESULT_OK && requestCode == galleryCode) {

            // Upload image and get Firebase new URI
            val imageId = Utils.generateId()
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
                    this.trip.photosList.add(downloadUri.toString())
                    tripViewModel.updateTripIntoFirestore(this.trip).addOnSuccessListener {

                        Toast.makeText(activity, "New photo added !", Toast.LENGTH_SHORT).show()
                        this.photoAdapter.notifyDataSetChanged()
                    }
                }
            }
        }


        // Handle autocomplete request
        else if (resultCode == Activity.RESULT_OK && requestCode == autocompleteRequestCode) {

            // Retrieve Place data and create City object
            val place = Autocomplete.getPlaceFromIntent(data!!)
            val city = City(
                place.id!!,
                place.name!!,
                place.getCountry(context!!),
                place.latLng!!.latitude,
                place.latLng!!.longitude,
                place.photoMetadatas!![0].zza()
            )

            // Create city collection if it doesn't exist
            cityViewModel.getCity(city.cityId).observe(viewLifecycleOwner, Observer {

                if (it == null) cityViewModel.createCityIntoFirestore(city)
            })

            // Add city to trip destinations list
            tripViewModel.addCityToTrip(trip.tripId, city.cityId).addOnSuccessListener {
                // Add trip to city trip list
                cityViewModel.addTripToCity(city.cityId, trip.tripId).addOnSuccessListener {
                    // Add user to city visitors list
                    cityViewModel.addVisitorToCity(city.cityId, trip.userId).addOnSuccessListener {
                        // Add city to user visited cities
                        userViewModel.addVisitedCity(trip.userId, city.cityId)
                    }
                }
            }
        }
    }



    private fun deleteTrip(trip: Trip) {

        // Delete trip from Firestore
        tripViewModel.deleteTripFromFirestore(trip.tripId).addOnSuccessListener {

            // Delete trip from users
            for (doc in getBuddiesList(trip)) {
                userViewModel.removeTripFromUser(doc.userId, trip.tripId)
            }

            // Delete trip from cities
            for (doc in getDestinationsList(trip)) {
                cityViewModel.removeTripFromCity(doc.cityId, trip.tripId)
            }

            Toast.makeText(activity, "Trip deleted !", Toast.LENGTH_SHORT).show()
        }

        // Close fragment and get back to home screen
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(
            R.id.fragment_container,
            ProfileFragment.newInstance(FirebaseUserHelper.getCurrentUser()!!.uid)
        ).commit()
    }
}

