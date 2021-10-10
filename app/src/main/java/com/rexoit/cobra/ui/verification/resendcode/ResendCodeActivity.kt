package com.rexoit.cobra.ui.verification.resendcode

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.rexoit.cobra.CobraApplication
import com.rexoit.cobra.CobraViewModelFactory
import com.rexoit.cobra.R
import com.rexoit.cobra.ui.auth.viewmodel.AuthViewModel
import com.rexoit.cobra.ui.verification.emailverify.EmailVerificationActivity
import com.rexoit.cobra.utils.Status
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_resend_code.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

private const val TAG = "resendCodeActivity"

class ResendCodeActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthViewModel> {
        CobraViewModelFactory(
            (application as CobraApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resend_code)
        this.window.statusBarColor = resources.getColor(R.color.white)


        btn_resend.setOnClickListener {
            runBlocking {
                resendCode()
            }
        }


    }

    private suspend fun resendCode() {
        val email = resend_verification_email_edit_text.text.toString().trim()

        if (email.isEmpty()) {
            Snackbar.make(
                resend_layout,
                "Enter Email",
                Snackbar.LENGTH_SHORT
            )
                .show()
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.resendCode(email).collect { resource ->
                    Log.d(TAG, "onCreate: Verification Response: $resource")
                    when (resource.status) {
                        Status.SUCCESS -> {
                            Log.d(TAG, "onCreate: Verification Code Was Sent")
                            withContext(Dispatchers.Main) {
                                startActivity(
                                    Intent(
                                        this@ResendCodeActivity,
                                        EmailVerificationActivity::class.java
                                    )
                                )
                            }
                            finish()
                        }
                        Status.ERROR -> {
                            Log.d(TAG, "onCreate: ${resource.message}")
                            Snackbar.make(
                                resend_layout,
                                "Something Went Wrong",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                        Status.LOADING -> {
                            Snackbar.make(
                                resend_layout,
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
    }
}