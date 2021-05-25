package com.rongc.feature.ui.kt

import com.rongc.feature.ability.AbsProgressAbility
import com.rongc.feature.ability.showProgressIfLoading
import com.rongc.feature.ui.host.IHost
import com.rongc.feature.vo.Resource

fun IHost<*>.showProgressIfLoading(resource: Resource<*>?) {
    findAbility {
        it is AbsProgressAbility
    }?.let {
        it as AbsProgressAbility
        it.showProgressIfLoading(resource)
    } ?: throw IllegalArgumentException("need to register ProgressAbility first!!!")
}