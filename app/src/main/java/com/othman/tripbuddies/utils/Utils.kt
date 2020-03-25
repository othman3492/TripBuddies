package com.othman.tripbuddies.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

object Utils {


    fun convertDate(date: Date): String {

        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE)

        return dateFormat.format(date)
    }

    fun convertDateAndTime(date: Date): String {

        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.FRANCE)

        return dateFormat.format(date)
    }


    fun generateId(): String {

        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..28)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}