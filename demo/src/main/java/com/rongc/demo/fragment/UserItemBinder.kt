package com.rongc.demo.fragment

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.demo.databinding.UserHeaderBinding
import com.rongc.feature.refresh.BaseItemBindingBinder

class UserItemBinder : BaseItemBindingBinder<UserHeaderBinding, String>() {
    override fun convert(binding: UserHeaderBinding, holder: BaseViewHolder, data: String) {
        binding.name.text = data
    }
}