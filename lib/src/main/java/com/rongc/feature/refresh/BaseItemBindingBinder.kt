package com.rongc.feature.refresh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.BR
import com.rongc.feature.utils.singleAction

/**
 * 支持DataBinding的ItemBinder
 */
abstract class BaseItemBindingBinder<B : ViewDataBinding, T> : BaseRecyclerItemBinder<T>() {

    protected var pos = -1
    protected var entity: T? = null

    override fun convert(holder: BaseViewHolder, data: T) {
        pos = holder.adapterPosition
        entity = data
        val binding = DataBindingUtil.findBinding<B>(holder.itemView)!!.apply {
            try {
                // 如果xml中没定义mBean
                this::class.java.superclass?.getDeclaredField("mBean")
                setVariable(BR.bean, data)
            } catch (e: Exception) {
            }
            try {
                this::class.java.superclass?.getDeclaredField("mBinder")
                setVariable(BR.binder, this@BaseItemBindingBinder)
            } catch (e: Exception) {
            }
        }
        convert(binding, holder, data)
        binding.executePendingBindings()
    }

    override fun createView(parent: ViewGroup, viewType: Int): View {
        return getViewBinding(LayoutInflater.from(parent.context), parent).root
    }

    /**
     * 设置item的内容
     * @param binding 当前item的DataBinding
     * @param holder BaseViewHolder
     * @param data item 内容
     */
    abstract fun convert(binding: B, holder: BaseViewHolder, data: T)

    /**
     * 返回Item布局资源id
     */
    abstract fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup): B

    override fun onClick(holder: BaseViewHolder, view: View, data: T, position: Int) {
        view.singleAction {
            val binding = DataBindingUtil.findBinding<B>(holder.itemView)!!
            onItemClick(binding, view, data, position)
        }
    }

    override fun onChildClick(holder: BaseViewHolder, view: View, data: T, position: Int) {
        view.singleAction {
            onItemChildClick(DataBindingUtil.findBinding<B>(holder.itemView)!!, view, data, position)
        }
    }

    override fun onLongClick(holder: BaseViewHolder, view: View, data: T, position: Int): Boolean {
        return onItemLongClick(
            DataBindingUtil.findBinding<B>(holder.itemView)!!,
            view,
            data,
            position
        )
    }

    override fun onChildLongClick(
        holder: BaseViewHolder,
        view: View,
        data: T,
        position: Int
    ): Boolean {
        return onItemChildLongClick(
            DataBindingUtil.findBinding<B>(holder.itemView)!!,
            view,
            data,
            position
        )
    }

    open fun onItemChildLongClick(binding: B, view: View, data: T, position: Int) = false

    open fun onItemLongClick(binding: B, view: View, data: T, position: Int) = false

    open fun onItemChildClick(binding: B, view: View, data: T, position: Int) {
    }

    open fun onItemClick(binding: B, view: View, data: T, position: Int) {
    }
}