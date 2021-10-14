package com.rexoit.cobra.ui.login

import android.app.ProgressDialog
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
import com.rexoit.cobra.ui.main.MainActivity
import com.rexoit.cobra.ui.signup.SignUpActivity
import com.rexoit.cobra.utils.SharedPrefUtil
import com.rexoit.cobra.utils.Status
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPrefUtil: SharedPrefUtil
    private lateinit var email: String
    private lateinit var password: String
    private val viewModel by viewModels<AuthViewModel> {
        CobraViewModelFactory(
            (application as CobraApplication).repository
        )
    }

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPrefUtil = SharedPrefUtil(applicationContext)
        progressDialog = ProgressDialog(this)
        progressDialog.apply {
            setCancelable(false)
            setMessage("Logging in...")
        }

        go_to_sign_up.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        btn_login.setOnClickListener {
            email = login_email_edit_text.text.toString().trim()
            password = login_password_edit_text.text.toString().trim()

            if (email.isEmpty()) {
                Snackbar.make(login_activity, "Enter Email", Snackbar.LENGTH_SHORT).show()
            }
            if (password.isEmpty()) {
                Snackbar.make(login_activity, "Enter Password", Snackbar.LENGTH_SHORT).show()
            } else {
                runBlocking {
                    viewModel.login(email, password).collect { resource ->
                        Log.d(TAG, "onCreate: Login Response: $resource")

                        when (resource.status) {
                            Status.SUCCESS -> {
                                val token = resource.data?.token.toString()

                                sharedPrefUtil.setUserToken(token)

                                // login success. get user profile info
                                getUserInfo(token)
                            }
                            Status.ERROR -> {
                                Log.d(TAG, "onCreate: ${resource.message}")
                                Snackbar.make(
                                    login_activity,
                                    "${resource.message}",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                progressDialog.dismiss()
                            }
                            Status.LOADING -> {
                                progressDialog.show()
                            }
                            Status.UNAUTHORIZED -> {
                                Snackbar.make(
                                    login_activity,
                                    "${resource.message}",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                progressDialog.dismiss()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getUserInfo(token: String) {
        runBlocking {
            viewModel.getUserInfo(token).collect { resource ->
                Log.d(TAG, "onCreate: Login Response: $resource")
                progressDialog.dismiss()

                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.user?.email?.let {
                            sharedPrefUtil.setUserEmail(it)
                            viewModel.setUserInfo(resource.data.user)
                        }

                        progressDialog.dismiss()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    Status.ERROR -> {
                        Log.d(TAG, "onCreate: ${resource.message}")
                        Snackbar.make(
                            login_activity,
                            "${resource.message}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        progressDialog.dismiss()
                    }
                    Status.LOADING -> {
                        progressDialog.show()
                    }
                    Status.UNAUTHORIZED -> {
                        Snackbar.make(
                            login_activity,
                            "${resource.message}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (sharedPrefUtil.getUserToken() != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}