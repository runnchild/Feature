package com.rongc.list.viewpager2

import android.annotation.SuppressLint
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.rongc.list.viewmodel.RefreshEmptyViewModel

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/21
 */
abstract class BaseFragmentPagerAdapter<T>(private val fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    constructor(fragmentActivity: FragmentActivity) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle
    )

    constructor(parent: Fragment) : this(parent.childFragmentManager, parent.lifecycle)

    private var mEmptyData: RefreshEmptyViewModel? = null

    private val data = ObservableArrayList<T>()

    override fun getItemCount() = data.size

    fun getItem(position: Int) = data.getOrNull(position)

    override fun createFragment(position: Int): Fragment {
        if (getItem(position) is RefreshEmptyViewModel) {
            val fragment = generateEmptyFragment()
            @Suppress("UNCHECKED_CAST")
            (fragment as IPagerItem<T>).attachAdapter(this)
            fragment.lifecycleScope.launchWhenStarted {
                fragment.convert(position, mEmptyData!!, null)
            }
            return fragment
        }
        val fragment = createItemFragment(getItem(position)!!, position)
        fragment.attachAdapter(this)
        fragment as Fragment
        fragment.lifecycleScope.launchWhenStarted {
            fragment.convert(position, getItem(position)!!, null)
        }
        return fragment
    }

    open fun generateEmptyFragment() = EmptyListFragment()

    abstract fun createItemFragment(item: T, position: Int): IPagerItem<T>

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: Collection<T>?) {
        data.clear()
        if (!list.isNullOrEmpty()) {
            data.addAll(list)
        }
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
        val item = findFragment(position) as? IPagerItem<T>
        item?.convert(position, getItem(position)!!, payloads)
    }

    override fun getItemId(position: Int): Long {
        return if (getItem(position) is RefreshEmptyViewModel) {
            RecyclerView.NO_ID
        } else {
            super.getItemId(position)
        }
    }

    override fun containsItem(itemId: Long): Boolean {
        return itemId != RecyclerView.NO_ID
    }

    fun findFragment(position: Int): Fragment? {
        return fragmentManager.findFragmentByTag("f${getItemId(position)}")
    }

    fun setEmptyData(emptyData: RefreshEmptyViewModel) {
        this.mEmptyData = emptyData
    }

    fun emptyData() = mEmptyData
}