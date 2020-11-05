package com.example.graphicdesign.logic.network

import com.example.graphicdesign.logic.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("/myRelationship")
    fun getUser():Call<UserResponse>
}