package com.othman.tripbuddies.ui.fragments


import com.othman.tripbuddies.adapters.CityBuddiesAdapter
import com.othman.tripbuddies.adapters.CityTripsAdapter
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.othman.tripbuddies.BuildConfig
import com.othman.tripbuddies.R
import com.othman.tripbuddies.data.model.City
import com.othman.tripbuddies.data.model.Trip
import com.othman.tripbuddies.data.model.User
import com.othman.tripbuddies.ui.activities.ChatActivity
import com.othman.tripbuddies.ui.activities.MainActivity
import com.othman.tripbuddies.util.extensions.getCountry
import com.othman.tripbuddies.util.Connection
import com.othman.tripbuddies.util.FirebaseUserHelper
import com.othman.tripbuddies.viewmodel.FirestoreCityViewModel
import com.othman.tripbuddies.viewmodel.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodel.FirestoreUserViewModel
import kotlinx.android.synthetic.main.fragment_city.*


class CityFragment : Fragment(R.layout.fragment_city) {


    private var city: City? = null
    private lateinit var tripViewModel: FirestoreTripViewModel
    private lateinit var userViewModel: FirestoreUserViewModel
    private lateinit var cityViewModel: FirestoreCityViewModel
    private lateinit var cityBuddiesAdapter: CityBuddiesAdapter
    private lateinit var cityTripsAdapter: CityTripsAdapter



    private val autocompleteRequestCode = 100
    private val coverImageURL =
        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&maxheight=1000&photoreference="


    companion object {
        fun newInstance(city: City?): CityFragment {

            val args = Bundle()
            args.putSerializable("CITY", city)

            val fragment = CityFragment()
            fragment.arguments = args
            return fragment
        }
    }


    /*-----------------------------

    USER INTERFACE

    ---------------------------- */



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize current user
        configureViewModels()
        configureBuddiesRecyclerView()
        configureTripsRecyclerView()

        // Initialize Places for Autocomplete
        if (!Places.isInitialized()) Places.initialize(requireContext(), BuildConfig.google_apikey)
        city_original_search_button.setOnClickListener { configurePlaceAutocomplete() }

