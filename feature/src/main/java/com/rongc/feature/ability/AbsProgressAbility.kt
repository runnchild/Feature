package com.rongc.feature.ability

import android.content.Context
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import com.rongc.feature.vo.Resource
import com.rongc.feature.vo.Status
import kotlinx.coroutines.*

abstract class AbsProgressAbility(context: Context) : IAbility {
    var delay = 400L

    open val dialog: AlertDialog by lazy {
        AlertDialog.Builder(context)
            .setView(ProgressBar(context))
            .create()
    }

    private var dialogJob: Job? = null

    fun showDialog() {
        dialogJob?.cancel()
        dialogJob = GlobalScope.launch(Dispatchers.Main) {
            // 延迟400ms，如果期间页面响应并关闭了弹窗，则不会弹出
            withContext(Dispatchers.IO) {
                delay(delay)
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

fun AbsProgressAbility.showIfLoading(it: Resource<*>?) {
    if (it?.status == Status.LOADING) {
        showDialog()
    } else {
        dismissDialog()
    }
}