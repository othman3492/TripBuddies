package com.othman.tripbuddies.models

import com.google.android.gms.location.places.Place


data class City(

    val tripCityId: String,
    var name: String,
    var country: String,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var visitorsList: MutableList<User> = ArrayList(),
    var nbVisitors: Int = visitorsList.size,
    var lastTrips: MutableList<Trip> = ArrayList(),
    var nextTrips: MutableList<Trip> = ArrayList()
)