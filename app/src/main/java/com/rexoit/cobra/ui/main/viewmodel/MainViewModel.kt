package com.rexoit.cobra.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.rexoit.cobra.data.model.user.UserInfo
import com.rexoit.cobra.data.remote.RetrofitBuilder
import com.rexoit.cobra.data.repository.CobraRepo
import com.rexoit.cobra.utils.OkHttpClientInterceptor
import com.rexoit.cobra.utils.Resource
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class MainViewModel(repo: CobraRepo) : ViewModel() {
    fun getUserInfo(token: String) = flow<Resource<UserInfo>> {
        val retrofit =
            RetrofitBuilder.getApiService(OkHttpClientInterceptor.getOkHttpClient(token = token))

        emit(Resource.loading(null))

        try {
            emit(Resource.success(retrofit.getUserInfo()))
        } catch (e: HttpException) {
            emit(Resource.unauthorized(null, "${e.localizedMessage}"))
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


    fun getBlockedNumbers() = flow<Resource<Any>> {
        emit(Resource.loading(null))

        try {
            val retrofit = RetrofitBuilder.getApiService(null)

//            Test
//            emit(Resource.success(retrofit.getApi()))

            emit(Resource.success(retrofit.getBlockedNumbers()))
        } catch (e: java.lang.Exception) {
            emit(Resource.error(null, "${e.localizedMessage}"))
        }
    }

}