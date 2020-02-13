package com.othman.tripbuddies.models

import com.google.android.libraries.places.api.model.Place
import com.othman.tripbuddies.utils.Utils
import java.io.Serializable
import kotlin.random.Random


data class User(

    var userId: String = "",
    var name: String = "",
    var presentation: String? = null,
    var urlPicture: String? = null,
    var urlCoverPicture: String? = null,
    var tripList: List<Trip> = ArrayList(),
    var wishList: List<Place> = ArrayList()
): Serializable {

    constructor(): this(Utils.generateId(), "", "", "", "", ArrayList(), ArrayList())
}
