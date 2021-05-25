package com.rongc.demo.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.demo.vo.Repo

class RepoListAdapter :
    ListAdapter<Repo, BaseViewHolder>(object : DiffUtil.ItemCallback<Repo>() {
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
//        return oldItem.owner == newItem.owner
//                && oldItem.name == newItem.name
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
//        return oldItem.description == newItem.description
//                && oldItem.stars == newItem.stars
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(TextView(parent.context))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val textView = holder.itemView as TextView
        textView.doOnLayout {
            textView.updateLayoutParams {
                height = 150
            }
        }
        textView.text = getItem(position).name
    }
}