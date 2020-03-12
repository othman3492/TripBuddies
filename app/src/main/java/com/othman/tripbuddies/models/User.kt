package com.othman.tripbuddies.models

import com.google.android.libraries.places.api.model.Place
import com.othman.tripbuddies.utils.Utils
import java.io.Serializable
import kotlin.random.Random


data class User(

    var userId: String = "",
    var name: String = "",
    var urlPicture: String? = null,
    var urlCoverPicture: String? = null,
    var tripList: List<String> = ArrayList(),
    var wishList: List<String> = ArrayList(),
    var visitedCitiesList: MutableList<String> = ArrayList(),
    var nbTrips: Int = tripList.size,
    var nbWishList: Int = wishList.size,
    var nbVisitedCities: Int = visitedCitiesList.size
): Serializable {

    constructor(): this(Utils.generateId(), "", "", "")
}
