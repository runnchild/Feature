package com.rongc.feature.utils

import android.graphics.Color
import android.view.Gravity
import com.blankj.utilcode.util.ToastUtils


var toastInstance = ToastUtils.make()
    .setBgColor(Color.parseColor("#80000000"))
    .setTextColor(Color.WHITE)
    .setGravity(Gravity.CENTER, 0, 0)

fun String?.toast() {
    if (!isNullOrEmpty()) {
        toastInstance.show(this)
    }
}