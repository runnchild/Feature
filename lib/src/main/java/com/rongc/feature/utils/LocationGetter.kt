package com.rongc.feature.utils

import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.Utils
import java.util.*

/**
 * Desc: 当前城市位置获取类
 * Author: QiuRonaC
 * Date: 19-10-17
 * Copyright: Copyright (c) 2013-2018
 * Company: @米冠网络
 * Update Comments:
 */
object LocationGetter {
    @JvmStatic
    fun requestLocation(call: (Address?) -> Unit) {
        fun request() =
            (Utils.getApp().getSystemService(Context.LOCATION_SERVICE) as? LocationManager)?.run {
                val criteria = Criteria()
                criteria.accuracy = Criteria.ACCURACY_COARSE//低精度，如果设置为高精度，依然获取不了location。
                criteria.isAltitudeRequired = false//不要求海拔
                criteria.isBearingRequired = false//不要求方位
                criteria.isCostAllowed = true//允许有花费
                criteria.powerRequirement = Criteria.POWER_LOW//低功耗

                // 获取所有可用的位置提供者
                val providers = getProviders(true)
                val provider = getBestProvider(criteria, true) ?: when {
                    providers.contains(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                    providers.contains(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                    else -> null
                }
                provider ?: let {
                    call(null)
                    return@run
                }
                getLocation(this, provider, call)
            }

        PermissionUtils.permission(PermissionConstants.LOCATION)
            .callback(object : PermissionUtils.SimpleCallback {
                override fun onGranted() {
                    request()
                }

                override fun onDenied() {
                    request()
                }
            }).request()
    }

    private fun getLocation(manager: LocationManager, provider: String, call: (Address?) -> Unit) {
        val permission = ActivityCompat.checkSelfPermission(
            Utils.getApp(),
            android.Manifest.permission.LOCATION_HARDWARE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            val location = manager.getLastKnownLocation(provider)
            if (location != null) {
                parseLocation(location, call)
            } else {
                manager.requestLocationUpdates(provider, 1000L, 0f, object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        parseLocation(location, call)
                        manager.removeUpdates(this)
                    }

                    override fun onStatusChanged(
                        provider: String?,
                        status: Int,
                        extras: Bundle?
                    ) {
                    }

                    override fun onProviderEnabled(provider: String?) {
                    }

                    override fun onProviderDisabled(provider: String?) {
                    }
                })
            }
        }
    }
}

private fun parseLocation(location: Location, call: (Address?) -> Unit) {
    try {
        Geocoder(Utils.getApp(), Locale.CHINA)
            .getFromLocation(location.latitude, location.longitude, 1).forEach {
                call(it)
            }
    } catch (e: Exception) {
        // if the network is unavailable or any other I/O problem occurs
        call(null)
        e.printStackTrace()
    }
}