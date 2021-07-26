package com.rongc.list.ability

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.NetworkUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.diff.ListChangeListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.AppExecutors
import com.rongc.feature.ability.IAbility
import com.rongc.feature.ui.host.IAbilityList
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.feature.vo.*
import com.rongc.list.ItemDecoration
import com.rongc.list.adapter.BaseRecyclerItemBinder
import com.rongc.list.adapter.BinderAdapter
import com.rongc.list.setCompatDiffNewData
import com.rongc.list.viewmodel.*
import com.rongc.list.viewpager2.BaseFragmentPagerAdapter
import com.runnchild.emptyview.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

abstract class AbsListAbility(val viewModel: BaseViewModel, val listHost: IListHost) : IAbility {

    val adapter: RecyclerView.Adapter<*> by lazy {
        listHost.providerAdapter() ?: BinderAdapter()
    }

    lateinit var emptyConfig: EmptyViewConfig

    val haveSetEmpty get() = ::emptyConfig.isInitialized

    private val listener = ListChangeListener<Any> { _, curList ->
        // 列表已有数据，不设置空页面
        if (curList.size > 0) {
            return@ListChangeListener
        }
//        val adapter = adapter
//        if (adapter.itemCount > 0) {
//            return@ListChangeListener
//        }

        if (!haveSetEmpty) {
            setEmptyView(EmptyViewConfig())
        }
        val vm = viewModel as? BaseListViewModel<*>
        vm?.let {
            buildEmpty(vm.setupEmptyView.value!!, emptyConfig) {
                vm.refresh()
            }
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        observeListResource(owner)

        val decoration = ItemDecoration.Builder().apply(listHost.decorationBuilder()).build()
        setupItemDecoration(decoration)

        val itemBinders = arrayListOf<BaseRecyclerItemBinder<out Any>>()
        listHost.registerItemBinders(itemBinders)
        setupItemBinders(itemBinders)

        (adapter as? BaseQuickAdapter<Any, *>)?.getDiffer()?.addListListener(listener)
    }

    private fun observeListResource(owner: LifecycleOwner) {
        @Suppress("UNCHECKED_CAST")
        val vm = viewModel as? BaseListViewModel<Any>
        // 如果非BaseListViewModel则需要手动调用observeResource
        if (vm != null) {
            vm._autoRefresh = listHost.autoRefresh()
            owner.observeResource(adapter, vm)

//            vm.setupEmptyView.observe(owner) { state ->
//                val adapter = adapter
//                // 列表已有数据，不设置空页面
//                if (adapter is BaseQuickAdapter<*, *>) {
//                    if (adapter.data.size > 0) {
//                        return@observe
//                    }
//                } else if (adapter.itemCount > 0) {
//                    return@observe
//                }
//
//                if (!haveSetEmpty) {
//                    setEmptyView(EmptyViewConfig())
//                }
//                buildEmpty(state, emptyConfig) {
//                    vm.refresh()
//                }
//            }
        }
    }

    open fun setEmptyView(emptyConfig: EmptyViewConfig) {
        val providerAdapter = adapter
        if (providerAdapter is BaseQuickAdapter<*, *>) {
            val emptyView = providerEmptyView() ?: return
            emptyView.config = emptyConfig
            providerAdapter.setEmptyView(emptyView as View)
            // providerAdapter.headerWithEmptyEnable = true
            // providerAdapter.footerWithEmptyEnable = true
        }
        this.emptyConfig = emptyConfig
    }

    abstract fun providerEmptyView(): IEmptyView?

    abstract fun setupItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>)

    abstract fun setupItemDecoration(decoration: ItemDecoration)

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        (adapter as? BaseQuickAdapter<Any, *>)?.getDiffer()?.removeListListener(listener)
    }
}

