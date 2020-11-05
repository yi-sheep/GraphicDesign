package com.example.graphicdesign.logic.network

import com.example.graphicdesign.logic.model.InfoResponse
import retrofit2.Call
import retrofit2.http.GET

interface InfoService {
    @GET("/myInfo")
    fun getInfo():Call<InfoResponse>
}