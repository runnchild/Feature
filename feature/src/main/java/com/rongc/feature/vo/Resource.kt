/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.rongc.feature.vo

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(val status: Status, val data: T?, val error: Throwable?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(error: Throwable?, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, error)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

val Resource<*>?.isSuccess get() = this?.status == Status.SUCCESS
val Resource<*>?.isError get() = this?.status == Status.ERROR
val Resource<*>?.isLoading get() = this?.status == Status.LOADING

fun <T> Resource<T>?.doOnSuccess(block: (T) -> Unit) {
    if (this != null && isSuccess) {
        block(data!!)
    }
}

fun <T> Resource<T>?.doOnError(block: (Throwable) -> Unit) {
    if (this != null && isError) {
        block(error ?: Exception())
    }
}

fun <T> Resource<T>?.doOnLoading(block: (T?) -> Unit) {
    if (this != null && isLoading) {
        block(data)
    }
}
