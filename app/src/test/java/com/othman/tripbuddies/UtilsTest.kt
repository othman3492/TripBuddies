package com.othman.tripbuddies

import com.othman.tripbuddies.utils.Utils
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.util.*

class UtilsTest {


    @Test
    fun convertDate_isCorrect() {

        val date = Date(2020-1900, 1-1,1)
        val result = Utils.convertDate(date)

        assertEquals("2020/01/01", result)
    }

    @Test
    fun convertDateAndTime_isCorrect() {

        val date = Date(2020-1900, 1-1, 1, 15, 30)
        val result = Utils.convertDateAndTime(date)

        assertEquals("2020/01/01 15:30:00", result)
    }
}