package com.example.graphicdesign.ui.my

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graphicdesign.logic.model.InfoContent
import com.example.graphicdesign.logic.model.InfoResponse
import com.example.graphicdesign.logic.network.InfoService
import com.example.graphicdesign.logic.network.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewModel : ViewModel() {
    private val _personalInfoLive = MutableLiveData<InfoContent>()
    val personalInfoLive:LiveData<InfoContent> get() =  _personalInfoLive

    fun getInfo(token:String) {
        val infoService = ServiceCreator.create<InfoService>(token)
        infoService.getInfo().enqueue(object : Callback<InfoResponse> {
            override fun onResponse(call: Call<InfoResponse>, response: Response<InfoResponse>) {
                val body = response.body()
                if (body != null) {
                    _personalInfoLive.postValue(body.content)
                }
            }

            override fun onFailure(call: Call<InfoResponse>, t: Throwable) {
                Log.d("my",t.toString())
            }

        })
    }
}