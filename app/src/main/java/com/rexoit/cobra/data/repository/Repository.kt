package com.rexoit.cobra.data.repository

import com.rexoit.cobra.data.remote.CobraApiService
import com.rexoit.cobra.data.remote.RetrofitBuilder
import com.rexoit.cobra.utils.Resource
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class Repository(private val cobraApiService: CobraApiService) {
    suspend fun login(email: String, password: String) = cobraApiService.login(email, password)

}