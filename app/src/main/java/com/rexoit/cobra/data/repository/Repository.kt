package com.rexoit.cobra.data.repository

import com.rexoit.cobra.data.remote.CobraApiService

class Repository(private val cobraApiService: CobraApiService) {
    suspend fun login(email: String, password: String) = cobraApiService.login(email, password)
}