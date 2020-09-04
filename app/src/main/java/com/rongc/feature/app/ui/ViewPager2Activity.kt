package com.rongc.feature.app.ui

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.feature.app.databinding.ActivityViewPager2Binding
import com.rongc.feature.app.ui.viewmodel.ViewPagerViewModel
import com.rongc.feature.binding.ViewPager2Binding.itemBinders
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.ui.BaseBindingActivity
import com.rongc.feature.utils.Compat.toast

class ViewPager2Activity : BaseBindingActivity<ActivityViewPager2Binding, ViewPagerViewModel>() {

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityViewPager2Binding {
        return ActivityViewPager2Binding.inflate(inflater, container, false)
    }

    override fun initView(view: View) {
        super.initView(view)
        binding.viewPager.itemBinders(mutableListOf(object : BaseRecyclerItemBinder<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) = false

            override fun areContentsTheSame(oldItem: String, newItem: String) = false

            override fun convert(holder: BaseViewHolder, data: String) {
                ((holder.itemView as ViewGroup).getChildAt(0) as TextView).text = data
            }

            override fun createView(parent: ViewGroup, viewType: Int): View {
                return FrameLayout(parent.context).apply {
                    layoutParams = ViewGroup.LayoutParams(-1, -1)
                    setBackgroundColor(com.blankj.utilcode.util.ColorUtils.getRandomColor())

                    addView(TextView(parent.context).apply {
                        gravity = Gravity.CENTER
                        textSize = 19f
                    })
                }
            }

            override fun onClick(holder: BaseViewHolder, view: View, data: String, position: Int) {
                data.toast()
            }
        }))
    }
}