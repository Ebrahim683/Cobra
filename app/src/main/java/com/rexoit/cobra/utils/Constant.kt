package com.rexoit.cobra.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

fun <T> result(call: suspend () -> Response<T>): Flow<Resource<T>> = flow {

    try {
        val c = call()
        c.let { response ->
            if (c.isSuccessful) {
                emit(Resource.success(response.body()) as Resource<T>)
            } else {
                c.errorBody().let { error ->
                    emit(Resource.error(null, error.toString()))
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}