package com.example.graphicdesign.ui.my

import android.content.ContentProvider
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.graphicdesign.OverallApplication
import com.example.graphicdesign.R
import kotlinx.android.synthetic.main.my_fragment.*

class MyFragment : Fragment() {
    private val shpName = OverallApplication.shp_name
    private val shpDataToken = OverallApplication.shp_token

    companion object {
        fun newInstance() = MyFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!verifyLogin()) {
            head_img.setImageDrawable(
                ContextCompat.getDrawable(
                    OverallApplication.context,
                    R.drawable.ic_nologon
                )
            )
            user_name.text = "用户名"
            grade.text = "0"
            dynamic_number.text = "0"
            follow_number.text = "0"
            fans_number.text = "0"
            return
        }
        viewModel.personalInfoLive.observe(viewLifecycleOwner, Observer {
            Glide.with(this)
                .load(it.portrait)
                .placeholder(R.drawable.ic_image_search)
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(150)))
                .into(head_img)
            user_name.text = it.name
            grade.text = "0"
            dynamic_number.text = "0"
            follow_number.text = "0"
            fans_number.text = "0"
        })
    }

    //    验证是否登录
    private fun verifyLogin() =
        if (OverallApplication.verify_login) {
            // 已登录
            val shp =
                OverallApplication.context.getSharedPreferences(shpName, Context.MODE_PRIVATE)
            val token = shp.getString(shpDataToken, "") ?: ""
            viewModel.getInfo(token)
            true
        } else {
            false
        }

}