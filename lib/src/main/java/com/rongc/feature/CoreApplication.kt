package com.rongc.feature

import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.Utils

class CoreApplication: Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Utils.init(this)
    }
}