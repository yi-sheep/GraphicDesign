package com.example.graphicdesign.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.graphicdesign.OverallApplication
import com.example.graphicdesign.R
import com.example.graphicdesign.logic.model.Images
import kotlinx.android.synthetic.main.activity_view_picture.*
import kotlinx.android.synthetic.main.view_picture_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 查看图片
 * viewPager适配器
 */
class ViewPictureAdapter(
    private val activity: Activity,
    private val lifecycleScope: LifecycleCoroutineScope
) :
    ListAdapter<Images, ViewPictureAdapter.ViewHolder>(DIFFCALLBACK) {
    val context = OverallApplication.context

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    // ListAdapter的比较器
    object DIFFCALLBACK : DiffUtil.ItemCallback<Images>() {
        override fun areItemsTheSame(oldItem: Images, newItem: Images): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Images, newItem: Images): Boolean {
            return oldItem === newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 加载item
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_picture_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position) // 获取数据
        holder.itemView.apply {
            // 加载图片
            Glide.with(this)
                .load(data.url)
                .listener(object : RequestListener<Drawable> {
                    // 加载失败
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false.also {
                            Toast.makeText(
                                context,
                                context.getString(R.string.image_load_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    // 加载成功
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(view_img)
            viewImage_good_number.text = data.likes.number.toString() // 显示点赞数
            // 下载按钮
            viewImage_download_img.setOnClickListener {
                val bitmap = view_img.drawable.toBitmap()
                lifecycleScope.launch {
                    downloadImage(bitmap)
                }
            }
            // 返回
            viewImage_back.setOnClickListener {
                activity.finish()
            }
        }
    }

    /**
     * 下载图片
     */
    private suspend fun downloadImage(bitmap: Bitmap) {
        // 开启协程
        withContext(Dispatchers.IO) {
            // 开启图库路径
            val saveUri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            ) ?: kotlin.run {
                // 开启失败
                MainScope().launch {
                    Toast.makeText(
                        context,
                        context.getString(R.string.download_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@withContext // 跳出协程
            }
            // 保存图片到图库
            context.contentResolver.openOutputStream(saveUri).use {
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, it)) {
                    MainScope().launch {
                        Toast.makeText(
                            context,
                            context.getString(R.string.download_ok),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // 保存失败
                    MainScope().launch {
                        Toast.makeText(
                            context,
                            context.getString(R.string.download_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}