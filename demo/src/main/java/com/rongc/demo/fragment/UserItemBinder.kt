package com.rongc.demo.fragment

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.demo.databinding.UserHeaderBinding
import com.rongc.demo.vo.Owner
import com.rongc.feature.refresh.BaseItemBindingBinder

class UserItemBinder : BaseItemBindingBinder<UserHeaderBinding, Owner>() {
    override fun convert(binding: UserHeaderBinding, holder: BaseViewHolder, data: Owner) {
        binding.name.text = data.login
    }

    override fun areItemsTheSame(oldItem: Owner, newItem: Owner): Boolean {
        return oldItem.login == newItem.login
    }

    override fun areContentsTheSame(oldItem: Owner, newItem: Owner): Boolean {
        return oldItem.login == newItem.login
    }
}