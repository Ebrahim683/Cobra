package com.rexoit.cobra.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.rexoit.cobra.data.model.user.LoginResponse
import com.rexoit.cobra.data.remote.RetrofitBuilder
import com.rexoit.cobra.data.repository.CobraRepo
import com.rexoit.cobra.utils.Resource
import kotlinx.coroutines.flow.flow

class AuthViewModel(private val repository: CobraRepo) : ViewModel() {
    fun login(email: String, password: String) = flow<Resource<LoginResponse>> {
        emit(Resource.loading(null))

        try {
            val retrofit = RetrofitBuilder.getApiService(null) // no interceptor required when login
            emit(Resource.success(retrofit.login(email, password)))
        } catch (e: Exception) {
            emit(Resource.error(null, "${e.localizedMessage}"))
        }
    }
}