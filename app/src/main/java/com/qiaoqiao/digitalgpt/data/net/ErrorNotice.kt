package com.qiaoqiao.digitalgpt.data.net

import android.app.Activity
import android.content.Context
import android.util.Log


/**
 * Copyright (C) 2022-2025, 敲撬GPT, All rights reserved.
 * Author:       Lenovo
 * Time:         2022/6/21 9:19
 * Create:       2022/6/21 :  ErrorNotice.java  create by (Lenovo)
 * Changes       2022/6/21 changes by (Lenovo)
 * Description:  网络异常通知
 */

class ErrorNotice private constructor() {


    private var mErrorListener: ErrorListener? = null

    fun reg(listener: ErrorListener) {
        this.mErrorListener = listener

    }
    fun unReq(context: Activity){
        if (mErrorListener == context){
            mErrorListener = null
        }
    }

    fun notifyError(code: Int, msg: String) {
        if (null != mErrorListener) {
            mErrorListener!!.onNotify(code, msg)
        }
    }

    interface ErrorListener {
        fun onNotify(code: Int, msg: String)
    }

    companion object {

        val INSTANCE : ErrorNotice by lazy {
            ErrorNotice()
        }

    }
}
