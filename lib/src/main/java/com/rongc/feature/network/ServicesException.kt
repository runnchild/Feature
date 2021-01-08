package com.rongc.feature.network

class ServicesException(val code: Int, message: String?, val error: Throwable? = null): Exception(message) {
    companion object {
        /**
         * 网络链接失败
         */
        const val CODE_CONNECTED = 5000

        /**
         * 其他没有被定义的错误
         */
        const val CODE_OTHER = 6000
    }
}