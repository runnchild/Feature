package com.rongc.feature.ui.ability.list

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.model.BaseModel
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.ui.ability.IAbility
import com.rongc.feature.viewmodel.BaseListViewModel
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.feature.viewmodel.EmptyBuilder
import com.rongc.feature.widget.EmptyView
import com.rongc.feature.widget.IEmptyView
import com.rongc.feature.widget.ItemDecoration

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/26
 */
interface IListAbility : IAbility {

    override fun onPageCreate(viewModel: BaseViewModel<out BaseModel>, owner: LifecycleOwner, savedInstanceState: Bundle?) {
        viewModel as BaseListViewModel<*, out BaseModel>
        val host = owner as? IListAbility ?: this
        viewModel.autoRefresh = host.autoRefresh()
        onPageCreate(viewModel, owner, savedInstanceState)
    }

    fun onPageCreate(viewModel: BaseListViewModel<*, out BaseModel>, owner: LifecycleOwner, savedInstanceState: Bundle?) {
    }

    /**
     * 默认使用BaseBinderAdapter，如果需要提供新Adapter, 重写此方法返回需要设置的Adapter
     */
    fun providerAdapter(): RecyclerView.Adapter<*>? = null

    /**
     * 是否进入页面自动刷新
     */
    fun autoRefresh() = true

    /**
     * 配置空页面UI
     */
    fun setupEmptyView(state: Int): EmptyBuilder.() -> Unit = {}

    /**
     * 如果需要，重写并返回其他空页面
     */
    fun providerEmptyView(context: Context): IEmptyView? = EmptyView(context)

    /**
     * 配置列表分割线
     */
    fun decorationBuilder(): ItemDecoration.Builder.() -> Unit {
        return { }
    }

    /**
     * 添加列表item样式
     */
    fun registerItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
    }
}