package com.rongc.list.ability

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rongc.list.R

internal fun invokeDoOnAdapter(view: View, adapter: RecyclerView.Adapter<*>) {
    @Suppress("UNCHECKED_CAST")
    val call = view.getTag(R.id.tag_adapter_callback) as? ArrayList<(Any) -> Unit>
    call?.forEach {
        it(adapter)
    }
    call?.clear()
    view.setTag(R.id.tag_adapter_callback, null)
}

internal fun <T : RecyclerView.Adapter<*>> addDoOnAdapterCallback(view: View, block: (T) -> Unit) {
    val blockList = view.getTag(R.id.tag_adapter_callback)
            as? ArrayList<(T) -> Unit> ?: arrayListOf()
    if (!blockList.contains(block)) {
        blockList.add(block)
        view.setTag(R.id.tag_adapter_callback, blockList)
    }
}