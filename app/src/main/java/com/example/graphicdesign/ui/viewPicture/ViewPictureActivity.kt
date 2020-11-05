package com.example.graphicdesign.ui.viewPicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import com.example.graphicdesign.OverallApplication
import com.example.graphicdesign.R
import com.example.graphicdesign.logic.model.Images
import kotlinx.android.synthetic.main.activity_view_picture.*

/**
 * activity
 * 查看图片
 */
class ViewPictureActivity : AppCompatActivity() {
    // 托管viewModel不用在意何时初始化
    private val viewModel by lazy {
        ViewModelProvider(this).get(ViewPictureViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_picture)
        val viewPictureAdapter = ViewPictureAdapter(this,lifecycleScope) // 创建适配器
        // 设置viewPager2
        viewPager.apply {
            adapter = viewPictureAdapter
            orientation = ORIENTATION_VERTICAL
        }
        // 接收需要查看的图片列表
        intent.getBundleExtra(OverallApplication.images_intent)?.apply {
            val imageList = getParcelableArrayList<Images>(OverallApplication.images_intent)
            val position = getInt(OverallApplication.images_intent_position)
            Log.d("my",imageList.toString())
            if (imageList != null) {
                viewModel.submitData(imageList.toList(),position) // 将列表传递到viewModel
            }
        }
        // 监听liveData
        viewModel.imageLiveData.observe(this, Observer {
            viewPictureAdapter.submitList(it) // 将数据提交到适配器
            viewPager.setCurrentItem(viewModel.position,false) // 让viewPager2移动到要查看的图片位子
        })

    }
    // 重写系统返回键事件，摧毁当前activity
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}