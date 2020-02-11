package com.othman.tripbuddies.models

import java.io.Serializable


data class User(

    var userId: String = "",
    var name: String = "",
    var presentation: String? = null,
    var urlPicture: String? = null,
    var urlCoverPicture: String? = null,
    var tripList: List<Trip> = ArrayList(),
    var wishList: List<City> = ArrayList()
): Serializable {

    constructor(): this("", "", "", "", "", ArrayList(), ArrayList())
}
