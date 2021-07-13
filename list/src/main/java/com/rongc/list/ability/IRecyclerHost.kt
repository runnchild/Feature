package com.rongc.list.ability

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/26
 */
interface IRecyclerHost: IListHost {
    val recyclerView: RecyclerView

    /**
     * 默认返回LinearLayoutManager
     * 如果需要其他的LayoutManager，重写此方法
     */
    fun providerLayoutManager(context: Context): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}