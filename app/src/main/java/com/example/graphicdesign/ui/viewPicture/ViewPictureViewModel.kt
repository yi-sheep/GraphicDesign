package com.example.graphicdesign.ui.viewPicture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graphicdesign.logic.model.Images

class ViewPictureViewModel:ViewModel() {
    private val _imageLiveData = MutableLiveData<List<Images>>() // 私有的图片列表可变liveData
    val imageLiveData:LiveData<List<Images>> get() = _imageLiveData // 公开的不可变liveData
    private var _position = 0 // 私有的变量
    val position:Int get() = _position // 公开的常量
    // 用于传递图片列表和图片位子
    fun submitData(images: List<Images>,position:Int) {
        _imageLiveData.value = images
        _position = position
    }
}