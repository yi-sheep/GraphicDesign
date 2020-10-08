package com.example.graphicdesign.ui.pushGraph

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.graphicdesign.OverallApplication
import com.example.graphicdesign.R
import kotlinx.android.synthetic.main.activity_view_picture.*
import kotlinx.android.synthetic.main.push_graph_fragment.*

class PushGraphFragment : Fragment() {

    companion object {
        // 获取碎片的实例
        fun newInstance() = PushGraphFragment()
    }
    //将viewModel托管，不用在意它何时初始化
    val viewModel by lazy {
        ViewModelProvider(this).get(PushGraphViewModel::class.java)
    }
    // 延迟初始化适配器
    private lateinit var pushGraphAdapter: PushGraphAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.push_graph_fragment, container, false) // 加载布局
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pushGraphAdapter = PushGraphAdapter(requireContext(),viewModel) // 促使和适配器
        // 设置recycleView
        pushGraphRecyclerView.apply {
            adapter = pushGraphAdapter // 加载适配器
            // 加载布局为错乱布局
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
        }
        // 监听推图数据liveData
        viewModel.pushGraphListLive.observe(viewLifecycleOwner, Observer {
            // 判断是否需要滚动到顶部
            if (viewModel.newToScrollToTop){
                pushGraphRecyclerView.scrollToPosition(0) // 将recycleView移动到顶部
                viewModel.newToScrollToTop = false // 之后不需要滚动到顶部了
            }
            pushGraphAdapter.submitList(it) // 将liveData的数据提交到适配器中
        })
        // 监听请求状态liveData
        viewModel.netWorkStatusLive.observe(viewLifecycleOwner,{
            pushGraphAdapter.footerViewStatus = it // 将状态保存到适配器中
            pushGraphAdapter.notifyItemChanged(pushGraphAdapter.itemCount-1) // 通知适配器页脚需要发生改变
            // 如果是网络错误，给个Toast提示
            if (it == NetWorkStatus.DATA_NETWORK_ERROR)
                Toast.makeText(OverallApplication.context,getString(R.string.netWork),Toast.LENGTH_SHORT).show()
        })

        // 监听recycleView的滚动
        pushGraphRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            // 负数表示向上滚动，正数表示向下滚动
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 向上滚动跳出onScrolled函数
                if (dy < 0) return
                // 获取布局
                val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                val intArray = IntArray(2) // 创建一个数组
                layoutManager.findLastVisibleItemPositions(intArray) // 获取当前屏幕最后的那个位子并将位子在所有数据中是第几个保存到数组中
                // 判断是不是最后一个
                if (intArray[0] == pushGraphAdapter.itemCount - 1) {
                    viewModel.getPushGraph() // 加载数据
                }
            }
            // 1表示滚动，0表示停止滚动，2表示飞快滚动
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

}