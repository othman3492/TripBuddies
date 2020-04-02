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
    var departDate: String = "",
    var returnDate: String? = null,
    var photosList: MutableList<String> = ArrayList(),
    var destinationsList: MutableList<String> = ArrayList(),
    var buddiesList: MutableList<String> = ArrayList()
    ) : Serializable {

    constructor() : this(Utils.generateId(), "", "", "", "", "", "", null)


    // Compare objects without comparing lists
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Trip

        if (tripId != other.tripId) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (creationDate != other.creationDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tripId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + creationDate.hashCode()
        return result
    }
}