        // Display city data if bundle isn't empty, or if saved instance state isn't empty
        if (requireArguments().getSerializable("CITY") != null) {

            city = requireArguments().getSerializable("CITY") as City
            configureUI(city!!)
        }

    }


    @SuppressLint("DefaultLocale", "RestrictedApi")
    private fun configureUI(city: City) {

        var cityToDisplay: City

        cityViewModel.getCity(city.cityId).observe(viewLifecycleOwner, Observer {

            cityToDisplay = it ?: city

            // Display right floating action button
            if (cityToDisplay.wishList.contains(FirebaseUserHelper.getCurrentUser()!!.uid)) {

                add_city_wish_list_floating_action_button.hide()
                remove_city_wish_list_floating_action_button.show()
            } else {

                remove_city_wish_list_floating_action_button.hide()
                add_city_wish_list_floating_action_button.show()
            }

            configureBuddiesRecyclerView()
            configureTripsRecyclerView()

            configureButtons(cityToDisplay)
            getTripList(cityToDisplay)
            getWishList(cityToDisplay)

            // Update UI
            city_original_layout.visibility = View.GONE
            city_fragment_layout.visibility = View.VISIBLE

            city_name.text = cityToDisplay.name.toUpperCase()
            city_country.text = cityToDisplay.country

            Glide.with(this).load(loadStaticMap(cityToDisplay)).into(city_static_map)

            val path =
                coverImageURL + cityToDisplay.coverPicture + "&key=" + BuildConfig.google_apikey
            Glide.with(this).load(path).into(city_cover_picture)

        })


    }


    private fun configureButtons(city: City) {


        val chatIntent = Intent(activity, ChatActivity::class.java)
        chatIntent.putExtra("CHAT_CITY", city)

        city_search_button.setOnClickListener { configurePlaceAutocomplete() }

        open_chat_floating_action_button.setOnClickListener { startActivity(chatIntent) }

        city_last_trips.setOnClickListener {
            configureTripsRecyclerView()
            getTripList(city)
        }
        city_wish_list.setOnClickListener {
            configureBuddiesRecyclerView()
            getWishList(city)
        }


        // Activate like button if network is available
        if (Connection.checkNetworkState(requireActivity())) {

            add_city_wish_list_floating_action_button.setOnClickListener { addCityToWishList(city) }
            remove_city_wish_list_floating_action_button.setOnClickListener {
                removeCityFromWishList(city)
            }
        } else {
            add_city_wish_list_floating_action_button.setOnClickListener {

                Toast.makeText(activity, "No network available", Toast.LENGTH_SHORT).show()
            }
            remove_city_wish_list_floating_action_button.setOnClickListener {

                Toast.makeText(activity, "No network available", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun configureTripsRecyclerView() {

        city_buddies_recycler_view.visibility = View.GONE
        city_trips_recycler_view.visibility = View.VISIBLE

        // Configure trips RecyclerView
        cityTripsAdapter =
            CityTripsAdapter(requireContext()) { trip: Trip -> openTripFragmentOnClick(trip) }
        city_trips_recycler_view.adapter = cityTripsAdapter
        city_trips_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        city_trips_recycler_view.addItemDecoration(
            DividerItemDecoration(
                city_trips_recycler_view.context, DividerItemDecoration.VERTICAL
            )
        )
    }


    private fun configureBuddiesRecyclerView() {

        city_trips_recycler_view.visibility = View.GONE
        city_buddies_recycler_view.visibility = View.VISIBLE

        // Configure cities RecyclerView
        cityBuddiesAdapter =
            CityBuddiesAdapter(requireContext()) { user: User -> openProfileFragmentOnClick(user) }
        city_buddies_recycler_view.adapter = cityBuddiesAdapter
        city_buddies_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        city_buddies_recycler_view.addItemDecoration(
            DividerItemDecoration(
                city_buddies_recycler_view.context, DividerItemDecoration.VERTICAL
            )
        )
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
            .build(requireContext())
        startActivityForResult(intent, autocompleteRequestCode)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == autocompleteRequestCode) {
            if (resultCode == RESULT_OK) {

                // Retrieve Place data and create City object
                val place = Autocomplete.getPlaceFromIntent(data!!)
                this.city = City(
                    place.id!!,
                    place.name!!,
                    place.getCountry(requireContext()),
                    place.latLng!!.latitude,
                    place.latLng!!.longitude,
                    place.photoMetadatas!![0].zza()
                )

                cityViewModel.getCity(city!!.cityId).observe(viewLifecycleOwner, Observer {

                    if (it == null) cityViewModel.createCityIntoFirestore(city!!)
                })

                configureUI(city!!)
            }
        }
    }


    // Configure static map URI from City object
    private fun loadStaticMap(city: City): String {

        val location = "${city.latitude}, ${city.longitude}"

        // Set center of the map
        val mapURLInitial = "https://maps.googleapis.com/maps/api/staticmap?center=$location"
        // Set properties and marker
        val mapURLProperties = "&zoom=4&size=200x200&markers=size:tiny%7C$location"
        val key = "&key=${BuildConfig.google_apikey}"

        return mapURLInitial + mapURLProperties + key
    }


    // Open Trip details fragment when clicked
    private fun openTripFragmentOnClick(trip: Trip) {

        (activity as MainActivity).displayFragment(TripFragment.newInstance(trip))
    }


    // Open User profile fragment when clicked
    private fun openProfileFragmentOnClick(user: User) {

        (activity as MainActivity).displayFragment(ProfileFragment.newInstance(user.userId))
    }


    /*-----------------------------

    DATA QUERIES

    ---------------------------- */


    private fun configureViewModels() {

        tripViewModel = ViewModelProvider(this).get(FirestoreTripViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(FirestoreUserViewModel::class.java)
        cityViewModel = ViewModelProvider(this).get(FirestoreCityViewModel::class.java)
    }


    // Get trip list from city
    private fun getTripList(city: City) {

        val list: MutableList<Trip> = ArrayList()

        for (doc in city.tripList) {

            tripViewModel.getTrip(doc).observe(viewLifecycleOwner, Observer { it ->
                if (it != null && !list.contains(it)) {
                    list.add(it)
                    list.sortByDescending { it.departDate }
                    cityTripsAdapter.updateData(list)
                }
            })
        }
    }


    // Get wish list from city
    private fun getWishList(city: City) {

        val list: MutableList<User> = ArrayList()

        for (doc in city.wishList) {

            userViewModel.getUser(doc).observe(viewLifecycleOwner, Observer { it ->
                if (it != null && !list.contains(it)) {
                    list.add(it)
                    list.sortBy { it.name }
                    cityBuddiesAdapter.updateData(list)
                }
            })
        }
    }


    @SuppressLint("RestrictedApi")
    private fun addCityToWishList(city: City) {


        // Add city to current user wish list
        userViewModel.addCityToWishList(FirebaseUserHelper.getCurrentUser()!!.uid, city.cityId)
            .addOnSuccessListener {

                // Add user to city wish list
                cityViewModel.addUserToWishList(
                    city.cityId,
                    FirebaseUserHelper.getCurrentUser()!!.uid
                )
                    .addOnSuccessListener {

                        // Update button
                        add_city_wish_list_floating_action_button.hide()
                        remove_city_wish_list_floating_action_button.show()
                        cityBuddiesAdapter.notifyDataSetChanged()

                        Toast.makeText(activity, "City added to wish list !", Toast.LENGTH_SHORT)
                            .show()

                    }
            }

    }


    private fun removeCityFromWishList(city: City) {

        // Remove city from user wish list
        userViewModel.removeCityFromWishList(FirebaseUserHelper.getCurrentUser()!!.uid, city.cityId)
            .addOnSuccessListener {

                // Remove user from city wish list
                cityViewModel.removeUserFromWishList(
                    city.cityId,
                    FirebaseUserHelper.getCurrentUser()!!.uid
                )
                    .addOnSuccessListener {

                        // Update button
                        remove_city_wish_list_floating_action_button.hide()
                        add_city_wish_list_floating_action_button.show()
                        cityBuddiesAdapter.notifyDataSetChanged()

                        Toast.makeText(
                            activity,
                            "City removed from wish list !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
    }

}