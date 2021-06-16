package com.rongc.wan.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.ui.BaseFragment
import com.rongc.list.ability.IRecyclerList
import com.rongc.list.ability.ListAbility
import com.rongc.list.databinding.BaseRecyclerWithRefreshBinding
import com.rongc.list.viewpager2.IPagerItem

class ProjectListFragment : BaseFragment<BaseRecyclerWithRefreshBinding, ProjectListViewModel>(),
    IPagerItem<Int>, IRecyclerList {

    override val recyclerView: RecyclerView
        get() = mBinding.recyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerAbility(ListAbility(viewModel, this))
    }

    override fun convert(position: Int, item: Int, payloads: MutableList<Any>?) {

    }
}