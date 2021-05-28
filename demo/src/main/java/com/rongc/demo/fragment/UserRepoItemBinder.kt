package com.rongc.demo.fragment

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.demo.databinding.RepoItemBinding
import com.rongc.demo.vo.Repo
import com.rongc.feature.refresh.BaseItemBindingBinder

class UserRepoItemBinder: BaseItemBindingBinder<RepoItemBinding, Repo>() {
    override fun convert(binding: RepoItemBinding, holder: BaseViewHolder, data: Repo) {
        binding.name.text = data.name
        binding.des.text = data.description
    }
}