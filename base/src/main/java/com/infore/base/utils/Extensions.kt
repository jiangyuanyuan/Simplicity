package com.infore.base.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * 防止重复点击
 */
var lastTime = 0L

fun View.click(commit: () -> Unit) {

    setOnClickListener {
        if (System.currentTimeMillis() - lastTime < 200) {
            return@setOnClickListener
        }
        lastTime = System.currentTimeMillis()
        commit.invoke()
    }
}
fun View.clickLong(commit: () -> Unit) {

    setOnClickListener {
        if (System.currentTimeMillis() - lastTime < 1000) {
            return@setOnClickListener
        }
        lastTime = System.currentTimeMillis()
        commit.invoke()
    }
}

fun Activity.copyText(data: String) {

    try {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("xwow", data)
        clipboardManager.setPrimaryClip(clipData)

    }catch (e:java.lang.Exception){

    }


}




/**
 * 设置 EditText 是否为可编辑状态。
 */
fun EditText?.setEditable(editable: Boolean) {
    if (editable) {
        this?.isFocusable = true
        this?.isEnabled = true
        this?.requestFocus()
        this?.setSelection(this.text.length)
    } else {
        this?.isFocusable = false
        this?.isEnabled = false
    }
}



/**
 * 判断 TextView 是否为空。(TextView / EditText / Button)
 */
fun TextView?.isEmpty(): Boolean {
    return this?.text.isNullOrEmpty()
}

/**
 * 返回 TextView trim 后的内容。(TextView / EditText / Button)
 */
fun TextView?.trimText(): String {
    return (this?.text ?: "").toString().trim()
}

/**
 * 设置是否可以点击（TextView / Button）
 */
fun TextView?.setClickStatus(enable: Boolean, alpha: Float = 0.5f) {
    if (enable) {
        this?.alpha = 1f
        this?.setTextColor(this.textColors.withAlpha(255))
        this?.isClickable = true
    } else {
        this?.alpha = alpha
        this?.setTextColor(this.textColors.withAlpha((alpha * 255).toInt()))
        this?.isClickable = false
    }
}


fun String.hide(beforeLength: Int, afterLength: Int): String {
    if (this.isNullOrBlank()) {
        return this
    }
    var tempAfterLength = afterLength
    var tempBeforeLength = beforeLength
    val length = this.length
    //替换字符串，当前使用“*”
    val replaceSymbol = "*"
    val sb = StringBuffer()
    if (this?.contains("@") == true) {
        tempAfterLength = this?.indexOf("@")?.let { this?.substring(it, this.length)?.length }!!
        tempBeforeLength = 4
    }
    for (i in 0 until length) {
        if (i < tempBeforeLength || i >= length - tempAfterLength) {
            sb.append(this[i])
        } else {
            if (!sb?.toString()?.contains(replaceSymbol)!!) {
                sb.append("****")
            }

        }
    }
    return sb.toString()
}



fun EditText?.Text(string: String?) {
    if (string == null || string?.isNullOrBlank() == true) return
    this?.setText(string.toCharArray(), 0, string?.length ?: 0)
    this?.setSelection(this?.text?.length ?: 0)
}

fun EditText?.Text(): String {
    return this?.text?.toString() ?: ""
}
fun EditText?.TextNoTrim(): String {
    return this?.text?.toString() ?: ""
}

/**
 * 返回当前程序版本号
 */
fun  Activity.getAppVersionCode( ):String {
    var versioncode = 0;
    try {
        val pm = this?.packageManager
        val pi = this?.packageName?.let { pm?.getPackageInfo(it, 0) };
        // versionName = pi.versionName;
        versioncode = pi?.versionCode?:0;
    } catch (e: Exception) {
    }
    return versioncode.toString() + "";
}

/**
 * 返回当前程序versionName
 */
fun  Activity.getAppVersionName( ):String {
    var versionname = ""
    try {
        val pm = this?.packageManager
        val pi = this?.packageName?.let { pm?.getPackageInfo(it, 0) };
        // versionName = pi.versionName;
        versionname = pi?.versionName?:"";
    } catch (e: Exception) {
    }
    return versionname.toString() + "";
}


fun Activity.getConnectWifiSsid(): String? {
    val wifiManager: WifiManager? = applicationContext.getSystemService(WIFI_SERVICE) as? WifiManager
    val wifiInfo: WifiInfo? = wifiManager?.connectionInfo
    return wifiInfo?.ssid
}

/**
 * TextView 加载 html 带格式的内容 。
 */
fun TextView.loadHtml(html: String) {
    this.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(html)
    }
}



/***
 * 显示自己填充视图的AlertDialog
 */

