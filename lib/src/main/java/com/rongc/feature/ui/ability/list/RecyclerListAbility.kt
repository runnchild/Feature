package com.rongc.feature.ui.ability.list

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.R
import com.rongc.feature.binding.itemBinders
import com.rongc.feature.binding.itemDecoration
import com.rongc.feature.binding.setupEmptyView
import com.rongc.feature.model.BaseModel
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.viewmodel.BaseListViewModel
import com.rongc.feature.viewmodel.EmptyBuilder
import com.rongc.feature.viewmodel.RefreshEmptyViewModel
import com.rongc.feature.widget.ItemDecoration

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/26
 * @since 2.1.4
 */
open class RecyclerListAbility : IRecyclerListAbility {

    private lateinit var viewModel: BaseListViewModel<*, out BaseModel>
    private lateinit var owner: LifecycleOwner
    private val host get() = owner as? IRecyclerListAbility ?: this

    override fun onPageCreate(
        viewModel: BaseListViewModel<*, out BaseModel>,
        owner: LifecycleOwner,
        savedInstanceState: Bundle?
    ) {
        this.viewModel = viewModel
        this.owner = owner
    }

    override fun onPageCreateView(view: View, firstCreate: Boolean, savedInstanceState: Bundle?) {
        if (firstCreate) {
            val recyclerView =
                host.returnRecyclerView() ?: view.findViewById(R.id.base_recyclerView)
            recyclerView.adapter = host.providerAdapter()
            val binders = arrayListOf<BaseRecyclerItemBinder<out Any>>()
            recyclerView.layoutManager = host.providerLayoutManager(view.context)
            host.registerItemBinders(binders)
            val decoration = ItemDecoration.Builder().apply(host.decorationBuilder()).build()
            recyclerView.itemDecoration(decoration)
            @Suppress("UNCHECKED_CAST")
            recyclerView.itemBinders(binders as MutableList<BaseRecyclerItemBinder<Any>>)

            setEmptyView(recyclerView, view.context)
        }
    }

    private fun setEmptyView(recyclerView: RecyclerView, context: Context) {
        val emptyView = host.providerEmptyView(context) ?: return
        val emptyViewModel = recyclerView.setupEmptyView(emptyView)?.apply {
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
            (emptyView as View?)?.measure(recyclerView.measuredWidth, recyclerView.measuredHeight)
        }
    }
}