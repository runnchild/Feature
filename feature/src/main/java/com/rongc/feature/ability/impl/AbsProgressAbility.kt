package com.rongc.feature.ability.impl

import android.content.Context
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import com.rongc.feature.ability.IAbility
import com.rongc.feature.vo.Resource
import com.rongc.feature.vo.Status
import kotlinx.coroutines.*

/**
 * 显示进度弹窗能力
 * 先在Activity/Fragment中registerAbility(AbsProgressAbility(context)),
 * 在网络请求或者耗时操作时可调用#showDialog()显示弹窗, #dismissDialog()关闭弹窗。
 * 当showDialog后会延时delay毫秒后显示，如果期间被关闭了则此次不会显示弹窗。
 */
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
            delay(delay)
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

fun AbsProgressAbility.showProgressIfLoading(it: Resource<*>?) {
    if (it?.status == Status.LOADING) {
        showDialog()
    } else {
        dismissDialog()
    }
}