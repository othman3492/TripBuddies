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
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.utils.Utils
import com.othman.tripbuddies.viewmodels.FirestoreCityViewModel
import com.othman.tripbuddies.viewmodels.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel
import kotlinx.android.synthetic.main.fragment_trip.*


class TripFragment : Fragment(R.layout.fragment_trip) {

    private lateinit var photoAdapter: TripPhotosAdapter
    private lateinit var buddiesAdapter: TripBuddiesAdapter
    private lateinit var destinationsAdapter: TripDestinationsAdapter
    private lateinit var trip: Trip

    private lateinit var tripViewModel: FirestoreTripViewModel
    private lateinit var userViewModel: FirestoreUserViewModel
    private lateinit var cityViewModel: FirestoreCityViewModel

    private val autocompleteRequestCode = 1
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
        getLists(trip)


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
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        trip_buddies_recycler_view.addItemDecoration(
            DividerItemDecoration(
                trip_buddies_recycler_view.context, DividerItemDecoration.VERTICAL
            )
        )

        // Configure destinations RecyclerView
        destinationsAdapter = TripDestinationsAdapter(
            requireContext(),
            { city: City -> openCityFragmentOnClick(city) },
            { configurePlaceAutocomplete() })

        trip_destinations_recycler_view.adapter = destinationsAdapter
        trip_destinations_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        trip_destinations_recycler_view.addItemDecoration(
            DividerItemDecoration(
                trip_destinations_recycler_view.context, DividerItemDecoration.VERTICAL
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
        transaction.replace(
            R.id.fragment_container,
            ProfileFragment.newInstance(FirebaseUserHelper.getCurrentUser()!!.uid)
        ).commit()
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


    // Get lists from subcollections
    private fun getLists(trip: Trip) {

        tripViewModel.getAllCitiesFromTrip(trip.tripId).observe(viewLifecycleOwner, Observer {
            this.destinationsAdapter.updateData(it)
            trip.nbDestinations = it.size
        })

        tripViewModel.getAllBuddiesFromTrip(trip.tripId).observe(viewLifecycleOwner, Observer {
            this.buddiesAdapter.updateData(it)
            trip.nbBuddies = it.size
        })

        // Update number properties
        tripViewModel.updateTripIntoFirestore(trip)
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


    }
}

