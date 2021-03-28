package com.rongc.feature.app.ui.binders

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.refresh.BaseRecyclerItemBinder

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/28
 * @since 2.1.4
 */
class PagerSimpleBinder : BaseRecyclerItemBinder<String>() {
    override fun convert(holder: BaseViewHolder, data: String) {
        val itemView = holder.itemView as TextView
        itemView.text = data
    }

    override fun createView(parent: ViewGroup, viewType: Int): View {
        return TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(-1, -1)
            setBackgroundColor(Color.BLUE)
        }
    }
}