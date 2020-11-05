package com.example.graphicdesign.ui.pushGraph

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graphicdesign.OverallApplication
import com.example.graphicdesign.OverallApplication.Companion.context
import com.example.graphicdesign.logic.model.ImageId
import com.example.graphicdesign.logic.model.Images
import com.example.graphicdesign.logic.model.PushGraphResponse
import com.example.graphicdesign.logic.model.UserResponse
import com.example.graphicdesign.logic.network.LikeService
import com.example.graphicdesign.logic.network.PushGraphService
import com.example.graphicdesign.logic.network.ServiceCreator
import com.example.graphicdesign.logic.network.UserService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ceil

/**
 * 枚举类保存保存请求状态
 */
enum class NetWorkStatus {
    DATA_CAN_LOAD_MORE,
    DATA_NO_MORE,
    DATA_NETWORK_ERROR,
    LIKE_REQUEST_SUCCEEDED,
    LIKE_REQUEST_FAILED,
    NO_LIKE_REQUEST_SUCCEEDED,
    NO_LIKE_LIKE_REQUEST_FAILED,
}

/**
 * 推图viewModel
 */
class PushGraphViewModel : ViewModel() {
    private val _netWorkStatusLive = MutableLiveData<NetWorkStatus>()
    val netWorkStatusLive: LiveData<NetWorkStatus>
        get() = _netWorkStatusLive
    private val _pushGraphListLive = MutableLiveData<List<Images>>() // 私有可变的liveData类型
    val pushGraphListLive: LiveData<List<Images>> // 公开不可变liveData类型
        get() = _pushGraphListLive // 可以通过类的实例调用get方法获取数据对象
    private val _likeRequestStatusLive = MutableLiveData<NetWorkStatus>() // 反应点赞请求的状态
    val likeRequestStatusLive: LiveData<NetWorkStatus> get() = _likeRequestStatusLive // 提供给外部访问
    private val _noLikeRequestStatusLive = MutableLiveData<NetWorkStatus>() // 反应取消点赞请求的状态
    val noLikeRequestStatusLive:LiveData<NetWorkStatus> get() = _noLikeRequestStatusLive // 提供给外部访问
    var newToScrollToTop = true // 是否滚动到顶部
    private val _likeImageList = MutableLiveData<List<ImageId>>()
    val likeImageList: LiveData<List<ImageId>> get() = _likeImageList
    private val count = OverallApplication.requestNumber

    // 声明变量保存请求的各种状态，这里的都是初始值不用在意
    private var currentPage = 0 // 当前请求的页数
    private var totalPage = 1 // 总页数
    private var isNewQuery = true // 是否是一个新的请求
    private var isLoading = false // 是否是正在加载，防止重复调用

    // 初始化viewModel时调用
    init {
        resetPushGraph() // 请求数据
        verifyLogin() // 验证登录
    }

    private fun resetPushGraph() {
        currentPage = 0 // 让当前请求的页数为第0页
        totalPage = 1 // 让总页数为1，因为这个时候还没有拿到数据的总量
        isNewQuery = true // 是一个新的请求
        newToScrollToTop = true // 需要滚动到顶部
        getPushGraph() // 发起数据请求
    }

    /**
     * 获取推图
     */
    fun getPushGraph() {
        // 如果是正在加载就跳出这个方法
        if (isLoading) return
        // 判断当前页数大于总页数吗
        if (currentPage > totalPage) {
            // 修改请求状态为没有更多数据了
            _netWorkStatusLive.value = NetWorkStatus.DATA_NO_MORE
            // 跳出这个方法
            return
        }
        // 将这次请求改为正在加载
        isLoading = true
        // 请求数据
        val appService = ServiceCreator.create<PushGraphService>()
        appService.searchPushGraph(currentPage, count)
            .enqueue(object : Callback<PushGraphResponse> {
                // 成功
                override fun onResponse(
                    call: Call<PushGraphResponse>,
                    response: Response<PushGraphResponse>
                ) {
                    val body = response.body() // 获取到请求内容
                    // 判空
                    if (body != null) {
                        totalPage =
                            ceil(body.number.toDouble() / currentPage).toInt() // 拿到数据的总量，并保存到总页数
                        // 判断是否为新的请求
                        if (isNewQuery) {
                            _pushGraphListLive.value = body.content // 直接将数据保存到liveData
                        } else {
                            _pushGraphListLive.value = arrayListOf(
                                _pushGraphListLive.value!!,
                                body.content
                            ).flatten() // 将原本的liveData数据和新数据组合成一个新的列表
                        }
                    }
                    // 修改请求状态为加载更多
                    _netWorkStatusLive.value = NetWorkStatus.DATA_CAN_LOAD_MORE
                    isNewQuery = false // 不是一个新的请求
                    currentPage++ // 当前页+1
                    isLoading = false // 这个请求已完成
                }

                // 失败
                override fun onFailure(call: Call<PushGraphResponse>, t: Throwable) {
                    isLoading = false // 这个请求已完成
                    // 修改请求状态为网络错误
                    _netWorkStatusLive.value = NetWorkStatus.DATA_NETWORK_ERROR
                    Log.d("my", t.toString())
                }
            })
    }

    /**
     * 验证登录
     */
    fun verifyLogin() {
        val shp = context.getSharedPreferences(OverallApplication.shp_name, Context.MODE_PRIVATE)
        val token = shp.getString(OverallApplication.shp_token, "") ?: ""
        if (token.isEmpty()) {
            OverallApplication.verify_login = false
            return
        }
        OverallApplication.verify_login = true
        val userService = ServiceCreator.create<UserService>(token)
        userService.getUser().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val body = response.body()
                if (body != null) {
                    Log.d("my", body.content.likes_images.images.toString())
                    _likeImageList.value = body.content.likes_images.images
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("my", t.toString())
            }

        })
    }

    /**
     * 点赞
     */
    fun sendLike(id: Int) {
        val shp = context.getSharedPreferences(OverallApplication.shp_name, Context.MODE_PRIVATE)
        val token = shp.getString(OverallApplication.shp_token, "") ?: ""
        val likeService = ServiceCreator.create<LikeService>(token)
        likeService.sendLike(id).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = response.body()
                if (body != null) {
                    _likeRequestStatusLive.value = NetWorkStatus.LIKE_REQUEST_SUCCEEDED
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("my", t.toString())
                _likeRequestStatusLive.value = NetWorkStatus.LIKE_REQUEST_FAILED
            }
        })
    }

    /**
     * 取消点赞
     */
    fun sendNoLike(id: Int) {
        val shp = context.getSharedPreferences(OverallApplication.shp_name, Context.MODE_PRIVATE)
        val token = shp.getString(OverallApplication.shp_token, "") ?: ""
        val likeService = ServiceCreator.create<LikeService>(token)
        likeService.sendNoLike(id).enqueue(object :Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = response.body()
                if (body != null) {
                    _noLikeRequestStatusLive.value = NetWorkStatus.NO_LIKE_REQUEST_SUCCEEDED
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("my",t.toString())
                _noLikeRequestStatusLive.value = NetWorkStatus.NO_LIKE_LIKE_REQUEST_FAILED
            }
        })
    }
}