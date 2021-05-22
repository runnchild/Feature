package com.runnchild.feature.ability

import android.os.Bundle
import android.view.View

interface IAbility {
    fun onPageCreateView(view: View, firstCreate: Boolean, savedInstanceState: Bundle?) {}
    fun onPageCreate() {}
    fun onPageResume() {}
    fun onPageStart() {}
    fun onPagePause() {}
    fun onPageStop() {}
    fun onPageBackPressed() {}
    fun onPageDestroy() {}

    fun onPageViewCreated(view: View, savedInstanceState: Bundle?) {}
    fun onPageHiddenChanged(hidden: Boolean) {}
}