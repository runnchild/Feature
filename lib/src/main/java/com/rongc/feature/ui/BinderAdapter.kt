package com.rongc.feature.ui

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseBinderAdapter
import com.rongc.feature.refresh.BaseRecyclerItemBinder

class BinderAdapter(list: MutableList<Any>? = null) : BaseBinderAdapter(list) {
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recyclerView.layoutManager is GridLayoutManager) {
            setGridSpanSizeLookup { _, viewType, pos ->
                val item = getItemBinder(viewType)
                if (item is BaseRecyclerItemBinder<*>) {
                    item.spanSize(pos)
                } else {
                    1
                }
            }
        }
    }
}