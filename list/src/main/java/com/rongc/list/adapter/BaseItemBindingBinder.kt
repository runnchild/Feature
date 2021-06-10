package com.rongc.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.BR
import com.rongc.list.R
import java.lang.reflect.ParameterizedType

/**
 * 支持DataBinding的ItemBinder
 */
abstract class BaseItemBindingBinder<B : ViewBinding, T> : BaseRecyclerItemBinder<T>() {

    override fun convert(holder: BaseViewHolder, data: T) {
        @Suppress("UNCHECKED_CAST")
        val binding = holder.itemView.binding
        if (binding is ViewDataBinding) {
            try {
                // 如果xml中有定义mBean，赋值
                binding::class.java.superclass?.getDeclaredField("mBean")
                binding.setVariable(BR.bean, data)
            } catch (e: Exception) {
            }
        }
        convert(binding, holder, data)
        if (binding is ViewDataBinding) {
            binding.executePendingBindings()
        }
    }

    override fun createView(parent: ViewGroup, viewType: Int): View {
        return binding(LayoutInflater.from(parent.context), parent).let {
            it.root.setTag(R.id.tag_binding, it)
            it.root
        }
    }

    @Suppress("UNCHECKED_CAST")
    private val View.binding
        get() = getTag(R.id.tag_binding) as B

    /**
     * 设置item的内容
     * @param binding 当前item的DataBinding
     * @param holder BaseViewHolder
     * @param data item 内容
     */
    abstract fun convert(binding: B, holder: BaseViewHolder, data: T)

    private fun binding(inflater: LayoutInflater, container: ViewGroup?): B {
        val bindingClass =
            (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
        val method = bindingClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        method.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return (method.invoke(null, inflater, container, false) as B)
    }

    final override fun onClick(holder: BaseViewHolder, view: View, data: T, position: Int) {
        onItemClick(holder.itemView.binding, view, data, position)
    }

    final override fun onChildClick(holder: BaseViewHolder, view: View, data: T, position: Int) {
        onItemChildClick(holder.itemView.binding, view, data, position)
    }

    final override fun onLongClick(
        holder: BaseViewHolder, view: View, data: T, position: Int
    ): Boolean {
        return onItemLongClick(
            holder.itemView.binding, view, data, position
        )
    }

    override fun onChildLongClick(
        holder: BaseViewHolder, view: View, data: T, position: Int
    ): Boolean {
        return onItemChildLongClick(
            holder.itemView.binding, view, data, position
        )
    }

    open fun onItemChildLongClick(binding: B, view: View, data: T, position: Int) = false

    open fun onItemLongClick(binding: B, view: View, data: T, position: Int) = false

    open fun onItemChildClick(binding: B, view: View, data: T, position: Int) {
    }

    open fun onItemClick(binding: B, view: View, data: T, position: Int) {
    }
}