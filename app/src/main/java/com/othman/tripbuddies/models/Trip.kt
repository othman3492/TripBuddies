package com.othman.tripbuddies.models

import java.io.Serializable


data class Trip(

    val tripId: String,
    var name: String,
    var user: User,
    var destination: MutableList<City> = ArrayList(),
    var description: String?,
    var creationDate: String,
    var departDate: String?,
    var returnDate: String?,
    var imageList: MutableList<String> = ArrayList(),
    var buddiesList: MutableList<User> = ArrayList()
    ): Serializable {

    constructor():this("", "", User(), ArrayList(), "", "", "", "", ArrayList(), ArrayList() )
}