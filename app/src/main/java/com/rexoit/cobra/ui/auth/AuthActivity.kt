package com.rexoit.cobra.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rexoit.cobra.CobraApplication
import com.rexoit.cobra.CobraViewModelFactory
import com.rexoit.cobra.R
import com.rexoit.cobra.ui.auth.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

private const val TAG = "AuthActivity"

class AuthActivity : AppCompatActivity() {
    private val authViewModel by viewModels<AuthViewModel> {
        CobraViewModelFactory(
            (application as CobraApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        runBlocking {
            authViewModel.login("tauhid8k@example.com", "12345678").collect {
                Log.d(TAG, "onCreate: Login Response: " + it)
            }
        }
    }
}