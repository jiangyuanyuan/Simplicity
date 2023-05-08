package com.infore.base.utils

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat

/**
 * Created by song on 2022/3/28.
 */

fun View?.show(shown: Boolean, invisible: Boolean = false) {
    this?.visibility = if (shown) View.VISIBLE else {
        if(invisible) View.INVISIBLE else View.GONE
    }
}


fun View?.isVisible(): Boolean = this?.visibility == View.VISIBLE

fun View?.setBgDrawable(drawableId: Int) {
    runCatching {
        val drawable = this?.run { ContextCompat.getDrawable(context, drawableId) }
        setBgDrawable(drawable)
    }
}

fun View?.setBgDrawable(drawable: Drawable?) {
    drawable?.let { this?.setBackground(drawable) }
}