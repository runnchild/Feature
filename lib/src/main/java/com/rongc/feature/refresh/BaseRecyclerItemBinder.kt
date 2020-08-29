package com.rongc.feature.refresh

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.binder.BaseItemBinder
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * RecyclerView的Item绑定类，每种类型都对应一种ItemBinder
 */
abstract class BaseRecyclerItemBinder<T> : BaseItemBinder<T, BaseViewHolder>() {

    var callback = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return this@BaseRecyclerItemBinder.areItemsTheSame(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return this@BaseRecyclerItemBinder.areContentsTheSame(oldItem, newItem)
        }

        override fun getChangePayload(oldItem: T, newItem: T): Any? {
            return this@BaseRecyclerItemBinder.getChangePayload(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(createView(parent, viewType))
    }

    open fun getChangePayload(oldItem: T, newItem: T): Any? {
        return null
    }

    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    /**
     * item内容设置
     */
    abstract override fun convert(holder: BaseViewHolder, data: T)

    /**
     * 返回item的布局
     * 如果是inflate xml， 需要把parent设置进去
     */
    abstract fun createView(parent: ViewGroup, viewType: Int): View
}