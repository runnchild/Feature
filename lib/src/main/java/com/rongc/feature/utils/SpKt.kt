package com.rongc.feature.utils
import com.blankj.utilcode.util.SPUtils
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Desc: SharePreference代理类
 * Author: QiuRonaC
 * Date: 19-3-29
 * Copyright: Copyright (c) 2013-2018
 * Company: @米冠网络
 * Update Comments:
 * @param relatedToAccount  是否跟账号关联 true 在退出登录的时候应被清空 {@link SpUtils#getInstance#clear}
 */
@Suppress("UNCHECKED_CAST")
class SpKt<T>(val key: String, val default: T, spName: String? = null, relatedToAccount: Boolean = true) : ReadWriteProperty<Any?, T> {

    private val sp by lazy {
        if (!relatedToAccount) {
            SPUtils.getInstance("Do Not Related To Login")
        } else {
            SPUtils.getInstance(spName)
        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val any = when (default) {
            is String -> sp.getString(key, default)
            is Boolean -> sp.getBoolean(key, default)
            is Int -> sp.getInt(key, default)
            is Long -> sp.getLong(key, default)
            is Float -> sp.getFloat(key, default)
            is Set<*> -> sp.getStringSet(key, default as Set<String>)
            else -> default
        }
        return any as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        when (default) {
            is String -> sp.put(key, value as String)
            is Boolean -> sp.put(key, value as Boolean)
            is Int -> sp.put(key, value as Int)
            is Long -> sp.put(key, value as Long)
            is Float -> sp.put(key, value as Float)
            is Set<*> -> sp.getStringSet(key, value as? Set<String>)
        }
    }
}