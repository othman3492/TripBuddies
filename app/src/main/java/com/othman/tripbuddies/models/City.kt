package com.othman.tripbuddies.models

import com.google.android.gms.location.places.Place
import java.io.Serializable


data class City(

    val tripCityId: String,
    var name: String,
    var country: String,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var visitorsList: MutableList<User> = ArrayList(),
    var nbVisitors: Int = visitorsList.size,
    var lastTrips: MutableList<Trip> = ArrayList(),
    var wishList: MutableList<City> = ArrayList()
): Serializable {

    constructor(): this ("", "", "", null, null,
        ArrayList(), 0, ArrayList(), ArrayList())
}