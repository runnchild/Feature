package com.rongc.feature.ui.ability.list

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableList
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.diff.BrvahListUpdateCallback
import com.rongc.feature.R
import com.rongc.feature.binding.doOnAdapter
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
            recyclerView.layoutManager = host.providerLayoutManager(view.context)
            val providerAdapter = host.providerAdapter()
            recyclerView.adapter = providerAdapter
            val binders = arrayListOf<BaseRecyclerItemBinder<out Any>>()
            host.registerItemBinders(binders)
            val decoration = ItemDecoration.Builder().apply(host.decorationBuilder()).build()
            recyclerView.itemDecoration(decoration)
            @Suppress("UNCHECKED_CAST")
            recyclerView.itemBinders(binders as MutableList<BaseRecyclerItemBinder<Any>>)

            setEmptyView(recyclerView, view.context)

//            recyclerView.doOnAdapter {
//                val call = BrvahListUpdateCallback(it)
//                viewModel.items.addOnListChangedCallback(object :
//                    ObservableList.OnListChangedCallback<ObservableList<Any>>() {
//                    override fun onChanged(sender: ObservableList<Any>?) {
//                        it.notifyDataSetChanged()
//                    }
//
//                    override fun onItemRangeChanged(
//                        sender: ObservableList<Any>?,
//                        positionStart: Int,
//                        itemCount: Int
//                    ) {
//                        call.onChanged(positionStart, itemCount, null)
//                    }
//
//                    override fun onItemRangeInserted(
//                        sender: ObservableList<Any>?,
//                        positionStart: Int,
//                        itemCount: Int
//                    ) {
//                        call.onInserted(positionStart, itemCount)
//                    }
//
//                    override fun onItemRangeMoved(
//                        sender: ObservableList<Any>?,
//                        fromPosition: Int,
//                        toPosition: Int,
//                        itemCount: Int
//                    ) {
//                        call.onMoved(fromPosition, toPosition)
//                    }
//
//                    override fun onItemRangeRemoved(
//                        sender: ObservableList<Any>,
//                        positionStart: Int,
//                        itemCount: Int
//                    ) {
//                        call.onRemoved(positionStart, itemCount)
//                    }
//                })
//            }
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