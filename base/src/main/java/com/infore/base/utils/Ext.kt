package com.infore.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.infore.base.Parameter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Copyright (C) 2022-2025, 敲撬GPT, All rights reserved.
 * Author:       Lenovo
 * Time:         2022/6/28 8:57
 * Create:       2022/6/28 :  Ext.java  create by (Lenovo)
 * Changes       2022/6/28 changes by (Lenovo)
 * Description:  拓展类
 */

//暴力点击
var startTime = System.currentTimeMillis()
fun View.onclick(bolck:()->Unit){
    this.setOnClickListener {
        if ((System.currentTimeMillis() - startTime)>300){
            bolck?.invoke()
            startTime= System.currentTimeMillis()
        }
    }
}

public suspend fun <T> withContextIo( block: suspend CoroutineScope.() -> T){
    try {
        kotlinx.coroutines.withContext(Dispatchers.IO,block)
    }catch (e:Exception){
        null
    }
}




fun Activity.navigate(clazz: Class<out Activity>){
    val intent = Intent(this,clazz)
    this.startActivity(intent)
}
fun Activity.navigateWithString(clazz: Class<out Activity>,data: String){
    val intent = Intent(this,clazz)
    intent?.putExtra(Parameter.FIRST,data)
    this.startActivity(intent)
}
fun Activity.navigateWithBundle(clazz: Class<out Activity>,data: Bundle){
    val intent = Intent(this,clazz)
    intent?.putExtras(data)
    this.startActivity(intent)
}

fun Activity.navigateWithFinish(clazz: Class<out Activity>){
    val intent = Intent(this,clazz)
    this.startActivity(intent)
    this.finish()
}


