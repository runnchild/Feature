package com.rongc.feature.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/6/19
 */

/**
 * 同一个Event只会接收到回调一次 */
inline fun <T> LiveData<Event<T>>.observeOnce(
    owner: LifecycleOwner, crossinline onEventUnhandledContent: (T) -> Unit
) {
    observe(owner) {
        it?.getContentIfNotHandled()?.let(onEventUnhandledContent)
    }
}

/**
 * 保证所有事件不丢失，保存非激活状态时的事件，并能够在激活后依次回调，且没有内存泄漏
 */
fun <T> LiveData<T>.observeAny(owner: LifecycleOwner, observer: Observer<T>) {
    AnyEventObserver.bind(this, owner, observer)
}

/**
 * 根据事件名称创建LiveDataBus
 */
fun <T> String.liveBus(): LiveDataBus.StickyLiveData<T> {
    return LiveDataBus.with(this)
}