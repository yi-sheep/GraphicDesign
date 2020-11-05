package com.example.graphicdesign.logic.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LikeService {
    @FormUrlEncoded
    @POST("images/like")
    fun sendLike(@Field("id")id:Int): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/images/cancellike")
    fun sendNoLike(@Field("id")id:Int):Call<ResponseBody>
}