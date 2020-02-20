package com.othman.tripbuddies.controllers.fragments


import CityBuddiesAdapter
import CityTripsAdapter
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
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
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.viewmodels.FirestoreCityViewModel
import com.othman.tripbuddies.viewmodels.FirestoreTripViewModel
import com.othman.tripbuddies.viewmodels.FirestoreUserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.android.synthetic.main.fragment_profile.*


class CityFragment : Fragment(R.layout.fragment_city) {


    private lateinit var city: City
    private lateinit var tripViewModel: FirestoreTripViewModel
    private lateinit var userViewModel: FirestoreUserViewModel
    private lateinit var cityViewModel: FirestoreCityViewModel
    private lateinit var cityBuddiesAdapter: CityBuddiesAdapter
    private lateinit var cityTripsAdapter: CityTripsAdapter


    val AUTOCOMPLETE_REQUEST_CODE = 100
    val COVER_IMAGE_URL =
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Places for Autocomplete
        if (!Places.isInitialized()) Places.initialize(context!!, BuildConfig.google_apikey)

        configureViewModels()
        configureButtons()


        if (arguments!!.getSerializable("CITY") != null) {

            city = arguments!!.getSerializable("CITY") as City
            configureUI(city)
        }
    }


    private fun configureViewModels() {

        tripViewModel = ViewModelProviders.of(this).get(FirestoreTripViewModel::class.java)
        userViewModel = ViewModelProviders.of(this).get(FirestoreUserViewModel::class.java)
        cityViewModel = ViewModelProviders.of(this).get(FirestoreCityViewModel::class.java)
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


    @SuppressLint("DefaultLocale", "RestrictedApi")
    private fun configureUI(city: City) {

        // Update UI
        city_original_layout.visibility = View.GONE
        city_fragment_layout.visibility = View.VISIBLE


        val path = COVER_IMAGE_URL + city.coverPicture +
                "&key=" + BuildConfig.google_apikey

        city_name.text = city.name.toUpperCase()
        city_country.text = city.country
        Picasso.get().load(loadStaticMap(city)).into(city_static_map)
        Picasso.get().load(path).fit().into(city_cover_picture)

        // Display right floating action button
        cityViewModel.getAllCitiesFromUser(FirebaseUserHelper.getCurrentUser()!!.uid)
            .observe(viewLifecycleOwner, Observer {

                for (doc in it) {

                    if (doc.cityId == city.cityId) {

                        add_city_wish_list_floating_action_button.hide()
                        remove_city_wish_list_floating_action_button.show()
                    }
                }
            })


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

                // Retrieve Place data and create City object
                val place = Autocomplete.getPlaceFromIntent(data!!)
                this.city = City(
                    place.id!!,
                    place.name!!,
                    place.getCountry(context!!),
                    place.latLng!!.latitude,
                    place.latLng!!.longitude,
                    place.photoMetadatas!![3].zza(),
                    ArrayList(),
                    ArrayList(),
                    ArrayList())

                configureUI(city)
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


    @SuppressLint("RestrictedApi")
    private fun addCityToWishList(city: City) {

        userViewModel.getUser(FirebaseUserHelper.getCurrentUser()!!.uid)
            .observe(viewLifecycleOwner, Observer {

                cityViewModel.addCityToWishList(it, city).addOnSuccessListener {

                    // Update button
                    add_city_wish_list_floating_action_button.hide()
                    remove_city_wish_list_floating_action_button.show()

                    Toast.makeText(activity, "City added to wish list !", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun removeCityFromWishList(city: City) {

        userViewModel.getUser(FirebaseUserHelper.getCurrentUser()!!.uid)
            .observe(viewLifecycleOwner, Observer {

                cityViewModel.removeCityFromWishList(it, city).addOnSuccessListener {

                    // Update button
                    remove_city_wish_list_floating_action_button.hide()
                    add_city_wish_list_floating_action_button.show()

                    Toast.makeText(activity, "City removed from wish list !", Toast.LENGTH_SHORT).show()
                }
            })
    }

}