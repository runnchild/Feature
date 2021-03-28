package com.rongc.feature.ui.ability

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.rongc.feature.model.BaseModel
import com.rongc.feature.viewmodel.BaseViewModel

interface IAbility {
    fun onPageCreateView(view: View, firstCreate: Boolean, savedInstanceState: Bundle?) {}
    fun onPageCreate(viewModel: BaseViewModel<out BaseModel>, owner: LifecycleOwner, savedInstanceState: Bundle?) {}
    fun onPageResume() {}
    fun onPagePause() {}
    fun onPageBackPressed() {}
    fun onPageDestroy() {}

    fun onPageViewCreated(view: View, savedInstanceState: Bundle?) {}
    fun onPageHiddenChanged(hidden: Boolean) {}
}