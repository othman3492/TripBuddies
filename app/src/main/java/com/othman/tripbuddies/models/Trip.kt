package com.othman.tripbuddies.models

import com.google.android.libraries.places.api.model.Place
import com.othman.tripbuddies.utils.Utils
import java.io.Serializable


data class Trip(

    val tripId: String = "",
    var name: String = "",
    var userId: String,
    var username: String,
    var description: String?,
    var creationDate: String = "",
    var departDate: String?,
    var returnDate: String?,
    var imagesList: List<String> = ArrayList(),
    var nbDestinations: Int = 0,
    var nbBuddies: Int = 0,
    var nbImages: Int = imagesList.size
    ) : Serializable {

    constructor() : this(Utils.generateId(), "", "", "", "", "", "", "")
}