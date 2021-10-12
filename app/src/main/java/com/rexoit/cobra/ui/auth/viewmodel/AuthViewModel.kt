package com.rexoit.cobra.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.rexoit.cobra.data.model.registration.RegistrationResponse
import com.rexoit.cobra.data.model.user.LoginResponse
import com.rexoit.cobra.data.remote.RetrofitBuilder
import com.rexoit.cobra.data.repository.CobraRepo
import com.rexoit.cobra.utils.Resource
import kotlinx.coroutines.flow.flow

class AuthViewModel(private val repository: CobraRepo) : ViewModel() {

    //Login
    fun login(email: String, password: String) = flow {
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
        flow{
            emit(Resource.loading(null))

            try {
                val retrofit =
                    RetrofitBuilder.getApiService(null)
                emit(Resource.success(retrofit.registration(name, email, phone, password)))
            } catch (e: Exception) {
                emit(Resource.error(null, "${e.localizedMessage}"))
            }
        }


    //Email Verification
    fun emailVerification(email: String, verificationCode: String) = flow<Resource<Any>> {
        emit(Resource.loading(null))

        try {
            val retrofit = RetrofitBuilder.getApiService(null)
            emit(Resource.success(retrofit.verifyEmailAddress(email, verificationCode)))
        } catch (e: java.lang.Exception) {
            emit(Resource.error(null, "${e.localizedMessage}"))
        }
    }

    //Resend Code
    fun resendCode(email: String) = flow<Resource<Any>> {
        emit(Resource.loading(null))

        try {
            val retrofit = RetrofitBuilder.getApiService(null)
            emit(Resource.success(retrofit.resendVerificationEmail(email)))
        } catch (e: java.lang.Exception) {
            emit(Resource.error(null, "${e.localizedMessage}"))
        }
    }


}