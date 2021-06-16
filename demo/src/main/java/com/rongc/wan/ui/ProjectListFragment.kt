package com.rongc.wan.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.ui.BaseFragment
import com.rongc.list.ability.IRecyclerList
import com.rongc.list.ability.ListAbility
import com.rongc.list.adapter.BaseRecyclerItemBinder
import com.rongc.list.databinding.BaseRecyclerWithRefreshBinding
import com.rongc.list.viewpager2.IPagerItem

class ProjectListFragment : BaseFragment<BaseRecyclerWithRefreshBinding, ProjectListViewModel>(),
    IPagerItem<String>, IRecyclerList {

    override val recyclerView: RecyclerView
        get() = mBinding.recyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.cid.value = arguments?.getString("cid")
        registerAbility(ListAbility(viewModel, this))
    }

    override fun registerItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
        binders.add(ProjectItemBinder())
    }
}