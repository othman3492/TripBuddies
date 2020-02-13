package com.othman.tripbuddies.models

import com.google.android.libraries.places.api.model.Place
import com.othman.tripbuddies.utils.Utils
import java.io.Serializable


data class Trip(

    val tripId: String = "",
    var name: String = "",
    var userId: String,
    var username: String,
    var destination: List<Place> = ArrayList(),
    var description: String?,
    var creationDate: String = "",
    var departDate: String?,
    var returnDate: String?,
    var imageList: List<String> = ArrayList(),
    var buddiesList: List<User> = ArrayList()
    ): Serializable {

    constructor():this(Utils.generateId(), "", "", "", ArrayList(), "", "", "", "", ArrayList(), ArrayList() )
}