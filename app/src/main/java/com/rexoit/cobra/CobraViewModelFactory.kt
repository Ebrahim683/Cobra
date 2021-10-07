package com.rexoit.cobra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rexoit.cobra.data.repository.CobraRepo
import com.rexoit.cobra.ui.auth.viewmodel.AuthViewModel
import com.rexoit.cobra.ui.main.viewmodel.MainViewModel

class CobraViewModelFactory(private val repository: CobraRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}