package com.othman.tripbuddies.util

import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

object Utils {

    // Convert date to usable format
    fun convertDate(date: Date): String {

        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE)
        return dateFormat.format(date)
    }

    // Convert date and time to usable format
    fun convertDateAndTime(date: Date): String {

        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.FRANCE)
        return dateFormat.format(date)
    }

    // Create unique ID for objects
    fun generateId(): String {

        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..28)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }


    // EventBus
    class AdapterEvent(val adapterId: Int, val data: String)
}