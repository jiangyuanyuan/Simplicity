package com.qiaoqiao.digitalgpt.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.elvishew.xlog.XLog
import com.infore.base.common.EasyAdapter
import com.infore.base.utils.onclick
import com.infore.base.utils.toJsonString
import com.orhanobut.hawk.Hawk
import com.qiaoqiao.digitalgpt.common.AppBaseActivity
import com.qiaoqiao.digitalgpt.data.net.BaseParam
import com.qiaoqiao.digitalgpt.data.net.BaseRep
import com.qiaoqiao.digitalgpt.databinding.ActivityMainBinding
import com.qiaoqiao.digitalgpt.databinding.ItemListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppBaseActivity<ActivityMainBinding>() {
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)



    private var mAdapter : EasyAdapter<String,ItemListBinding>?=null
    private val mList = mutableListOf<String>()


    override fun initViews() {
        super.initViews()
        //日志打印   可以持久化到文件  方便出问题后查看
        XLog.i("测试log")


        //get 请求
        binding.tvGet.onclick {
            lifecycleScope?.launch {
                mViewModel?.getBanner()?.collectLatest {
                    XLog.d(it)
                    toast(it?.toJsonString())
                    //持久化到本地
                    Hawk.put("GET_KEY",it)
                }
            }
        }
        //持久化的本地获取数据
        binding.tvGetLocal.onclick {
            val local = Hawk.get<BaseRep<Any>>("GET_KEY")
            toast(local?.toJsonString())
        }


        //post 请求
        binding.tvPost.onclick {
            lifecycleScope?.launch {
                val baseParam = BaseParam()
                baseParam["username"] = "test"
                baseParam["password"] = "test"
                mViewModel?.login(baseParam)?.collectLatest {
                    XLog.d(it)
                    toast(it?.toJsonString())
                }
            }

        }



        //list列表的操作
        binding.rvList.layoutManager =  LinearLayoutManager(this)
        mAdapter =  MyAdapter()
        binding.rvList.adapter  = mAdapter

        mList.add("1")
        mList.add("2")
        mList.add("3")
        mList.add("4")
        mList.add("5")
        mAdapter?.submitList(mList)

    }

     class MyAdapter():EasyAdapter<String,ItemListBinding>({
         viewBinding,i,item->
         //数据绑定操作
         viewBinding.tvItem.text = item

     }){
         override fun getViewBinding(parent: ViewGroup): ItemListBinding {
             return ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         }

     }


}