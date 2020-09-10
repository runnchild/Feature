package com.rongc.feature.app.ui

import android.graphics.Color
import com.rongc.feature.app.R
import com.rongc.feature.ui.BaseActivity
import com.rongc.feature.ui.toolbar.BarConfig
import com.rongc.feature.utils.Compat.drawable
import com.rongc.feature.utils.Compat.toast
import com.rongc.feature.viewmodel.EmptyViewModel

class StatusBarActivity : BaseActivity<EmptyViewModel>() {

    override fun getContentViewRes() = R.layout.activity_status_bar

    override fun getBarConfig(): BarConfig.() -> Unit {
        return {
            // 沉浸且状态栏icon黑色
//            statusBarState = BarConfig.TRANSPARENT_BLACK
            // 沉浸且状态栏icon白色
//            statusBarState = BarConfig.TRANSPARENT_WHITE
            // 设置状态栏颜色 和 statusBarState冲突
            statusColor = Color.BLUE
            // 设置toolbar颜色
            toolBarBackground = Color.BLUE
            // 返回键显示状态
//            toolbarBackVisible = false
            // toolbar底部分割线颜色
            toolBarDividerColor = Color.RED
            // 返回键图标
            toolbarBackDrawable = R.mipmap.common_icon_back.drawable()!!
            // 虚拟导航栏颜色
            navColor = Color.BLUE
            // 菜单
            menu {
                text = "提交"
                setTextColor(Color.WHITE)
                setOnClickListener { title.toString().toast() }
            }
            toolBarBackground = Color.BLUE
        }
    }
}