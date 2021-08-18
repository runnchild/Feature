package com.rongc.list.viewpager2

import android.annotation.SuppressLint
import androidx.collection.LongSparseArray
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.runnchild.emptyview.EmptyViewConfig

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/21
 */
abstract class BaseFragmentPagerAdapter<T>(
    private val fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    constructor(fragmentActivity: FragmentActivity) : this(
        fragmentActivity.supportFragmentManager, fragmentActivity.lifecycle
    )

    constructor(parent: Fragment) : this(parent.childFragmentManager, parent.lifecycle)

    private var mEmptyData: EmptyViewConfig? = null

    private val data = ObservableArrayList<T>()

    override fun getItemCount() = if (data.size == 0 && hasEmptyView) {
        1
    } else {
        data.size
    }

    fun getDataCount() = data.size

    private val hasEmptyView get() = mEmptyData != null

    fun getItem(position: Int) = data.getOrNull(position)

    @Suppress("UNCHECKED_CAST")
    override fun createFragment(position: Int): Fragment {
        val fragment = if (getItem(position) == null && hasEmptyView) {
            generateEmptyFragment()
        } else {
            createItemFragment(getItem(position)!!, position)
        }
        (fragment as IPagerItem<T>).attachAdapter(this)
        return fragment as Fragment
    }

    open fun generateEmptyFragment() = EmptyListFragment()

    abstract fun createItemFragment(item: T, position: Int): IPagerItem<T>

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: Collection<T>?) {
        data.clear()
        if (!list.isNullOrEmpty()) {
            data.addAll(list)
        }
        val itemCount = list?.size ?: 0
        if (itemCount > 0 && hasEmptyView) {
            notifyItemChanged(0)
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

    fun addData(list: List<T>?) {
        list?:return
        val index = 0.coerceAtLeast(data.size - 1)
        data.addAll(list)
        notifyItemRangeInserted(index, list.size)
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

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(
        holder: FragmentViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        val fragment = findFragment(position)
        val item = fragment as? IPagerItem<T>
        val data = getItem(position)
        fragment?.lifecycleScope?.launchWhenResumed {
            if (item is EmptyListFragment) {
                item.convert(position, mEmptyData!!, payloads)
            } else {
                item?.convert(position, data!!, payloads)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return if (getItem(position) == null) {
            RecyclerView.NO_ID
        } else {
            super.getItemId(position)
        }
    }

    override fun containsItem(itemId: Long): Boolean {
        return super.containsItem(itemId)
    }

    fun findFragment(position: Int): Fragment? {
        return fragmentManager.findFragmentByTag("f${getItemId(position)}") ?: let {
            val field = FragmentStateAdapter::class.java.getDeclaredField("mFragments")
            field.isAccessible = true
            val array = field.get(this) as? LongSparseArray<*>
            array?.get(getItemId(position)) as? Fragment
        }
    }

    fun setEmptyData(emptyData: EmptyViewConfig) {
        this.mEmptyData = emptyData
    }

    fun emptyData() = mEmptyData
}