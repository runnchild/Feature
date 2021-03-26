package com.rongc.feature.ui.delegate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

interface IFragmentAbility {
    fun onCreate(savedInstanceState: Bundle?) {}
    fun onResume() {}
    fun onPause() {}
    fun setWindowBackground(view: View) {}
    fun initView(view: View) {}
    fun initData() {}
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {}
    fun onBackPressed() {}
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = null

    fun onDestroy() {}
    fun onHiddenChanged(hidden: Boolean) {}
}