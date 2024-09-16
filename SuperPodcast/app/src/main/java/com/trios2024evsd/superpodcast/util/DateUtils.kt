package com.trios2024evsd.superpodcast.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun jsonDateToShortDate(jsonDate: String?): String {
        //1
        if (jsonDate == null) {
            return "-"
        }

        // 2
        val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        // 3
        val date = inFormat.parse(jsonDate) ?: return "-"
        // 4
        val outputFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
        // 6
        return outputFormat.format(date)
    }

    fun xmlDateToDate(dateString: String?): Date {
        val date = dateString ?: return Date()
        val inFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault())
        return inFormat.parse(date) ?: Date()
    }

    fun dateToShortDate(date: Date): String {
        val outputFormat = DateFormat.getDateInstance(
            DateFormat.SHORT, Locale.getDefault())
        return outputFormat.format(date)
    }


}
