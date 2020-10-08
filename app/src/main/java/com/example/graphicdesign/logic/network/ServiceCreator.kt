package com.example.graphicdesign.logic.network

import com.example.graphicdesign.OverallApplication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit构建器
 */
object ServiceCreator {
    private const val BASE_URL = OverallApplication.netWorkUrl
    val retrofit: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
    val httpClient = OkHttpClient.Builder()

    /**
     * 创建一个AppService接口
     * 接收一个 className::class.java
     */
    fun <T> create(serviceClass: Class<T>):T = retrofit.build().create(serviceClass)
    inline fun <reified T> create():T = create(T::class.java)
    inline fun <reified T> create(token:String):T {
        val authentication = AuthenticationInterface(token) // 创建一个拦截器对象
        // 判断客户端拦截器有没有这个拦截器存在
        if (!httpClient.interceptors().contains(authentication)) {
            httpClient.addInterceptor(authentication) // 将拦截器添加到一个客户端
            retrofit.client(httpClient.build()) // 将客户端传入发起请求的客户端
        }
        return retrofit.build().create(T::class.java)
    }
}