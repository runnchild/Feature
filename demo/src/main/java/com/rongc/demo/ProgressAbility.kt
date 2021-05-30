package com.rongc.demo

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.rongc.feature.ability.impl.AbsProgressAbility

class ProgressAbility(context: Context): AbsProgressAbility(context) {
    override val dialog: AlertDialog
        get() = super.dialog
}