package com.rongc.feature.utils

import android.annotation.SuppressLint
import android.os.Build
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.PhoneUtils

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2020/9/7
 * @since 2.1.4
 */
object DeviceUtils {
    fun getImei(call: (String) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
             ""
        }
        PermissionUtils.permission(PermissionConstants.PHONE)
            .callback(object : PermissionUtils.SimpleCallback {
                @SuppressLint("MissingPermission")
                override fun onGranted() {
                    call(PhoneUtils.getIMEI())
                }

                override fun onDenied() {
                }
            })
            .request()
    }
}