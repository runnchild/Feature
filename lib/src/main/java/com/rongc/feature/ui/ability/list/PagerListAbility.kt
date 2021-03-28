package com.rongc.feature.ui.ability.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseQuickAdapter
import com.rongc.feature.R
import com.rongc.feature.binding.ViewPager2Binding.doOnAdapter
import com.rongc.feature.binding.ViewPager2Binding.itemBinders
import com.rongc.feature.binding.ViewPager2Binding.itemDecoration
import com.rongc.feature.binding.setupEmptyView
import com.rongc.feature.model.BaseModel
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.viewmodel.BaseListViewModel
import com.rongc.feature.viewmodel.EmptyBuilder
import com.rongc.feature.viewmodel.RefreshEmptyViewModel
import com.rongc.feature.widget.ItemDecoration
import java.util.*

open class PagerListAbility : IPagerListAbility {

    private lateinit var owner: LifecycleOwner
    private val host get() = owner as? IPagerListAbility ?: this
    private lateinit var viewModel: BaseListViewModel<*, out BaseModel>

    override fun onPageCreate(
        viewModel: BaseListViewModel<*, out BaseModel>,
        owner: LifecycleOwner,
        savedInstanceState: Bundle?
    ) {
        this.owner = owner
        this.viewModel = viewModel
    }

    override fun onPageCreateView(view: View, firstCreate: Boolean, savedInstanceState: Bundle?) {
        if (firstCreate) {
            val viewPager2 = host.returnPagerView() ?: view.findViewById(R.id.base_viewPager)
            viewPager2.adapter = host.providerAdapter()
            val decoration = ItemDecoration.Builder().apply(host.decorationBuilder()).build()
            viewPager2.itemDecoration(decoration)
            viewPager2.doOnAdapter<RecyclerView.Adapter<*>> {
                if (it is BaseQuickAdapter<*, *>) {
                    val binders = arrayListOf<BaseRecyclerItemBinder<out Any>>()
                    host.registerItemBinders(binders)
                    if (binders.isNotEmpty()) {
                        @Suppress("UNCHECKED_CAST")
                        viewPager2.itemBinders(binders as ArrayList<BaseRecyclerItemBinder<Any>>)
                    }
                }
            }
            setEmptyView(viewPager2)
        }
    }

    private fun setEmptyView(viewPager: ViewPager2) {
        val emptyView = host.providerEmptyView(viewPager.context) ?: return
        val emptyViewModel = viewPager.setupEmptyView(emptyView)?.apply {
            viewModel.emptyRefreshViewModel = this
        }
        viewModel.setupEmptyView.observe(owner) {
            when (it) {
                RefreshEmptyViewModel.EMPTY_NET_DISCONNECT -> {
                    emptyViewModel?.showNoNet { viewModel.refresh() }
                }
                RefreshEmptyViewModel.EMPTY_NET_UNAVAILABLE -> {
                    emptyViewModel?.showNetUnavailable { viewModel.refresh() }
                }
                else -> {
                    val builder = EmptyBuilder().apply(host.setupEmptyView(it)).apply {
                        if (refreshClick == null && refreshBuilder == null) {
                            refreshClick = {
                                viewModel.refresh()
                            }
                        }
                    }

                    emptyViewModel?.builder(builder)
                }
            }
            (emptyView as View?)?.measure(viewPager.measuredWidth, viewPager.measuredHeight)
        }
    }
}