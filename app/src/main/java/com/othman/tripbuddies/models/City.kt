package com.othman.tripbuddies.models

import android.content.Context
import com.google.android.libraries.places.api.model.Place
import com.othman.tripbuddies.extensions.getCountry
import java.io.Serializable


data class City(

    val cityId: String,
    var name: String,
    var country: String,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var coverPicture: String?,
    var visitorsList: List<User> = ArrayList(),
    var lastTrips: List<Trip> = ArrayList(),
    var wishList: List<User> = ArrayList()
) : Serializable {


    constructor() : this("", "", "", null, null, null, ArrayList(), ArrayList(), ArrayList())

}
