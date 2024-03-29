/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package com.rongc.feature.utils

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.rongc.feature.ui.host.ActivityHost
import com.rongc.feature.ui.host.IHost
import java.util.Collection
import java.util.Map
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A lazy property that gets cleaned up when the fragment's view is destroyed or the activity is destroyed.
 *
 * Accessing this variable while the fragment's view or activity is destroyed will throw NPE.
 */
class AutoClearedValue<T : Any>(host: IHost) : ReadWriteProperty<Any?, T> {
    private var _value: T? = null

    init {
        val lifecycle = if (host.host is ActivityHost) {
            (host as ComponentActivity).lifecycle
        } else {
            (host as Fragment).lifecycle
        }
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                clearCollection(_value)
                _value = null
            }

            override fun onCreate(owner: LifecycleOwner) {
                (host as? Fragment)?.viewLifecycleOwnerLiveData?.observe(host) { viewLifecycleOwner ->
                    viewLifecycleOwner?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            clearCollection(_value)
                            _value = null
                        }
                    })
                }
            }

            private fun clearCollection(value: T?) {
                if (value is Collection<*>) {
                    value.clear()
                } else if (value is Map<*, *>) {
                    value.clear()
                }
            }
        })
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return _value ?: throw IllegalStateException(
            "should never call auto-cleared-value get when it might not be available"
        )
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        _value = value
    }
}

/**
 * Creates an [AutoClearedValue] associated with this Host.
 */
fun <T : Any> IHost.autoCleared() = AutoClearedValue<T>(this)