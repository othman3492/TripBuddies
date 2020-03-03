package com.othman.tripbuddies.models

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
    var photosList: MutableList<String> = ArrayList(),
    var destinationsList: MutableList<String> = ArrayList(),
    var buddiesList: MutableList<String> = ArrayList(),
    var nbDestinations: Int = destinationsList.size,
    var nbBuddies: Int = buddiesList.size,
    var nbPhotos: Int = photosList.size
    ) : Serializable {

    constructor() : this(Utils.generateId(), "", "", "", "", "", "", "")
}