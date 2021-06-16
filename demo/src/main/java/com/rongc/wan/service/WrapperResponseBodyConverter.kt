package com.rongc.wan.service

import com.google.gson.Gson
import com.rongc.wan.WanResponse
import okhttp3.ResponseBody
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Converter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class WrapperResponseBodyConverter<T> internal constructor(private val mType: Type, private val gson: Gson) :
    Converter<ResponseBody, T?> {

    override fun convert(value: ResponseBody): T? {
        val result = value.string()
        val data = JSONTokener(result).nextValue()
        if (data is JSONObject) {
            return gson.fromJson<T>(
                if ((mType as? ParameterizedType)?.rawType == WanResponse::class.java) {
                    // 如果需要完整数据格式，Api方法的返回值类型为LiveData<ApiResponse<WanResponse<T>>>
                    result
                } else {
                    // 否则不关注错误码，只接收data，错误码不为0（成功）时抛出
                    val code = data.optInt("errorCode", -1)
                    if (code != 0) {
                        throw ServicesException(code, data.optString("errorMsg"))
                    }
                    data.optString("data")
                }, mType
            )
        }
        throw ServicesException(400, "response error, response value = $result")
    }
}