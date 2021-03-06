package com.othman.tripbuddies.ui.fragments


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.othman.tripbuddies.ui.activities.AddEditActivity
import com.othman.tripbuddies.ui.activities.MainActivity
import com.othman.tripbuddies.util.extensions.getCountry
import com.othman.tripbuddies.data.model.City
import com.othman.tripbuddies.data.model.Trip
import com.othman.tripbuddies.data.model.User
import com.othman.tripbuddies.util.FirebaseUserHelper
import com.othman.tripbuddies.util.Utils
import com.othman.tripbuddies.viewmodel.FirestoreCityViewModel
import com.othman.tripbuddies.viewmodel.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodel.FirestoreUserViewModel
import com.othman.tripbuddies.adapters.TripBuddiesAdapter
import com.othman.tripbuddies.adapters.TripDestinationsAdapter
import com.othman.tripbuddies.adapters.TripPhotosAdapter
import kotlinx.android.synthetic.main.fragment_trip.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


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

        configureButtons()
        updateUI(trip)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        EventBus.getDefault().register(this)
        if (!Places.isInitialized()) Places.initialize(context!!, BuildConfig.google_apikey)

        configureViewModel()
        trip = arguments!!.getSerializable("TRIP") as Trip
    }


    override fun onSaveInstanceState(outState: Bundle) {

        outState.putSerializable("SAVED_TRIP", trip)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }


    private fun updateUI(trip: Trip) {

        tripViewModel.getTrip(trip.tripId).observe(viewLifecycleOwner, Observer {

            if (it != null) {

                // Load data into views
                configureRecyclerViews(it)
                getDestinationsList(it)
                getBuddiesList(it)


                trip_name.text = it.name
                trip_username.text =
                    String.format(this.resources.getString(R.string.by_name), it.username)
                trip_description.text = it.description
                trip_dates.text = String.format(
                    context!!.resources.getString(R.string.dates_from_to),
                    it.departDate,
                    it.returnDate
                )
                nb_photos_textview.text = it.photosList.size.toString()
                nb_buddies_textview.text = it.buddiesList.size.toString()
                nb_destinations_textview.text = it.destinationsList.size.toString()

                // Display first trip photo if image list isn't empty
                if (it.photosList.isNotEmpty()) {
                    Glide.with(activity!!).load(it.photosList[0]).into(trip_cover_picture)
                }
            }


        })


    }


    private fun configureRecyclerViews(trip: Trip) {

        // Configure photos RecyclerView
        photoAdapter = TripPhotosAdapter(
            requireContext(),
            trip,
            { photo: String -> displayPhotoOnClick(photo) },
            { checkPermissionForGallery() })

        trip_photos_recycler_view.adapter = photoAdapter
        trip_photos_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Configure buddies RecyclerView
        buddiesAdapter = TripBuddiesAdapter(
            requireContext(),
            trip,
            { user: User -> openProfileFragmentOnClick(user) },
            { displayDialogFragment() })

        trip_buddies_recycler_view.adapter = buddiesAdapter
        trip_buddies_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Configure destinations RecyclerView
        destinationsAdapter = TripDestinationsAdapter(
            requireContext(),
            trip,
            { city: City -> openCityFragmentOnClick(city) },
            { configurePlaceAutocomplete() })

        trip_destinations_recycler_view.adapter = destinationsAdapter
        trip_destinations_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }


    private fun configureButtons() {

        // Set user rights
        if (trip.userId == FirebaseUserHelper.getCurrentUser()!!.uid) {

            edit_trip_button.visibility = View.VISIBLE
            delete_trip_button.visibility = View.VISIBLE

            // Set edit button
            edit_trip_button.setOnClickListener {
                val editIntent = Intent(activity, AddEditActivity::class.java)
                editIntent.putExtra("TRIP_TO_EDIT", trip.tripId)

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
        } else {

            edit_trip_button.visibility = View.GONE
            delete_trip_button.visibility = View.GONE
        }
    }


    // Display photo on cover when clicked
    private fun displayPhotoOnClick(photo: String) {

        Glide.with(this).load(photo).into(trip_cover_picture)
    }


    // Open User profile fragment when clicked
    private fun openProfileFragmentOnClick(user: User) {

        if (user.name != "Deleted user")
            (activity as MainActivity).displayFragment(ProfileFragment.newInstance(user.userId))

    }

    // Open City details fragment when clicked
    private fun openCityFragmentOnClick(city: City) {

        (activity as MainActivity).displayFragment(CityFragment.newInstance(city))
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


    // Create user search dialog fragment and pass it arguments from trip
    private fun displayDialogFragment() {

        val fragmentTransaction = childFragmentManager.beginTransaction()
        val prev = childFragmentManager.findFragmentByTag("dialog")
        if (prev != null) fragmentTransaction.remove(prev)

        fragmentTransaction.addToBackStack(null)

        val dialogFragment = UserSearchFragment()

        val bundle = Bundle()
        bundle.putString("TRIP_BUNDLE", trip.tripId)
        dialogFragment.arguments = bundle

        dialogFragment.show(fragmentTransaction, "dialog")
    }


    /*-----------------------------

    DATA QUERIES

    ---------------------------- */


    private fun configureViewModel() {

        tripViewModel = ViewModelProvider(this).get(FirestoreTripViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(FirestoreUserViewModel::class.java)
        cityViewModel = ViewModelProvider(this).get(FirestoreCityViewModel::class.java)

    }


    @Subscribe
    fun onEvent(event: Utils.AdapterEvent) {

        when (event.adapterId) {

            0 -> tripViewModel.removePhotoFromTrip(trip.tripId, event.data)

            1 -> tripViewModel.removeBuddyFromTrip(trip.tripId, event.data).addOnSuccessListener {
                userViewModel.removeTripFromUser(event.data, trip.tripId)
            }

            2 -> tripViewModel.removeCityFromTrip(trip.tripId, event.data).addOnSuccessListener {
                cityViewModel.removeTripFromCity(event.data, trip.tripId)
            }
        }
    }


    // Get destinations list from trip
    private fun getDestinationsList(trip: Trip) {

        val list: MutableList<City> = ArrayList()

        for (doc in trip.destinationsList) {

            cityViewModel.getCity(doc).observe(viewLifecycleOwner, Observer { it ->
                if (it != null && !list.contains(it)) {
                    list.add(it)
                    list.sortBy { it.name }
                    destinationsAdapter.updateData(list)
                }
            })
        }
    }


    // Get buddies list from trip
    private fun getBuddiesList(trip: Trip) {

        val list: MutableList<User> = ArrayList()

        for (doc in trip.buddiesList) {

            userViewModel.getUser(doc).observe(viewLifecycleOwner, Observer { it ->
                if (it != null && !list.contains(it)) {
                    list.add(it)
                    list.sortBy { it.name }
                    buddiesAdapter.updateData(list)
                }

                // Create blank user if user is deleted
                if (list.isEmpty()) {

                    val deletedUser = User(name = "Deleted user")
                    list.add(deletedUser)
                    buddiesAdapter.updateData(list)
                }
            })
        }
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
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
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

                    // Add photo to trip
                    tripViewModel.addPhotoToTrip(trip.tripId, downloadUri.toString())
                        .addOnSuccessListener {

                            Toast.makeText(activity, "New photo added !", Toast.LENGTH_SHORT).show()
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


            val liveData: LiveData<City> = cityViewModel.getCity(city.cityId)

            liveData.observe(viewLifecycleOwner, Observer {

                if (liveData.hasActiveObservers())
                    liveData.removeObservers(viewLifecycleOwner)

                if (it == null) {
                    // Create city collection
                    cityViewModel.createCityIntoFirestore(city).addOnSuccessListener {
                        // Add trip to city trip list
                        cityViewModel.addTripToCity(city.cityId, trip.tripId).addOnSuccessListener {
                            // Add city to trip destinations list
                            tripViewModel.addCityToTrip(trip.tripId, city.cityId)
                        }
                    }
                } else {
                    // Add trip to city trip list
                    cityViewModel.addTripToCity(city.cityId, trip.tripId).addOnSuccessListener {
                        // Add city to trip destinations list
                        tripViewModel.addCityToTrip(trip.tripId, city.cityId)
                    }
                }

            })
        }
    }


    private fun deleteTrip(trip: Trip) {

        // Delete trip from users
        for (doc in trip.buddiesList) userViewModel.removeTripFromUser(doc, trip.tripId)

        // Delete trip from cities
        for (doc in trip.destinationsList) cityViewModel.removeTripFromCity(doc, trip.tripId)

        // Delete trip from Firestore
        tripViewModel.deleteTripFromFirestore(trip.tripId).addOnSuccessListener {

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

