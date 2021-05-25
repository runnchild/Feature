package com.rongc.feature.ui.host

import androidx.annotation.CallSuper
import com.rongc.feature.ability.IAbility

interface IAbilityHost {
    val abilities: ArrayList<IAbility>

    @CallSuper
    fun registerAbility(ability: IAbility) {
        abilities.add(ability)
    }

    fun findAbility(predicate: (IAbility) -> Boolean): IAbility? {
        return abilities.firstOrNull(predicate)
    }

    @CallSuper
    fun onHostDestroy() {
        abilities.clear()
    }
}