package com.example.graphicdesign.ui.logon

import android.content.Context
import android.content.SharedPreferences
import android.telecom.Call
import android.util.JsonToken
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graphicdesign.OverallApplication
import com.example.graphicdesign.logic.model.LogonResponse
import com.example.graphicdesign.logic.network.LogonService
import com.example.graphicdesign.logic.network.ServiceCreator
import retrofit2.Callback
import retrofit2.Response
enum class LoginStatus{
    LOGIN_SUCCESSFUL, // 登录成功状态
    LOGIN_FAILED // 登录失败状态
}
class SignInViewModel : ViewModel() {
    private val shpName = OverallApplication.shp_name
    private val shpDataToken = OverallApplication.shp_token
    private val shpDataAccount = OverallApplication.shp_account
    private val shpDataPassword = OverallApplication.shp_password
    private val _loginStatusLiveData = MutableLiveData<LoginStatus>()
    val loginStatusLiveData:LiveData<LoginStatus> get() = _loginStatusLiveData

    fun logon(account: String, password: String) {
        val appService = ServiceCreator.create<LogonService>()
        appService.searchLogon(account,password).enqueue(object :
            Callback<LogonResponse> {
            override fun onResponse(
                call: retrofit2.Call<LogonResponse>,
                response: Response<LogonResponse>
            ) {
                val body = response.body()
                body?.user?.apply {
                    Log.d("my",this.toString())
                    OverallApplication.verify_login = true
                    save(this.account, this.password, token)
                    _loginStatusLiveData.value = LoginStatus.LOGIN_SUCCESSFUL
                }
            }

            override fun onFailure(call: retrofit2.Call<LogonResponse>, t: Throwable) {
                Log.d("my",t.toString())
                _loginStatusLiveData.value = LoginStatus.LOGIN_FAILED
            }
        })
    }

    /**
     * 保存token
     */
    fun save(account:String,password: String,token: String) {
        val shp = OverallApplication.context.getSharedPreferences(shpName,Context.MODE_PRIVATE)
        shp.edit().apply {
            putString(shpDataToken,token)
            putString(shpDataAccount,account)
            putString(shpDataPassword,password)
            apply()
        }
    }
}