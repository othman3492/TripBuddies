package com.othman.tripbuddies.models

import java.io.Serializable


data class City(

    val cityId: String,
    var name: String,
    var country: String,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var coverPicture: String?,
    var tripList: List<String> = ArrayList(),
    var wishList: List<String> = ArrayList(),
    var messagesList: MutableList<String> = ArrayList()
) : Serializable {


    constructor() : this("", "", "", null, null, null)


    // Compare objects without comparing lists
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as City

        if (cityId != other.cityId) return false
        if (name != other.name) return false
        if (country != other.country) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cityId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + (latitude?.hashCode() ?: 0)
        result = 31 * result + (longitude?.hashCode() ?: 0)
        return result
    }

}
