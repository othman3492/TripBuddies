package com.othman.tripbuddies.models

import com.google.firebase.firestore.ServerTimestamp
import com.othman.tripbuddies.utils.Utils
import java.io.Serializable
import java.util.*

data class Message(

    val messageId: String = "",
    val userId: String = "",
    var content: String = "",
    @ServerTimestamp
    var timestamp: Date? = null
): Serializable {

    constructor():this(Utils.generateId(),"", "", null)
}