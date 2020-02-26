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
    var nbTrips: Int = 0,
    var nbWishList: Int = 0,
    var nbVisitedCities: Int = 0
): Serializable {

    constructor(): this(Utils.generateId(), "", "", "")
}