/**
 * 订阅列表数据结果返回监听，当结果返回时刷新列表
 * 订阅前应先注册{@link ListAbility}，如果ViewModel继承的是BaseListViewModel,则在注册后会自动订阅。
 * 否则需手动调用 {@link #observeResourceManually(LiveData)}
 */
@Suppress("UNCHECKED_CAST")
fun <T> LifecycleOwner.observeResource(
    adapter: RecyclerView.Adapter<*>, viewModel: BaseListViewModel<T>
) {
    val baseAdapter = adapter as? BaseQuickAdapter<T, BaseViewHolder> ?: return
    viewModel.result.observe(this) { resource ->
        if (resource.status != Status.LOADING || resource.data != null) {
            // 错误状态不更新列表
            if (!resource.isError) {
                if (viewModel.isRefresh) {
                    baseAdapter.setCompatDiffNewData(resource.data)
                } else {
                    resource.data?.let {
                        baseAdapter.addData(it)
                    }
                }
            }
        }
    }

    viewModel.notifyData.observe(this) {
        baseAdapter.setCompatDiffNewData(it)
    }
}

/**
 * 注册了ListAbility的页面，ViewModel非继承BaseListViewModel，但也需要监听数据变化并在空数据时设置对应空页面，
 * 相比继承BaseListViewModel的ViewModel额外需要手动调用此方法。
 * @param result 数据源LiveData
 * @param emptyRetry 按钮默认点击监听
 */
@Suppress("UNCHECKED_CAST")
fun <T> IAbilityList.observeResourceManually(
    result: LiveData<Resource<List<T>>>, emptyRetry: () -> Unit = {}
) {
    findAbility { it is ListAbility }?.let {
        it as ListAbility
        result.observe(lifecycleOwner) { resource ->
            if (resource.status != Status.LOADING || resource.data != null) {
                // 错误状态不更新列表
                if (!resource.isError) {
                    val adapter = it.adapter
                    if (adapter is BaseFragmentPagerAdapter<*>) {
                        (adapter as BaseFragmentPagerAdapter<T>).setList(resource.data)
                    } else {
                        (it.adapter as? BaseQuickAdapter<T, BaseViewHolder>)?.setCompatDiffNewData(
                            resource.data
                        )
                    }
                }
            }

            if (resource.isLoading) {
                return@observe
            }

            if (!it.haveSetEmpty) {
                it.setEmptyView(EmptyViewConfig())
            }
            if (it.haveSetEmpty) {
                resource.emptyState { state ->
                    it.buildEmpty(state, it.emptyConfig, emptyRetry)
                }
            }
        }
    }
}

private fun AbsListAbility.buildEmpty(
    state: EmptyState, emptyConfig: EmptyViewConfig, defClick: () -> Unit
) {
    val defaultBuilder = listHost.getDefaultEmptyConfig(state)
    val emptyBuilder = EmptyBuilder(state).apply(defaultBuilder)
    emptyBuilder.btnClick = defClick
    emptyConfig.builder(emptyBuilder)
    viewModel.viewModelScope.launch {
        delay(10)
        // 默认配置基础上更改配置
        listHost.setupEmptyView(emptyBuilder)
        emptyConfig.builder(emptyBuilder)
    }
}

fun <T> Resource<List<T>?>.emptyState(block: (EmptyState) -> Unit) {
    when (status) {
        Status.SUCCESS -> {
            if (data.isNullOrEmpty()) {
                block(EmptyState.EMPTY_DATA)
            }
        }
        Status.ERROR -> {
            AppExecutors.diskIO().execute {
                if (!NetworkUtils.isConnected()) {
                    block(EmptyState.EMPTY_NET_DISCONNECT)
                } else if (!NetworkUtils.isAvailable()) {
                    block(EmptyState.EMPTY_NET_UNAVAILABLE)
                } else {
                    if (data.isNullOrEmpty()) {
                        block(EmptyState.EMPTY_SERVICE)
                    }
                }
            }
        }
        Status.LOADING -> {
        }
    }
}
