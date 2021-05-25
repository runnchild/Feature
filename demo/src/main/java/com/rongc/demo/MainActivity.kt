package com.rongc.demo

import android.os.Bundle
import com.rongc.demo.databinding.ActivityMainBinding
import com.rongc.demo.viewmodel.HomeViewModel
import com.rongc.feature.ui.BaseActivity
import com.rongc.feature.utils.autoCleared
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class MainActivity: BaseActivity<ActivityMainBinding, HomeViewModel>() {

    var autoField by autoCleared<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoField = "this field gets cleaned up when the activity is destroyed"

        // 建议放在Application
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
    }
}