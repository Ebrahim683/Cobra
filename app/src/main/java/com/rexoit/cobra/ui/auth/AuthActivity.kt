package com.rexoit.cobra.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.rexoit.cobra.CobraApplication
import com.rexoit.cobra.CobraViewModelFactory
import com.rexoit.cobra.R
import com.rexoit.cobra.ui.auth.viewmodel.AuthViewModel
import com.rexoit.cobra.utils.SharedPrefUtil
import com.rexoit.cobra.utils.Status
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        lifecycleScope.launchWhenStarted {
            verifyEmail()
        }
    }

    private suspend fun verifyEmail() {
        CoroutineScope(Dispatchers.IO).launch {
            authViewModel.emailVerification("tauhid8k@example.com", "verificationCode")
                .collect { resource ->
                    Log.d(TAG, "onCreate: Verification Response: $resource")
                    when (resource.status) {
                        Status.SUCCESS -> {
                            Log.d(TAG, "onCreate: Verification Success")
                            withContext(Dispatchers.Main) {
                                Log.d(TAG, "verifyEmail: Verification Success")
                                Snackbar.make(
                                    auth_activity,
                                    "Verification Success",
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                        Status.ERROR -> {
                            Log.d(TAG, "onCreate: ${resource.message}")
                            Snackbar.make(
                                auth_activity,
                                "Something Went Wrong",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                        Status.LOADING -> {
                            Snackbar.make(
                                auth_activity,
                                "Wait Please...",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                        Status.UNAUTHORIZED -> {

                        }
                    }
                }
        }
    }

    private suspend fun resendVerificationCode() {
        CoroutineScope(Dispatchers.IO).launch {
            authViewModel.resendCode("tauhid8k@example.com").collect { resource ->
                Log.d(TAG, "onCreate: Resend Response: $resource")

                when (resource.status) {
                    Status.SUCCESS -> {
                        Log.d(TAG, "onCreate: ${resource.data.toString()}")
                        Snackbar.make(
                            auth_activity,
                            resource.data.toString(),
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                    Status.ERROR -> {
                        Log.d(TAG, "onCreate: ${resource.message}")
                        Snackbar.make(auth_activity, "Something Went Wrong", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    Status.LOADING -> {
                        Snackbar.make(auth_activity, "Loading...", Snackbar.LENGTH_SHORT).show()
                    }
                    Status.UNAUTHORIZED -> {

                    }
                }
            }
        }
    }

}