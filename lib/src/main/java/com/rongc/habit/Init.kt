package com.rongc.habit

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * 核心初始化类， 初始化必要的库
 */
object Init {

    fun init(app: Application) {
        Utils.init(app)
        Fresco.initialize(app)
    }
}