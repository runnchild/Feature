package com.rongc.feature.app.viewmodel

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.refresh.BaseRecyclerItemBinder

class MainItemHolder : BaseRecyclerItemBinder<String>() {

    override fun convert(holder: BaseViewHolder, data: String) {
        (holder.itemView as TextView).text = "data:${holder.adapterPosition}"
    }

    override fun createView(parent: ViewGroup, viewType: Int): View {
        return TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(-1, 200)
            gravity = Gravity.CENTER
            setBackgroundColor(Color.GRAY)
        }
    }

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return false
    }

    override fun onClick(holder: BaseViewHolder, view: View, data: String, position: Int) {
        ToastUtils.showShort("data: $position")
    }
}