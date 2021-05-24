package com.rongc.feature.ability

import android.content.Context
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*

abstract class AbsProgressAbility(context: Context) : IAbility {

    open val dialog: AlertDialog by lazy {
        AlertDialog.Builder(context)
            .setView(ProgressBar(context))
            .create()
    }

    private var dialogJob: Job? = null

    fun showDialog() {
        dialogJob?.cancel()
        dialogJob = GlobalScope.launch(Dispatchers.Main) {
            // 延迟500ms，如果期间页面响应并关闭了弹窗，则不会弹出
            withContext(Dispatchers.IO) {
                delay(500)
            }
            if (!dialog.isShowing) {
                dialog.show()
            }
        }
    }

    fun dismissDialog() {
        dialogJob?.cancel()
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        dismissDialog()
    }
}