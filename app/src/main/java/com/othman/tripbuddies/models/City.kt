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
    var tripList: MutableList<String> = ArrayList(),
    var visitorsList: MutableList<String> = ArrayList(),
    var wishList: MutableList<String> = ArrayList(),
    var messagesList: MutableList<String> = ArrayList(),
    var nbTrips: Int = tripList.size,
    var nbVisitors: Int = visitorsList.size,
    var nbWishList: Int = wishList.size,
    var nbMessages: Int = messagesList.size
) : Serializable {


    constructor() : this("", "", "", null, null, null)

}
