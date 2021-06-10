package com.rongc.feature.ability.impl

import com.rongc.feature.ui.host.IHost
import com.rongc.feature.vo.Resource

private fun IHost<*>.findProgressAbility(): AbsProgressAbility {
    return findAbility {
        it is AbsProgressAbility
    } as? AbsProgressAbility
        ?: throw IllegalArgumentException("need to register ProgressAbility first!!!")
}

fun IHost<*>.showProgressIfLoading(resource: Resource<*>?) {
    findProgressAbility().showProgressIfLoading(resource)
}

fun IHost<*>.showProgress() {
    findProgressAbility().showDialog()
}

fun IHost<*>.dismissProgress() {
    findProgressAbility().dismissDialog()
}