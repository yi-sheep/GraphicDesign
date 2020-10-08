package com.example.graphicdesign.logic.network

import com.example.graphicdesign.logic.model.PushGraphResponse
import retrofit2.*
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 推图请求接口
 */
interface PushGraphService {
    @GET("images")
    fun searchPushGraph(@Query("page") page:Int,@Query("count") count:Int):Call<PushGraphResponse>
}