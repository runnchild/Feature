package com.rongc.feature

import android.app.Application
import android.view.Gravity
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * 核心初始化类， 初始化必要的库
 */
object Init {

    fun init(app: Application) {
        Utils.init(app)
        Fresco.initialize(app)
        ToastUtils.setGravity(Gravity.CENTER, 0, 0)
    }
}