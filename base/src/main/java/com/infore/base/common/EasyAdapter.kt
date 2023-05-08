package com.infore.base.common

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.infore.base.R


/**
 * 简单Adapter
 */

abstract class EasyAdapter<T,V :ViewBinding>( private val bindView: (V, Int, T) -> Unit) :
        RecyclerView.Adapter<EasyAdapter.ViewHolder<T,V>>() {

    private var mData = mutableListOf<T>()

    private lateinit var _binding: V
    protected val binding get() = _binding;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T,V> {
        _binding = getViewBinding(parent)
        return ViewHolder(binding, bindView)
    }

    protected abstract fun getViewBinding(parent: ViewGroup): V

    override fun onBindViewHolder(holder: ViewHolder<T,V>, position: Int) {
        holder.bind(mData[position], position)
    }

    /**
     * 更新数据集
     */
    fun submitList(newList: List<T>) {
        this.mData.clear()
        this.mData.addAll(newList)
        this.notifyDataSetChanged()
    }

    override fun getItemCount() = mData.size

    fun getData() = mData

    class ViewHolder<in T,V :ViewBinding>(val viewBinding: V, private val bindView: (V, Int, T) -> Unit) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: T, position: Int) {
            with(item) {
                itemView.setTag(R.id.easyadapter_position, position)
                bindView(viewBinding, position, item)
            }
        }
    }
}