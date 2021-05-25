package com.rongc.demo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.demo.R
import com.rongc.demo.databinding.RepoItemBinding
import com.rongc.demo.vo.Repo

class RepoListAdapter(val call: (Repo)->Unit) :
    ListAdapter<Repo, BaseViewHolder>(object : DiffUtil.ItemCallback<Repo>() {
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.owner == newItem.owner
                    && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.description == newItem.description
                    && oldItem.stars == newItem.stars
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            RepoItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ).root
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = getItem(position)
        holder.setText(R.id.name, item.name)
        holder.setText(R.id.des, item.description)
        holder.itemView.setOnClickListener {
            call(item)
        }
    }
}