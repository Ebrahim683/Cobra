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
}