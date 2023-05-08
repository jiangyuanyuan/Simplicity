package com.infore.base.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.Display
import android.view.WindowManager

object PixelUtil {
    private val point = Point()
    fun dp2px(context: Context, dpValue: Float): Int {
        return (context.resources.displayMetrics.density.toInt().toFloat() * dpValue + 0.5f).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 获取屏幕宽度
     *
     * @param context Activity
     * @return ScreenWidth
     */
    fun getScreenWidth(context: Context): Int {
        val display = display(context)
        if (display != null) {
            display.getSize(point)
            return point.x
        }
        return 0
    }

    /**
     * 获取屏幕高度
     *
     * @param context Activity
     * @return ScreenHeight
     */
    fun getScreenHeight(context: Context): Int {
        val display = display(context)
        if (display != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(point)
            } else {
                display.getSize(point)
            }
            //需要减去statusBar的高度  不用考虑navigationBar Display已经自动减去了
            return point.y - getStatusBarHeight(context as Activity)
        }
        return 0
    }

    fun getStatusBarHeight(activity: Activity): Int {
        val resources = activity.resources
        val resourceId = resources.getIdentifier(
            "status_bar_height",
            "dimen",
            "android"
        )
        return resources.getDimensionPixelSize(resourceId)
    }

    fun getNavigationBarHeight(activity: Activity): Int {
        val resources = activity.resources
        val resourceId = resources.getIdentifier(
            "navigation_bar_height",
            "dimen",
            "android"
        )
        return resources.getDimensionPixelSize(resourceId)
    }

    private fun display(context: Context): Display {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return wm.defaultDisplay
    }
}