package com.rongc.wan.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.ui.BaseFragment
import com.rongc.list.ability.IRecyclerHost
import com.rongc.list.ability.ListAbility
import com.rongc.list.adapter.BaseRecyclerItemBinder
import com.rongc.list.databinding.BaseRecyclerWithRefreshBinding
import com.rongc.list.viewpager2.IPagerItem

class ProjectListFragment : BaseFragment<BaseRecyclerWithRefreshBinding, ProjectListViewModel>(),
    IPagerItem<String>, IRecyclerHost {

    override val recyclerView: RecyclerView get() = mBinding.recyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.cid.value = arguments?.getString("cid")
        // 注册ListAbility实现列表相关功能
        registerAbility(ListAbility(viewModel, this))
    }

    override fun registerItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
        // 添加列表Item样式Binder
        binders.add(ProjectItemBinder())
    }
}