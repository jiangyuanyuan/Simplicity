package com.qiaoqiao.digitalgpt.data.net


class BaseRep <T>(
    val data: T?,
    val code: Int?,
    val msg: String?,
    val successed: String?
){
    fun isSuccessed():Boolean{
        return  code == 200  //和服务端约定的  请求成功code是200
    }
}