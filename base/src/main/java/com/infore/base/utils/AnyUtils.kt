package com.infore.base.utils

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.InputStream
import java.util.*

/**
 * created by song on 2021/10/25.
 */
inline fun <reified T> Any?.castObject(): T? {
    return this?.takeIf { this is T }?.run {
        this as T
    } ?: null
}

inline fun <reified T : Activity> Context?.startActivityKtx() {
    this?.startActivity(Intent(this, T::class.java))
}

inline fun <reified T : Service> Context?.startService() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this?.startForegroundService(Intent(this, T::class.java))
    } else {
        this?.startService(Intent(this, T::class.java))
    }
}

inline fun <reified T : Activity> Context.startActivityKtxWithParam(block: (intent: Intent) -> Any) {
    val intent = Intent(this, T::class.java)
    block.invoke(intent)
    this.startActivity(intent)
}


inline fun <reified T> Intent.objFromIntentParam(key: String): T? {
    return getStringExtra(key)?.run {
        this.jsonToObj()
    }
}

inline fun <reified T> Bundle.objFromBundleParam(key: String): T? {
    return getString(key)?.run {
        this.jsonToObj()
    }
}

inline fun <reified T> Any?.objToJsonString(): String {
    var obj: T = this?.takeIf { this is T }.let { it as T }
    val g = Gson()
    return obj?.run {
        g.toJson(obj)
    } ?: g.toJson(Any())
}

inline fun Any?.toJsonString(): String {
    val g = Gson()
    return this?.run {
        g.toJson(this)
    } ?: g.toJson(Any())
}

inline fun <reified T> String.jsonToObj(): T? {
    return try {
        val g = Gson()
        val type = object : TypeToken<T>() {}.type
        g.fromJson<T>(this, type)
    } catch (e: Exception) {
        null
    }
}

fun File.inputStreamToFile(inputStream: InputStream) {
    inputStream.use { input ->
        this.outputStream().use { fileOut ->
            input.copyTo(fileOut)
        }
    }
}

fun String?.toIntSafely(): Int {
    return this?.run {
        kotlin.runCatching { this.toInt() }.getOrNull() ?: 0
    } ?: 0
}

/**
 * 获取url里面请求参数
 */
fun String.getQueryParamsByKey(paramKey: String): String? {
    return getQueryParams(this)?.run {
        get(paramKey)
    } ?: ""
}

/**
 * 获取url里面所有的请求参数
 */
fun getQueryParams(strUrl: String): HashMap<String?, String?>? {
    var queryParams: HashMap<String?, String?>? = null
    if (strUrl != null) {
        val start = strUrl.indexOf('?')
        var substring: String = strUrl
        if (start > 0) {
            substring = strUrl.substring(start + 1, strUrl.length)
        }
        //a=b& Worst
        if (substring.length >= 4) {
            queryParams = HashMap()
            val split = substring.split("&".toRegex()).toTypedArray()
            for (s in split) {
                val pair = s.split("=".toRegex()).toTypedArray()
                if (pair.size == 2) {
                    queryParams[pair[0]] = pair[1]
                }
            }
        }
    }
    return queryParams
}

inline fun Handler?.postDelayed(runnable: Runnable?, delay: Long) {
    this?.run {
        runnable?.also {
            this.removeCallbacks(it)
            this.postDelayed(runnable, delay)
        }
    }
}

inline fun Handler?.postDelayedEx(runnable: Runnable?, delay: Long) {
    this?.run {
        runnable?.also {
            this.removeCallbacks(it)
            this.postDelayed(runnable, delay)
        }
    }
}

inline fun Handler?.removeRunnable(runnable: Runnable?) {
    this?.run {
        runnable?.also {
            this.removeCallbacks(it)
        }
    }
}

/**
 * 如果有小数位，则返回带小数位的string
 * 如果没有小数位，则返回不带小数位的string
 */
inline fun Float.toIntString(): String {
    return if (this % this.toLong() > 0)
        this.toString()
    else this.toLong().toString()
}

inline fun Double.toDoubleString(): String {
    return this.toLong().toString()
}

inline fun String?.toSafeFloat(): Float {
    return if (this.isNullOrEmpty()) 0f else this.toFloat()
}