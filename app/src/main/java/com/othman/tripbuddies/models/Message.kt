package com.othman.tripbuddies.models

import com.google.firebase.firestore.ServerTimestamp
import com.othman.tripbuddies.utils.Utils
import java.io.Serializable
import java.util.*

data class Message(

    val messageId: String = "",
    val type: Int = 0,
    val userId: String = "",
    val cityId: String = "",
    val username: String = "",
    val urlProfile: String = "",
    var content: String = "",
    @ServerTimestamp
    var timestamp: Date? = null
): Serializable {

    companion object {

        const val MY_MESSAGE = 0
        const val WISHLIST_MESSAGE = 1
        const val VISITOR_MESSAGE = 2
        const val OTHER_MESSAGE = 3
    }

    constructor():this(Utils.generateId(), 0,"", "", "", "", "")
}