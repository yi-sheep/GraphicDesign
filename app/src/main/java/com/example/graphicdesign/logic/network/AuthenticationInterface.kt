package com.example.graphicdesign.logic.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 请求拦截器
 * 拦截请求信息，添加token
 */
class AuthenticationInterface(private val token: String) :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request() // 获取到请求
        // 向这个请求中添加一个头部信息
        val header = original.newBuilder()
            .header("Token", token)
        val request = header.build() // 将改好的请求打包
        return chain.proceed(request) // 将打包好的请求发送出去
    }

}