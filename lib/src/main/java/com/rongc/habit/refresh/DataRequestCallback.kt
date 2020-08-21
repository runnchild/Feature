package com.rongc.habit.refresh

interface DataRequestCallback<T> {
    /**
     * 数据获取成功
     * @param page 此次请求的页码
     * @param data 此次请求的数据
     */
    fun onSuccess(page: Int, data: T)

    /**
     * 数据请求失败
     * @param page 此次请求的页码
     */
    fun onFailed(page: Int)
}