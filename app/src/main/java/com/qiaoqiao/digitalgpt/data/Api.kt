package com.qiaoqiao.digitalgpt.data


import com.qiaoqiao.digitalgpt.data.net.BaseParam
import com.qiaoqiao.digitalgpt.data.net.BaseRep
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {


    @GET("banner/json")
    suspend fun getBanner(): BaseRep<Any>


    @POST("user/login")
    suspend fun login(@Body obj: BaseParam): BaseRep<Any?>
}