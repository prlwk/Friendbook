package com.src.book.data.remote.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

class ErrorMessage<T> {
    fun getErrorMessage(response: Response<T>): String {
        val gson = Gson()
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse =
            gson.fromJson(response.errorBody()!!.charStream(), type)
        return errorResponse.message
    }
}