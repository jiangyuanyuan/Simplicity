package com.qiaoqiao.digitalgpt.common

import androidx.viewbinding.ViewBinding
import com.infore.base.common.BaseActivity
import com.qiaoqiao.digitalgpt.data.DataViewModel
import com.qiaoqiao.digitalgpt.data.net.ErrorNotice
import org.koin.androidx.viewmodel.ext.android.viewModel


abstract class AppBaseActivity<T : ViewBinding> :BaseActivity<T>(), ErrorNotice.ErrorListener {
    protected val mViewModel: DataViewModel by viewModel()


    override fun initViews() {
        super.initViews()
        ErrorNotice.INSTANCE.reg(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        ErrorNotice.INSTANCE.unReq(this)
    }


    override fun onNotify(code: Int, msg: String) {
        //统一处理接口返回报错
        toast(msg)
    }
}