fun Activity. showCustomAlertDialog(
    view_res_id: Int,
    bind_view_listener: (AlertDialog) -> Unit = {},
    window_gravity: Int = Gravity.CENTER,
    canCancel: Boolean = true,
    isShowStatusBar: Boolean = true,
    block: () -> Unit = {}
) :AlertDialog{
    val your_dialog = AlertDialog.Builder(this).create()
    your_dialog.setView(this.layoutInflater.inflate(view_res_id, null))                      // 此处添加此句，可以让软键盘正常弹出来。
    your_dialog.setCancelable(canCancel)
    your_dialog.show()

    bind_view_listener(your_dialog)                                                                                         //绑定任意View操作。

    your_dialog?.window?.setBackgroundDrawable(BitmapDrawable())                                    //设置背景图片。
    your_dialog?.window?.setLayout(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )                //设置长宽比例。
    // 设置弹出的动画效果
//    your_dialog?.window?.setWindowAnimations(R.style.AnimBottom)
    your_dialog?.setCanceledOnTouchOutside(canCancel)
    val window_mgr_lp = your_dialog?.window?.attributes
    val d: DisplayMetrics = this.getResources().getDisplayMetrics() // 获取屏幕宽、高用

    window_mgr_lp?.width = (d.widthPixels * 0.5).toInt() // 宽度设置为屏幕的0.9

//    your_dialog.setAttributes(lp)
    window_mgr_lp?.gravity = window_gravity
    if (isShowStatusBar){
        your_dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }else {
        your_dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    your_dialog?.window?.attributes = window_mgr_lp                                                 //设置在底部进行显示。
    return your_dialog
}


fun Activity.ShowAlertDialog(view_res_id: Int, bind_view_listener: (AlertDialog) -> Unit = {}, window_gravity: Int = Gravity.BOTTOM, canCancel: Boolean = true):AlertDialog {
    val your_dialog = AlertDialog.Builder(this).create()
    your_dialog.setView(this.layoutInflater.inflate(view_res_id, null))
    your_dialog.setCancelable(canCancel)
    your_dialog.show()

    bind_view_listener(your_dialog)                                                                                         //绑定任意View操作。

    your_dialog?.window?.setBackgroundDrawable(BitmapDrawable())                                    //设置背景图片。
    your_dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)                //设置长宽比例。
//    your_dialog?.window?.setWindowAnimations(R.style.AnimBottom)                                    // 设置弹出的动画效果
    your_dialog?.setCanceledOnTouchOutside(canCancel)
    val window_mgr_lp = your_dialog?.window?.attributes
    window_mgr_lp?.gravity = window_gravity
    your_dialog?.window?.attributes = window_mgr_lp
    return your_dialog
//设置在底部进行显示。

}

fun Activity.ShowAlertMatchDialog(view_res_id: Int, bind_view_listener: (AlertDialog) -> Unit = {}, window_gravity: Int = Gravity.BOTTOM, canCancel: Boolean = true):AlertDialog {
    val your_dialog = AlertDialog.Builder(this).create()
    your_dialog.setView(this.layoutInflater.inflate(view_res_id, null))
    your_dialog.setCancelable(canCancel)
    your_dialog.show()

    bind_view_listener(your_dialog)                                                                                         //绑定任意View操作。

    your_dialog?.window?.setBackgroundDrawable(BitmapDrawable())                                    //设置背景图片。
    your_dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)                //设置长宽比例。
//    your_dialog?.window?.setWindowAnimations(R.style.AnimBottom)                                    // 设置弹出的动画效果
    your_dialog?.setCanceledOnTouchOutside(canCancel)
    val window_mgr_lp = your_dialog?.window?.attributes
    window_mgr_lp?.gravity = window_gravity
    your_dialog?.window?.attributes = window_mgr_lp
    return your_dialog
//设置在底部进行显示。

}

fun Activity.ShowCenterAlertDialog(view_res_id: Int, bind_view_listener: (AlertDialog) -> Unit = {}, window_gravity: Int = Gravity.CENTER, canCancel: Boolean = true) {
    val your_dialog = AlertDialog.Builder(this).create()
    your_dialog.setView(this.layoutInflater.inflate(view_res_id, null))
    your_dialog.setCancelable(canCancel)
    your_dialog.show()

    your_dialog?.window?.setContentView(LayoutInflater.from(this).inflate(view_res_id, null))               //加载任意视图。
    bind_view_listener(your_dialog)                                                                                         //绑定任意View操作。

    your_dialog?.window?.setBackgroundDrawable(BitmapDrawable())                                    //设置背景图片。
    your_dialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)                //设置长宽比例。
//    your_dialog?.window?.setWindowAnimations(R.style.AnimBottom)                                    // 设置弹出的动画效果
    your_dialog?.setCanceledOnTouchOutside(canCancel)
    val window_mgr_lp = your_dialog?.window?.attributes
    window_mgr_lp?.gravity = window_gravity
    your_dialog?.window?.attributes = window_mgr_lp
//设置在底部进行显示。

}


