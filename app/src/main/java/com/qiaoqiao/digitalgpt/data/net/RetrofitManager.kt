package com.qiaoqiao.digitalgpt.data.net

import C.BASE_URL_TEST
import android.util.Log
import com.qiaoqiao.digitalgpt.BuildConfig
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

object RetrofitManager {

    private val mOkClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(initLogInterceptor())
        .addInterceptor(ResultkInterceptor())
        .retryOnConnectionFailure(true)
        .followRedirects(false)
        .cookieJar(LocalCookieJar())
        .build()



    private fun initLogInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
        interceptor.level = HttpLoggingInterceptor.Level.NONE
    }
        return interceptor
    }



    private var mRetrofit: Retrofit? = null


    fun initRetrofit(): RetrofitManager {
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_TEST)
            .client(mOkClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return this
    }

    fun <T> getService(serviceClass: Class<T>): T {
        if (mRetrofit == null) {
            throw UninitializedPropertyAccessException("Retrofit必须初始化")
        } else {
            return mRetrofit!!.create(serviceClass)
        }
    }
}


class ResultkInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
            ?.addHeader("Content_Type", "application/json")
            ?.addHeader("charset", "UTF-8")
            ?.addHeader("Connection", "close")

        var response: Response? = null
        try {
            response = chain.proceed(builder!!.build())
        } catch (e: Exception) {
            if (e is SocketTimeoutException)
                ErrorNotice.INSTANCE.notifyError(1408, e.toString())
            if (e is NoRouteToHostException)
                ErrorNotice.INSTANCE.notifyError(1401, "等待主机启动中")

        }

        val bytes = response?.body?.bytes() ?: ""?.toByteArray()
        val build = response?.newBuilder()?.body(ResponseBody.create("UTF-8".toMediaTypeOrNull(), bytes))?.build()
        var responseCode = ""
        var responseMsg = ""
        try {
//            if (bytes != null) {
//                val returnData = JsonParser()?.parse(String(bytes))?.asJsonObject
//                responseCode = returnData?.get("errcode")?.asString ?: "10000"
//                responseMsg = returnData?.get("errmsg")?.asString ?: "server error"
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            //交给上层显示错误
//            if (responseCode?.isNotBlank() && responseCode?.toInt() != 200) {
//                ErrorNotice.INSTANCE.notifyError(responseCode?.toInt(), responseMsg)
//            } else {
//            }
        }
        return if (build!=null){
            build
        }else {
            try {
                chain.proceed(builder!!.build())
            }catch (e:Throwable){
                //其他崩溃异常 伪装成IO 不崩溃
                throw throw IOException("OKhttp when requesting ${chain.request().url}")
            }
        }
    }
}
