package com.rongc.feature.app.viewmodel

import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.app.R
import com.rongc.feature.app.databinding.MainOtherBindingItemBinding
import com.rongc.feature.refresh.BaseItemBindingBinder

class MainOtherItemHolder : BaseItemBindingBinder<MainOtherBindingItemBinding, Int>() {
    override fun convert(binding: MainOtherBindingItemBinding, holder: BaseViewHolder, data: Int) {
    }

    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return false
    }

    override fun getViewRes() = R.layout.main_other_binding_item

    override fun onClick(holder: BaseViewHolder, view: View, data: Int, position: Int) {
        ToastUtils.showShort("image: $position")
    }
}
