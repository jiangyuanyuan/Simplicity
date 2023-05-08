package com.infore.base.common

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    private lateinit var _binding: T
    protected val binding get() = _binding;

    override fun onCreate(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).fullScreen(true).statusBarAlpha(0.0f).init()
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(_binding.root)

        initViews()
        initEvents()
    }

    protected abstract fun getViewBinding(): T
    open fun initViews() {}
    open fun initEvents() {}




    fun toast(str:String){
        runOnUiThread {
            Toast.makeText(this,str, Toast.LENGTH_SHORT).show()
        }
    }

}
