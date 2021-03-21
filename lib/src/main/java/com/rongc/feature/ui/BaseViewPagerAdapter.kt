package com.rongc.feature.ui

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/21
 * @since 2.1.4
 */
abstract class BaseViewPagerAdapter : FragmentStateAdapter {

    constructor(fragmentActivity: FragmentActivity) : super(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle
    )

    constructor(fragment: Fragment) : super(fragment.childFragmentManager, fragment.lifecycle)

    private val data = arrayListOf<Any>()

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: Collection<Any>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }
}