package com.infore.base.common

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.infore.base.R


/**
 * 简单Adapter
 */

open class EasyAdapter<T>(private val layoutResId: Int, private val bindView: (View, Int, T) -> Unit) :
        RecyclerView.Adapter<EasyAdapter.ViewHolder<T>>() {

    private var mData = mutableListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return ViewHolder(view, bindView)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
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

    class ViewHolder<in T>(view: View, private val bindView: (View, Int, T) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(item: T, position: Int) {
            with(item) {
                itemView.setTag(R.id.easyadapter_position, position)
                bindView(itemView, position, item)
            }
        }
    }
}