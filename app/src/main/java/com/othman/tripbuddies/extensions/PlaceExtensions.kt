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


// Configure static map URI from Place object
fun Place.loadStaticMap(): String {

    val location = "${this.latLng!!.latitude}, ${this.latLng!!.longitude}"

    // Set center of the map
    val mapURLInitial = "https://maps.googleapis.com/maps/api/staticmap?center=$location"
    // Set properties and marker
    val mapURLProperties = "&zoom=4&size=200x200&markers=size:tiny%7C$location"
    val key = "&key=${BuildConfig.google_apikey}"

    return mapURLInitial + mapURLProperties + key
}



