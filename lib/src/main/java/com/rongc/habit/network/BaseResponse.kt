package com.rongc.habit.network

class BaseResponse<T> {
    /**
     * 0	成功
     * 4444	请求参数与参数签名不匹配
     * 4000	token验证有误，
     * 4001	必要的参数缺失
     * 4002	请求已过期
     */
    var code: Int = -1
    var msg: String = ""
    var data: T? = null
}