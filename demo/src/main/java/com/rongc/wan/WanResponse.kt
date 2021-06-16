package com.rongc.wan

data class WanResponse<T>(val errorCode: Int, val errorMsg: String, val data: T)
