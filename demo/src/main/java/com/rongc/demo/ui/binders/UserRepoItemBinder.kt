package com.rongc.demo.ui.binders

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.demo.databinding.RepoItemBinding
import com.rongc.demo.vo.Repo
import com.rongc.feature.refresh.BaseItemBindingBinder

class UserRepoItemBinder: BaseItemBindingBinder<RepoItemBinding, Repo>() {

    override fun convert(binding: RepoItemBinding, holder: BaseViewHolder, data: Repo) {
        binding.name.text = data.name
        binding.des.text = data.description
    }

    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem.name == newItem.name && oldItem.description == newItem.description
    }
}