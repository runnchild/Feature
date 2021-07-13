package com.rongc.demo.ui

import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.rongc.demo.databinding.ActivityMainBinding
import com.rongc.demo.viewmodel.HomeViewModel
import com.rongc.feature.ui.BaseActivity
import com.rongc.feature.utils.autoCleared
import com.rongc.feature.utils.dp
import com.rongc.feature.utils.setRoundBg
import com.runnchild.emptyview.DefaultEmptyConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class MainActivity : BaseActivity<ActivityMainBinding, HomeViewModel>() {

    var autoField by autoCleared<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoField = "this field gets cleaned up when the activity is destroyed"

        // 建议放在Application+
        Glide.init(this.application, GlideBuilder())
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
        DefaultEmptyConfig.configEmptyDataBuilder {
            refreshBtn {
                setRoundBg(0, 15.dp)
            }
        }
    }
}