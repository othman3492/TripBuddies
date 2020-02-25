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
    var nbTrips: Int = 0,
    var nbCities: Int = 0
): Serializable {

    constructor(): this(Utils.generateId(), "", "", "", "",0,0)
}
