package com.rongc.feature.ability.impl

import android.view.View
import com.rongc.feature.R

object ViewTreeProgressAbilityOwner {

    fun AbsProgressAbility.set(view: View) {
        view.setTag(R.id.view_tree_progress_ability_owner, this)
    }

    operator fun get(view: View): AbsProgressAbility? {
        var found = view.getTag(R.id.view_tree_progress_ability_owner) as? AbsProgressAbility
        if (found != null) return found
        var parent = view.parent
        while (found == null && parent is View) {
            val parentView = parent as View
            found = parentView.getTag(R.id.view_tree_progress_ability_owner) as? AbsProgressAbility
            parent = parentView.parent
        }
        return found
    }
}

fun View.findViewTreeProgressAbility(): AbsProgressAbility? = ViewTreeProgressAbilityOwner[this]