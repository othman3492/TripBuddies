package com.othman.tripbuddies.models


data class Trip(

    val tripId: String,
    var name: String,
    var user: User,
    var destination: List<City>,
    var description: String?,
    var creationDate: String,
    var startDate: String?,
    var endDate: String?,
    var imageList: MutableList<String> = ArrayList(),
    var buddiesList: MutableList<User> = ArrayList()

    )