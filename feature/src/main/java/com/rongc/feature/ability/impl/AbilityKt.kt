package com.rongc.feature.ability.impl

import com.rongc.feature.ui.host.IAbilityList
import com.rongc.feature.vo.Resource

private fun IAbilityList.findProgressAbility(): AbsProgressAbility {
    return findAbility {
        it is AbsProgressAbility
    } as? AbsProgressAbility
        ?: throw IllegalArgumentException("need to register ProgressAbility first!!!")
}

fun IAbilityList.showProgressIfLoading(resource: Resource<*>?) {
    findProgressAbility().showProgressIfLoading(resource)
}

fun IAbilityList.showProgress() {
    findProgressAbility().showDialog()
}

fun IAbilityList.dismissProgress() {
    findProgressAbility().dismissDialog()
}