package com.qiaoqiao.digitalgpt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.elvishew.xlog.XLog
import com.infore.base.common.BaseActivity
import com.infore.base.common.EasyAdapter
import com.infore.base.utils.onclick
import com.infore.base.utils.toJsonString
import com.qiaoqiao.digitalgpt.common.AppBaseActivity
import com.qiaoqiao.digitalgpt.data.net.BaseParam
import com.qiaoqiao.digitalgpt.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppBaseActivity<ActivityMainBinding>() {
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)



    private var mAdapter : EasyAdapter<String>?=null
    private val mList = mutableListOf<String>()


    override fun initViews() {
        super.initViews()
        //日志打印   可以持久化到文件  方便出问题后查看
        XLog.i("测试log")


        //get 请求
        binding.tvGet.onclick {
            lifecycleScope?.launch {
                mViewModel?.getBanner()?.collectLatest {
                    if (it.isSuccessed()){
                        XLog.d(it)
                        toast(it.toJsonString())
                    }
                    XLog.d(it)
                    toast(it.toJsonString())
                }
            }
        }
        //post 请求
        binding.tvPost.onclick {
            lifecycleScope?.launch {
                val baseParam = BaseParam()
                baseParam["username"] = "test"
                baseParam["password"] = "test"
                mViewModel?.login(baseParam)?.collectLatest {
                    if (it.isSuccessed()){
                        XLog.d(it)
                        toast(it.toJsonString())
                    }
                }
            }

        }



        //list列表的操作
        binding.rvList.layoutManager =  LinearLayoutManager(this)
        mAdapter =  getAdapter()
        binding.rvList.adapter  = mAdapter

        mList.add("1")
        mList.add("2")
        mList.add("3")
        mList.add("4")
        mList.add("5")
        mAdapter?.submitList(mList)

    }

    private fun getAdapter():EasyAdapter<String>{
        return EasyAdapter(R.layout.item_list){view, i, item->
            view.findViewById<TextView>(R.id.tv_item).text = item

        }
    }


}