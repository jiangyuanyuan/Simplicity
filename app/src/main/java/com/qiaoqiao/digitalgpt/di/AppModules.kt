package com.qiaoqiao.digitalgpt.di


import com.google.gson.Gson
import com.qiaoqiao.digitalgpt.data.Api
import com.qiaoqiao.digitalgpt.data.DataRepository
import com.qiaoqiao.digitalgpt.data.DataViewModel
import com.qiaoqiao.digitalgpt.data.net.RetrofitManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Copyright (C) 2022-2025, , All rights reserved.
 * Author:       Lenovo
 * Time:         2022/6/20 15:15
 * Create:       2022/6/20 :  Modules.java  create by (Lenovo)
 * Changes       2022/6/20 changes by (Lenovo)
 * Description:  注入
 */

val appModule = module {
    single { Gson() }
}

val repositoryModule = module {
    single { RetrofitManager.initRetrofit().getService(Api::class.java) }
    single <DataRepository>{ DataRepository(get<Api>()) }

}

val viewModelModule = module {

    viewModel { DataViewModel(get()) }
}