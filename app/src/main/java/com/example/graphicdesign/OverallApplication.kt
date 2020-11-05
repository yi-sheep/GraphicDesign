package com.example.graphicdesign

import android.app.Application
import android.content.Context

/**
 * 全局配置文件
 */
class OverallApplication :Application(){
    companion object{
        lateinit var context: Context // 全局获取context
        const val netWorkUrl = "http://192.168.137.1:3000/" // 请求数据的后端服务器ip 电脑wifi http://192.168.137.1:3000/ 校园网 http://10.200.65.149:3000/
        const val requestNumber = 10 // 一次请求数据的总量
        const val request_write_external_storage = 101 // 写外部存储权限获取码
        const val images_intent = "INTENT_IMAGES" // 传递图片凭证
        const val images_intent_position = "INTENT_IMAGES_POSITION" // 传递图片凭证
        var verify_login = false // 验证登录
        const val shp_name = "Token" // 存放token的SharedPreferences文件名
        const val shp_token = "token" // 存放token的key
        const val shp_account = "account" // 存放账号
        const val shp_password = "password" // 存放密码
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}