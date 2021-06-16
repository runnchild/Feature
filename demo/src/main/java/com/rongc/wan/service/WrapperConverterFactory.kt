package com.rongc.wan.service

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class WrapperConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return WrapperResponseBodyConverter<Any>(type, gson)
    }

    companion object {
        fun create(gson: Gson? = Gson()): WrapperConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return WrapperConverterFactory(gson)
        }
    }
}