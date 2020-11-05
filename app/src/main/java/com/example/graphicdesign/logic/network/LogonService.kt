package com.example.graphicdesign.logic.network

import com.example.graphicdesign.logic.model.LogonResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 登录请求
 */
interface LogonService {
    @FormUrlEncoded
    @POST("login")
    fun searchLogon(@Field("account") account:String, @Field("password") password:String) :Call<LogonResponse>
}