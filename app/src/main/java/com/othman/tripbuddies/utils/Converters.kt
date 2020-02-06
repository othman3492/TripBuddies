package com.othman.tripbuddies.utils

import java.text.SimpleDateFormat
import java.util.*

object Converters {


    fun convertDate(date: Date): String {

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

        return dateFormat.format(date)
    }
}