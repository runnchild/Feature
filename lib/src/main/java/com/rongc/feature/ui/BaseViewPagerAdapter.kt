package com.rongc.feature.ui

import android.annotation.SuppressLint
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/21
 * @since 2.1.4
 */
abstract class BaseViewPagerAdapter<T>(val fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    constructor(fragmentActivity: FragmentActivity) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle
    )

    constructor(fragment: Fragment) : this(fragment.childFragmentManager, fragment.lifecycle)

    private val data = ObservableArrayList<T>()

    override fun getItemCount() = data.size

    fun getItem(position: Int) = data.getOrNull(position)

    override fun createFragment(position: Int): Fragment {
        val fragment = createItemFragment(position)
        fragment as Fragment
        fragment.lifecycleScope.launchWhenStarted {
            fragment.convert(position, getItem(position)!!, null)
        }
        return fragment
    }

    abstract fun createItemFragment(position: Int): IPagerItem<T>

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: Collection<T>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun notifyItem(item: T, payload: Any? = null) {
        val indexOf = indexOf(item)
        if (indexOf in 0 until data.size) {
            data[indexOf] = item
            notifyItemChanged(indexOf, payload)
        }
    }

    fun addItem(item: T, index: Int = data.size) {
        data.add(index, item)
        notifyItemInserted(index)
    }

    fun removeItem(item: T) {
        val indexOf = indexOf(item)
        if (indexOf in 0 until data.size) {
            data.remove(item)
            notifyItemRemoved(indexOf)
        }
    }

    fun removeAt(position: Int) {
        if (position in 0 until data.size) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun indexOf(item: T): Int {
        return data.indexOf(item)
    }

    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        @Suppress("UNCHECKED_CAST")
        val item = getCurrentFragment(position) as? IPagerItem<T>
        item?.convert(position, getItem(position)!!, payloads)
    }

    fun getCurrentFragment(position: Int): Fragment? {
        return fragmentManager.findFragmentByTag("f${getItemId(position)}")
    }
}