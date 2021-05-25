package com.rongc.demo.adapter

import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.demo.R
import com.rongc.demo.vo.Repo

class RepoListAdapter(val call: (Repo) -> Unit) :
    BaseQuickAdapter<Repo, BaseViewHolder>(R.layout.repo_item) {

    init {
        setDiffCallback(object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.owner == newItem.owner
                        && oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.description == newItem.description
                        && oldItem.stars == newItem.stars
            }
        })
    }

    override fun convert(holder: BaseViewHolder, item: Repo) {
        holder.setText(R.id.name, item.name)
        holder.setText(R.id.des, item.description)
        holder.itemView.setOnClickListener {
            call(item)
        }
    }
}