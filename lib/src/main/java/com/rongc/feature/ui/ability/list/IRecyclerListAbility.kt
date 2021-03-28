package com.rongc.feature.ui.ability.list

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/28
 * @since 2.1.4
 */
interface IRecyclerListAbility: IListAbility {
    fun returnRecyclerView(): RecyclerView? = null

    /**
     * 默认返回LinearLayoutManager
     * 如果需要其他的LayoutManager，重写此方法
     */
    fun providerLayoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}