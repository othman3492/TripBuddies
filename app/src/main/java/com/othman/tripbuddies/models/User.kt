package com.othman.tripbuddies.models

import java.io.Serializable


data class User(

    val userId: String = "",
    var name: String = "",
    var presentation: String? = null,
    var urlPicture: String? = null,
    var urlCoverPicture: String? = null,
    var tripList: MutableList<Trip> = ArrayList(),
    var wishList: MutableList<Trip> = ArrayList()
): Serializable {

    constructor(): this("", "", "", "", "", ArrayList(), ArrayList())
}
