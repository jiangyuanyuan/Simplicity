package com.qiaoqiao.digitalgpt.data


import com.qiaoqiao.digitalgpt.data.net.BaseParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Copyright (C) 2022-2025, , All rights reserved.
 * Author:       Lenovo
 * Time:         2022/6/21 9:04
 * Create:       2022/6/21 :  DataRepository.java  create by (Lenovo)
 * Changes       2022/6/21 changes by (Lenovo)
 * Description:  数据仓库
 */
class DataRepository(private val api: Api) {

    suspend fun getBanner() =
        withContext(Dispatchers.IO) {
            api.getBanner()
        }


    suspend fun login(baseParam: BaseParam) =
        withContext(Dispatchers.IO) {
            api.login(baseParam)
        }

}