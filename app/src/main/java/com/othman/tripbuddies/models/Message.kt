package com.othman.tripbuddies.models

import com.othman.tripbuddies.utils.Utils
import java.io.Serializable

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