package com.rongc.wan

data class BaseResponse<T>(val errorCode: Int, val errorMsg: String, val data: T)
