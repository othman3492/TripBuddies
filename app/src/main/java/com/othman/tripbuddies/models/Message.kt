package com.othman.tripbuddies.models

import com.google.firebase.firestore.ServerTimestamp
import com.othman.tripbuddies.utils.Utils
import java.io.Serializable
import java.util.*

data class Message(

    val messageId: String = "",
    val type: Int = 0,
    var userId: String = "",
    var cityId: String = "",
    var username: String = "",
    var urlProfile: String = "",
    var content: String = "",
    @ServerTimestamp
    var timestamp: Date? = null
): Serializable {

    companion object {

        const val MY_MESSAGE = 0
        const val OTHER_MESSAGE = 1
    }

    constructor():this(Utils.generateId(), 0,"", "", "", "", "")
}