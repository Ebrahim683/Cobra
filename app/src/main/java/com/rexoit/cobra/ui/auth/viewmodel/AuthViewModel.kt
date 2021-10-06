package com.rexoit.cobra.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.rexoit.cobra.data.model.user.LoginResponse
import com.rexoit.cobra.data.model.user.UserInfo
import com.rexoit.cobra.data.remote.RetrofitBuilder
import com.rexoit.cobra.data.repository.CobraRepo
import com.rexoit.cobra.utils.Resource
import com.rexoit.cobra.utils.result
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class AuthViewModel(private val repository: CobraRepo) : ViewModel() {

    //Login
    fun login(email: String, password: String) = flow<Resource<LoginResponse>> {
        emit(Resource.loading(null))

        try {
            val retrofit = RetrofitBuilder.getApiService(null) // no interceptor required when login
            emit(Resource.success(retrofit.login(email, password)))
        } catch (e: Exception) {
            emit(Resource.error(null, "${e.localizedMessage}"))
        }
    }

    //Registration
    fun registration(name: String, email: String, phone: String, password: String) =
        flow<Resource<Any>> {
            emit(Resource.loading(null))

            try {
                val retrofit =
                    RetrofitBuilder.getApiService(null)
                emit(Resource.success(retrofit.registration(name, email, phone, password)))
            } catch (e: Exception) {
                emit(Resource.error(null, "${e.localizedMessage}"))
            }
        }

    //Add Blocked Number
    fun addBlockedNumber(phone: String) = flow<Resource<Any>> {
        emit(Resource.loading(null))

        try {
            val retrofit = RetrofitBuilder.getApiService(null)
            emit(Resource.success(retrofit.addBlockedNumber(phone)))
        } catch (e: java.lang.Exception) {
            emit(Resource.error(null, "${e.localizedMessage}"))
        }
    }

    fun getBlockedNumbers() = result {
        //Test
//        RetrofitBuilder.getApiService(null).getApi()
        RetrofitBuilder.getApiService(null).getBlockedNumbers() as Response<Any>
    }

    fun getUserInfo() = result {
        RetrofitBuilder.getApiService(null).getUserInfo() as Response<UserInfo>
    }


}