fun getSecondTimestampTwo(date: Date?): Int {
    if (null == date) {
        return 0
    }
    val timestamp: String = java.lang.String.valueOf(date.getTime() / 1000)
    return Integer.valueOf(timestamp)
}

/**
 * 压缩图片到目标大小以下
 *
 * @param file
 * @param targetSize
 */
fun compressBmpFileToTargetSize(file: File, targetSize: Long) {
    if (file.length() > targetSize) {
        // 每次宽高各缩小一半
        val ratio = 8
        // 获取图片原始宽高
        val options = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeFile(file.absolutePath, options)
        var targetWidth = options.outWidth / ratio
        var targetHeight = options.outHeight / ratio

        // 压缩图片到对应尺寸
        val baos = ByteArrayOutputStream()
        val quality = 20
        var result: Bitmap? = generateScaledBmp(bitmap, targetWidth, targetHeight, baos, quality)

        // 计数保护，防止次数太多太耗时。
        var count = 0
        while (baos.size() > targetSize && count <= 10) {
            targetWidth /= ratio
            targetHeight /= ratio
            count++

            // 重置，不然会累加
            baos.reset()
            result = generateScaledBmp(result!!, targetWidth, targetHeight, baos, quality)
        }
        try {
            val fos = FileOutputStream(file)
            fos.write(baos.toByteArray())
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

/**
 * 图片缩小一半
 *
 * @param srcBmp
 * @param targetWidth
 * @param targetHeight
 * @param baos
 * @param quality
 * @return
 */
private fun generateScaledBmp(srcBmp: Bitmap, targetWidth: Int, targetHeight: Int, baos: ByteArrayOutputStream, quality: Int): Bitmap? {
    val result = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(result)
    val rect = Rect(0, 0, result.width, result.height)
    canvas.drawBitmap(srcBmp, null, rect, null)
    if (!srcBmp.isRecycled) {
        srcBmp.recycle()
    }
    result.compress(Bitmap.CompressFormat.JPEG, quality, baos)
    return result
}
  fun Activity?.isDestroy( ):Boolean {
    return this == null ||
            this?.isFinishing ==true ||
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && this?.isDestroyed()==true)
}

fun Activity?.killProgress() {
    val startMain =  Intent(Intent.ACTION_MAIN);
    startMain.addCategory(Intent.CATEGORY_HOME);
    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    this?.startActivity(startMain);
    //杀死该应用进程
    android.os.Process.killProcess(android.os.Process.myPid());
    System.exit(0);
}

fun isFirmwareSupport(currentVersion: String?,minVersion:String?):Boolean {
    try {
        var tempCurrentVersionArray = currentVersion?.split("_")
        var tempMinVersionArray = minVersion?.split("_")
        if ((tempCurrentVersionArray?.size ?: 0) > 1&&(tempMinVersionArray?.size ?: 0) > 1) {
            val tempCurrentVersion = tempCurrentVersionArray?.get(1)?.toInt() ?: 0
            val tempMinVersion = tempMinVersionArray?.get(1)?.toInt() ?: 0
            if (tempCurrentVersion >=  tempMinVersion) {
                //走正常
                return true
            } else {
                //走beta
                return false
            }
        }
    }catch (e:Exception){
        return false
    }
    return false
}




fun Activity?.getTopApp(mContext: Context): Boolean {
    try {
        val lockAppName = this?.javaClass?.name
        var topActivityName = ""
        val am = mContext
            .getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = am
            .getRunningTasks(1)
        if (runningTasks != null && !runningTasks.isEmpty()) {
            val taskInfo = runningTasks[0]
            topActivityName = taskInfo.topActivity!!.className
        }
        return lockAppName == topActivityName
    }catch (e:Exception){
        return false
    }

}

fun Activity.isOpenPermission():Boolean{
    val permission1 = PackageManager.PERMISSION_GRANTED ==
            packageManager.checkPermission("android.permission.ACCESS_COARSE_LOCATION", "com.zbeetle.www")
    val permission2 = PackageManager.PERMISSION_GRANTED ==
            packageManager.checkPermission("android.permission.ACCESS_FINE_LOCATION", "com.zbeetle.www")
    val permission3 = PackageManager.PERMISSION_GRANTED ==
            packageManager.checkPermission("android.permission.ACCESS_NETWORK_STATE", "com.zbeetle.www")
    val permission4 = PackageManager.PERMISSION_GRANTED ==
            packageManager.checkPermission("android.permission.ACCESS_WIFI_STATE", "com.zbeetle.www")
    val permission5 = PackageManager.PERMISSION_GRANTED ==
            packageManager.checkPermission("android.permission.CHANGE_NETWORK_STATE", "com.zbeetle.www")
    val permission6 = PackageManager.PERMISSION_GRANTED ==
            packageManager.checkPermission("android.permission.CHANGE_WIFI_STATE", "com.zbeetle.www")
    return permission1&&permission2&&permission3&&permission4&&permission5&&permission6
}




