package com.example.graphicdesign.ui.pushGraph

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.graphicdesign.OverallApplication
import com.example.graphicdesign.R
import com.example.graphicdesign.logic.model.Images
import com.example.graphicdesign.ui.logon.LogOnActivity
import com.example.graphicdesign.ui.viewPicture.ViewPictureActivity
import kotlinx.android.synthetic.main.load_more_item.view.*
import kotlinx.android.synthetic.main.push_graph_item.view.*

/**
 * 推图recycleView适配器
 */
class PushGraphAdapter(
    private val context: Context,
    private val viewModel: PushGraphViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) :
    ListAdapter<Images, PushGraphAdapter.ViewHolder>(DIFFCALLBACK) {
    companion object {
        const val NORMAL_VIEW_TYPE = 0 // 普通的item
        const val LOAD_VIEW_TYPE = 1 // 页脚加载item
    }

    private val shpName = OverallApplication.shp_name
    private val shpDataToken = OverallApplication.shp_token

    // 页脚的显示状态为加载更多
    var footerViewStatus = NetWorkStatus.DATA_CAN_LOAD_MORE

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

    // ViewHolder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    // 重写方法根据判断是否是加载最后一个item，最后一个item需要加载页脚请求状态item
    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) LOAD_VIEW_TYPE else NORMAL_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 判断viewType，它是从getItemViewType方法来的
        return if (viewType == NORMAL_VIEW_TYPE) {
            // 加载推图item
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.push_graph_item, parent, false)
            ).apply {
                itemView.setOnClickListener {
                    Bundle().apply {
                        putParcelableArrayList(
                            OverallApplication.images_intent,
                            ArrayList(currentList)
                        )
                        Log.d("my", ArrayList(currentList).toString())
                        putInt(OverallApplication.images_intent_position, bindingAdapterPosition)
                        val intent = Intent(context, ViewPictureActivity::class.java)
                        intent.putExtra(OverallApplication.images_intent, this)
                        context.startActivity(intent)
                    }
                }
            }
        } else {
            // 加载页脚请求状态item
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.load_more_item, parent, false)
                    // 同时
                    .also {
                        // 将这个错乱布局改为沾满一排
                        (it.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                            true
                        // 给这个页脚添加点击事件，当网络错误时可以点击重试
                        it.setOnClickListener { view ->
                            view.progressBar.visibility = View.VISIBLE // 将加载圈显示出来
                            // 修改显示文字为正在加载
                            view.load_tv.text =
                                OverallApplication.context.resources.getString(R.string.load)
                            // 加载数据
                            viewModel.getPushGraph()
                        }
                    }
            )
        }
    }


    /**
     * 加载item个数
     */
    override fun getItemCount(): Int {
        return super.getItemCount() + 1 // +1表示在原有数据总量的基础上加上了一个页脚
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 判断是不是到加载最后一个item的时候了
        if (position == itemCount - 1) {
            with(holder.itemView) {
                // 根据请求状态显示对应状态
                when (footerViewStatus) {
                    NetWorkStatus.DATA_CAN_LOAD_MORE -> {
                        isClickable = false
                        progressBar.visibility = View.VISIBLE
                        load_tv.text = context.resources.getString(R.string.load)
                    }
                    NetWorkStatus.DATA_NO_MORE -> {
                        isClickable = false
                        progressBar.visibility = View.GONE
                        load_tv.text = context.resources.getString(R.string.loadOk)
                    }
                    NetWorkStatus.DATA_NETWORK_ERROR -> {
                        isClickable = true
                        progressBar.visibility = View.GONE
                        load_tv.text = context.resources.getString(R.string.loadError)
                    }
                }
            }
            // 跳出onBindViewHolder函数
            return
        }
        val pushGraph = getItem(position) // 获取这个item对应的数据
        holder.itemView.imageView.layoutParams.height =
            pushGraph.thumbnailHeight // 根据请求到的缩略图高度改变imageView的高度
        // 加载图片
        Glide.with(holder.itemView)
            .load(pushGraph.thumbnailUrl)
            .placeholder(R.drawable.ic_image_search)
            .into(holder.itemView.imageView)
        // 初始化，避免状态复用
        holder.itemView.apply {
            good_number.text = pushGraph.likes.number.toString() // 显示点赞数
            good_img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_good))
            good_number.setTextColor(context.getColor(R.color.black))
        }
        val likeId = ContentValues()
        viewModel.likeImageList.observe(viewLifecycleOwner, Observer {
            for (id in it) {
                if (id.imageid == pushGraph.id.toString()) {
                    holder.itemView.apply {
                        good_img.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_good_ok
                            )
                        )
                        good_number.setTextColor(context.getColor(R.color.colorPrimaryDark))
                        likeId.put(pushGraph.id.toString(), true)
                    }
                }
            }
        })

        holder.itemView.good_ll.setOnClickListener {
            // 判断是否登录
            if (!OverallApplication.verify_login) {
                context.startActivity(Intent(context, LogOnActivity::class.java))
                Toast.makeText(context, context.getString(R.string.sign_in_no), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val isLike = likeId.getAsBoolean(pushGraph.id.toString())
            if (isLike != null && isLike) {
                viewModel.sendNoLike(pushGraph.id) // 发起取消点赞请求
                likeId.put(pushGraph.id.toString(),false)
                // 修改ui为取消点赞
                holder.itemView.apply {
                    good_img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_good))
                    good_number.apply {
                        setTextColor(context.getColor(R.color.black))
                        text = (good_number.text.toString().toInt() - 1).toString()
                    }
                }
                return@setOnClickListener
            }
            // 发起点赞请求
            viewModel.sendLike(pushGraph.id)
            likeId.put(pushGraph.id.toString(),true)
            // 修改ui为点赞状态
            holder.itemView.apply {
                good_img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_good_ok))
                good_number.apply {
                    setTextColor(context.getColor(R.color.colorPrimaryDark))
                    text = (good_number.text.toString().toInt() + 1).toString()
                }
            }
        }
        // 监听点赞请求状态
        viewModel.likeRequestStatusLive.observe(viewLifecycleOwner, Observer {
            when (it) {
                // 请求成功
                NetWorkStatus.LIKE_REQUEST_SUCCEEDED -> {
                    // 修改ui为点赞状态
                }
                else -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.like_status_no),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        // 监听
        viewModel.noLikeRequestStatusLive.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetWorkStatus.NO_LIKE_REQUEST_SUCCEEDED -> {
                    //
                }
                else -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.cancel_like_status_no),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

}