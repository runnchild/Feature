package com.rongc.feature.viewmodel

import android.graphics.Color
import androidx.core.graphics.drawable.toDrawable

object DefaultEmptyConfig {
    var noNetBuilder: EmptyBuilder.() -> Unit = {
        tip = "网络未连接"
        subTip = "请检查你的网络设置后刷新"
        btnVisible = true
        btnText = "刷新"
        iconDrawable = Color.RED.toDrawable()
    }

    var netUnavailableBuilder: EmptyBuilder.() -> Unit = {
        tip = "网络异常"
        subTip = "请检查你的网络设置后刷新"
        btnVisible = true
        btnText = "刷新"
        iconDrawable = Color.BLUE.toDrawable()
    }

    var emptyDataBuilder: EmptyBuilder.() -> Unit = {
        tip = "no data found"
        subTip = "try again"
        btnVisible = true
        btnText = "retry"
        iconDrawable = Color.GREEN.toDrawable()
    }

    /**
     * 配置断网时的空页面样式，断网并列表为空时默认使用此配置，
     * 若某个页面需要更改，在{@link com.rongc.feature.ability.IListAbility#setupEmptyView(int)}方法中
     * 根据state == {@link RefreshEmptyViewModel##EMPTY_NET_DISCONNECT}返回EmptyBuilder
     */
    fun configNetDisconnectBuilder(builder: EmptyBuilder.() -> Unit) {
        noNetBuilder = builder
    }

    /**
     * 配置网络不可用的空页面样式，网络不可用并列表为空时默认使用此配置，
     * 若某个页面需要更改，在{@link com.rongc.feature.ability.IListAbility#setupEmptyView(int)}方法中
     * 根据state == {@link RefreshEmptyViewModel#EMPTY_NET_UNAVAILABLE}返回EmptyBuilder
     */
    fun configNetUnavailableBuilder(builder: EmptyBuilder.() -> Unit) {
        netUnavailableBuilder = builder
    }

    /**
     * 配置空数据时的空页面样式，空数据并列表为空时默认使用此配置，
     * 若某个页面需要更改，在{@link com.rongc.feature.ability.IListAbility#setupEmptyView(int)}方法中
     * 根据state == {@link RefreshEmptyViewModel#EMPTY_DATA}返回EmptyBuilder
     */
    fun configEmptyDataBuilder(builder: EmptyBuilder.() -> Unit) {
        emptyDataBuilder = builder
    }
}