package com.othman.tripbuddies.controllers.fragments


import CityBuddiesAdapter
import CityTripsAdapter
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.othman.tripbuddies.BuildConfig
import com.othman.tripbuddies.R
import com.othman.tripbuddies.extensions.getCountry
import com.othman.tripbuddies.extensions.loadStaticMap
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.viewmodels.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.android.synthetic.main.fragment_profile.*


class CityFragment : Fragment(R.layout.fragment_city) {


    private lateinit var city: Place
    private lateinit var tripViewModel: FirestoreTripViewModel
    private lateinit var userViewModel: FirestoreUserViewModel
    private lateinit var cityBuddiesAdapter: CityBuddiesAdapter
    private lateinit var cityTripsAdapter: CityTripsAdapter


    val AUTOCOMPLETE_REQUEST_CODE = 100
    val COVER_IMAGE_URL =
        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&maxheight=1000&photoreference="


    companion object {
        fun newInstance() = CityFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Places for Autocomplete
        if (!Places.isInitialized()) Places.initialize(context!!, BuildConfig.google_apikey)

        configureViewModels()
        configureButtons()
    }


    private fun configureViewModels() {

        tripViewModel = ViewModelProviders.of(this).get(FirestoreTripViewModel::class.java)
        userViewModel = ViewModelProviders.of(this).get(FirestoreUserViewModel::class.java)
    }


    private fun configureButtons() {

        city_original_search_button.setOnClickListener { configurePlaceAutocomplete() }
        city_search_button.setOnClickListener { configurePlaceAutocomplete() }
        add_city_wish_list_floating_action_button.setOnClickListener { addCityToWishList(city) }
        remove_city_wish_list_floating_action_button.setOnClickListener {
            removeCityFromWishList(
                city
            )
        }
    }


    @SuppressLint("DefaultLocale")
    private fun configureUI(place: Place) {

        val path = COVER_IMAGE_URL + place.photoMetadatas!![1].zza() +
                "&key=" + BuildConfig.google_apikey

        city_name.text = place.name!!.toUpperCase()
        city_country.text = place.getCountry(context!!)



        Picasso.get().load(place.loadStaticMap()).into(city_static_map)
        Picasso.get().load(path).fit().into(city_cover_picture)

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
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                val city = Autocomplete.getPlaceFromIntent(data!!)
                this.city = city

                // Update UI
                city_original_layout.visibility = View.GONE
                city_fragment_layout.visibility = View.VISIBLE
                configureUI(city)
            }
        }
    }


    @SuppressLint("RestrictedApi")
    private fun addCityToWishList(place: Place) {

        userViewModel.getUser(FirebaseUserHelper.getCurrentUser()!!.uid)
            .observe(viewLifecycleOwner, Observer {

                userViewModel.addCityToWishList(it, place).addOnSuccessListener {

                    add_city_wish_list_floating_action_button.visibility = View.GONE
                    remove_city_wish_list_floating_action_button.visibility = View.VISIBLE
                    Toast.makeText(activity, "City added to wish list !", Toast.LENGTH_SHORT).show()
                }
            })
    }


    @SuppressLint("RestrictedApi")
    private fun removeCityFromWishList(place: Place) {

        userViewModel.getUser(FirebaseUserHelper.getCurrentUser()!!.uid)
            .observe(viewLifecycleOwner, Observer {

                it.wishList.toMutableList().remove(place)
                it.wishList.sortedBy { place.name }

                userViewModel.updateUserIntoFirestore(it).addOnSuccessListener {

                    remove_city_wish_list_floating_action_button.visibility = View.GONE
                    add_city_wish_list_floating_action_button.visibility = View.VISIBLE
                    Toast.makeText(activity, "City removed from wish list !", Toast.LENGTH_SHORT).show()
                }
            })
    }

}