package com.rongc.feature.ui.host

import androidx.annotation.CallSuper
import com.rongc.feature.ability.IAbility

interface IAbilityList: IHost {
    val abilities: ArrayList<IAbility>

    @CallSuper
    fun registerAbility(ability: IAbility) {
        abilities.add(ability)
        lifecycleOwner.lifecycle.addObserver(ability)
    }

    fun findAbility(predicate: (IAbility) -> Boolean): IAbility? {
        return abilities.firstOrNull(predicate)
    }

    @CallSuper
    fun onHostDestroy() {
        abilities.clear()
    }
}