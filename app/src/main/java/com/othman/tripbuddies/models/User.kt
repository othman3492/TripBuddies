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
    var tripList: List<String> = ArrayList(),
    var wishList: List<String> = ArrayList()
): Serializable {

    constructor(): this(Utils.generateId(), "", "", "")


    // Compare objects without comparing lists
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (userId != other.userId) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
