package com.othman.tripbuddies.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.othman.tripbuddies.utils.Utils
import com.othman.tripbuddies.utils.Utils.convertDate
import java.io.Serializable
import java.util.*

data class Message(

    val messageId: String = "",
    var userId: String = "",
    var cityId: String = "",
    var username: String = "",
    var urlProfile: String = "",
    var content: String = "",
    var timestamp: String
): Serializable {

    constructor():this(Utils.generateId(), "","", "", "", "", "")
}