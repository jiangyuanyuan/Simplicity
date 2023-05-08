package com.qiaoqiao.digitalgpt.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import com.infore.base.utils.toJsonString
import com.qiaoqiao.digitalgpt.data.net.BaseParam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class DataViewModel(private val repository: DataRepository) : ViewModel(){

    private fun <T> Flow<T>.doRequest(
        scope: CoroutineScope = viewModelScope
    ) = this.onStart {
        //请求开始前
    }.catch { error->
        //请求报错
        XLog.e(error?.toJsonString())
    }.onCompletion {
        //请求完成
    }


    suspend fun getBanner() = flow {
        emit(repository.getBanner())
    }.doRequest()


    suspend fun login(baseParam: BaseParam) = flow {
        emit(repository.login(baseParam))
    }.doRequest()
}