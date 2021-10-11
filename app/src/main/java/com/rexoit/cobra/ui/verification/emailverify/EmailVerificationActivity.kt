package com.rexoit.cobra.ui.verification.emailverify

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
import com.rexoit.cobra.ui.login.LoginActivity
import com.rexoit.cobra.ui.verification.resendcode.ResendCodeActivity
import com.rexoit.cobra.utils.Status
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_email_verification.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

private const val TAG = "emailVerification"
const val EXTRA_EMAIL_ADDRESS = "com.rexoit.cobra.ui.verification.emailverify.EXTRA_EMAIL_ADDRESS"

class EmailVerificationActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthViewModel> {
        CobraViewModelFactory(
            (application as CobraApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verification)
        this.window.statusBarColor = resources.getColor(R.color.white)


        btn_verify.setOnClickListener {
            runBlocking {
                verifyEmail()
            }
        }

        go_to_resend_verification_code.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ResendCodeActivity::class.java
                )
            )
        }

    }

    private suspend fun verifyEmail() {

        val email = intent.getStringExtra(EXTRA_EMAIL_ADDRESS)!!
        val verificationCode = verify_code_edit_text.text.toString().trim()

        when {
            verificationCode.isEmpty() -> {
                Snackbar.make(
                    email_verify_layout,
                    "Enter Password",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
            else -> {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.emailVerification(email, verificationCode)
                        .collect { resource ->
                            Log.d(TAG, "onCreate: Verification Response: $resource")
                            when (resource.status) {
                                Status.SUCCESS -> {
                                    if (resource.data?.equals(true)!!) {
                                        Log.d(TAG, "onCreate: Verification Success")
                                        withContext(Dispatchers.Main) {
                                            Log.d(TAG, "verifyEmail: Verification Success")
                                            startActivity(
                                                Intent(
                                                    this@EmailVerificationActivity,
                                                    LoginActivity::class.java
                                                )
                                            )
                                            finish()
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            Log.d(TAG, "verifyEmail: Verification Fail")
                                            Snackbar.make(
                                                email_verify_layout,
                                                "Verification Fail",
                                                Snackbar.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }
                                }
                                Status.ERROR -> {
                                    Log.d(TAG, "onCreate: ${resource.message}")
                                    Snackbar.make(
                                        email_verify_layout,
                                        "Something Went Wrong",
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                Status.LOADING -> {
                                    Snackbar.make(
                                        email_verify_layout,
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

}