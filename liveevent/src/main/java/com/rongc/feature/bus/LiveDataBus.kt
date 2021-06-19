package com.rongc.feature.bus

import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap

object LiveDataBus {

    private val eventMap = ConcurrentHashMap<String, StickyLiveData<*>>()

    fun <T> with(eventName: String): StickyLiveData<T> {
        //基于事件名称 订阅、分发消息，
        //由于 一个 livedata 只能发送 一种数据类型
        //所以 不同的event事件，需要使用不同的livedata实例 去分发
        var liveData = eventMap[eventName]
        if (liveData == null) {
            liveData = StickyLiveData<T>(eventName)
            eventMap[eventName] = liveData
        }
        @Suppress("UNCHECKED_CAST")
        return liveData as StickyLiveData<T>
    }

    class StickyLiveData<T>(private val eventName: String) : MutableLiveData<T>() {
        internal var mStickyData: T? = null
        internal var mVersion = 0

        fun setStickyValue(stickyData: T) {
            mStickyData = stickyData
            //在主线程去发送数据
            setValue(stickyData)
        }

        fun postStickyValue(stickyData: T) {
            mStickyData = stickyData
            //不受线程的限制
            postValue(stickyData)
        }

        override fun setValue(value: T) {
            mVersion++
            super.setValue(value)
        }

        override fun postValue(value: T) {
            mVersion++
            super.postValue(value)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            observerSticky(owner, false, observer)
        }

        fun observerSticky(owner: LifecycleOwner, sticky: Boolean, observer: Observer<in T>) {
            //允许指定注册的观察者 是否需要关心黏性事件
            //sticky =true, 如果之前存在已经发送的数据，那么这个observer会受到之前的黏性事件消息
            owner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                //监听 宿主 发生销毁事件，主动把livedata 移除掉。
                if (event == Lifecycle.Event.ON_DESTROY) {
                    if (eventMap[eventName]?.hasObservers() == false) {
                        eventMap.remove(eventName)
                    }
                    @Suppress("UNCHECKED_CAST")
                    eventMap[eventName]?.removeObserver(observer as Observer<Any?>)
                }
            })
            super.observe(owner, StickyObserver(this, sticky, observer))
        }
    }

    class StickyObserver<T>(
        val stickyLiveData: StickyLiveData<T>, val sticky: Boolean, val observer: Observer<in T>
    ) : Observer<T> {
        //lastVersion 和livedata的version 对齐的原因，就是为控制黏性事件的分发。
        //sticky 不等于true , 只能接收到注册之后发送的消息，如果要接收黏性事件，则sticky需要传递为true
        private var lastVersion = stickyLiveData.mVersion
        override fun onChanged(t: T) {
            if (lastVersion >= stickyLiveData.mVersion) {
                //说明stickyLiveData  没有更新的数据需要发送。
                if (sticky && stickyLiveData.mStickyData != null) {
                    observer.onChanged(stickyLiveData.mStickyData)
                }
                return
            }

            lastVersion = stickyLiveData.mVersion
            observer.onChanged(t)
        }
    }
}