package com.othman.tripbuddies.extensions

import android.content.Context
import android.location.Geocoder
import com.google.android.libraries.places.api.model.Place
import com.othman.tripbuddies.BuildConfig


// Retrieve country from Place object
fun Place.getCountry(context: Context): String {

    val geocoder = Geocoder(context)
    val location = this.latLng

    val country = geocoder.getFromLocation(location!!.latitude, location.longitude, 1)

    return country[0].countryName
}



