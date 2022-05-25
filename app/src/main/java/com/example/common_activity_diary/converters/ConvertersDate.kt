package com.example.common_activity_diary.converters

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

interface IConvertersDate {
    fun convertStringInDate(date: String?): Date?
    fun convertDateInString(date: Date?): String?
}


class ConvertersDate : IConvertersDate {

    @SuppressLint("SimpleDateFormat")
    override fun convertStringInDate(date: String?): Date? {
        date.let {
            val fmt = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            return fmt.parse(it)
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun convertDateInString(date: Date?): String? {
        date.let {
            val fmt = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            return fmt.format(it)
        }
    }
}