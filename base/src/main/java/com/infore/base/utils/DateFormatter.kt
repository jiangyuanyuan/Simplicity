package com.infore.base.utils

import android.text.TextUtils
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    private const val YYYY_MM_DD_HH_MM = "yyyy-mm-dd HH:mm"
    private val GHOST_FORMATTER = DateFormat.getDateInstance() as SimpleDateFormat
    private val DEFAULT_FORMATTER: DateFormat = SimpleDateFormat(YYYY_MM_DD_HH_MM, Locale.CHINA)
    fun millis2Date(millis: Long): String {
        return DEFAULT_FORMATTER.format(millis)
    }

    fun millis2Date(millis: Long, pattern: String?): String {
        require(!TextUtils.isEmpty(pattern)) { "param pattern could not be null." }
        GHOST_FORMATTER.applyPattern(pattern)
        return GHOST_FORMATTER.format(millis)
    }

    fun date2Millis(date: String?, pattern: String?): Long {
        return try {
            GHOST_FORMATTER.applyPattern(pattern)
            val parse = GHOST_FORMATTER.parse(date)
            parse.time
        } catch (e: ParseException) {
            e.printStackTrace()
            -1
        }
    }

    fun dateTransform(date: String?, originPattern: String?, desPattern: String?): String? {
        return try {
            GHOST_FORMATTER.applyPattern(originPattern)
            val parse = GHOST_FORMATTER.parse(date)
            GHOST_FORMATTER.applyPattern(desPattern)
            GHOST_FORMATTER.format(parse)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    fun second2Date(second: Int): String {
        val sb = StringBuilder()
        var min = second / 60
        var hour = if (min >= 60) min / 60 else 0
        min = if (hour > 0) min % 60 else min
        val day = if (hour >= 24) hour / 24 else 0
        hour = if (day > 0) hour % 24 else hour
        if (day > 0) {
            sb.append(day).append("天")
        }
        if (hour > 0) {
            sb.append(hour).append("时")
        }
        if (min > 0) {
            sb.append(min).append("分")
        }
        sb.append(second % 60).append("秒")
        return sb.toString()
    }
}