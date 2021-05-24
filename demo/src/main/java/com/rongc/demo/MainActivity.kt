package com.rongc.demo

import android.os.Bundle
import com.rongc.demo.databinding.ActivityMainBinding
import com.rongc.feature.ui.BaseActivity
import com.rongc.feature.utils.autoCleared

class MainActivity: BaseActivity<ActivityMainBinding, HomeViewModel>() {

    var autoField by autoCleared<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoField = "this field gets cleaned up when the activity is destroyed"
    }
}