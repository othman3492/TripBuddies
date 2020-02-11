package com.othman.tripbuddies.controllers.fragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.othman.tripbuddies.BuildConfig
import com.othman.tripbuddies.R
import kotlinx.android.synthetic.main.fragment_city.*
import java.util.*


class CityFragment : Fragment(R.layout.fragment_city) {


    val AUTOCOMPLETE_REQUEST_CODE = 100


    companion object {
        fun newInstance() = CityFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Places for Autocomplete
        if (!Places.isInitialized()) Places.initialize(context!!, BuildConfig.google_apikey)

        city_search_button.setOnClickListener { configurePlaceAutocomplete() }
    }


    private fun configureUI(place: Place) {

        city_name.text = place.name

    }


    // Retrieve city data from Autocomplete search
    private fun configurePlaceAutocomplete() {

        val fields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.PHOTO_METADATAS)

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
                configureUI(city)
            }
        }
    }


}
