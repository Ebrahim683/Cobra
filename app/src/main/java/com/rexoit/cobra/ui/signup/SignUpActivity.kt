package com.rexoit.cobra.ui.signup

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
import com.rexoit.cobra.ui.login.LoginActivity
import com.rexoit.cobra.ui.main.MainActivity
import com.rexoit.cobra.utils.Status
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "signUpActivity"

class SignUpActivity : AppCompatActivity() {

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var password: String

    private val viewModel by viewModels<AuthViewModel> {
        CobraViewModelFactory(
            (application as CobraApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        go_to_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btn_sign_up.setOnClickListener {
            name = name_edit_text.text.toString()
            email = email_edit_text.text.toString().trim()
            phone = mobile_number_edit_text.text.toString().trim()
            password = password_edit_text.text.toString().trim()

            if (name.isEmpty()) {
                Snackbar.make(
                    signup_activity,
                    "Enter Name",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            if (email.isEmpty()) {
                Snackbar.make(
                    signup_activity,
                    "Enter Email",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            if (phone.isEmpty()) {
                Snackbar.make(
                    signup_activity,
                    "Enter Mobile Number",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            if (password.isEmpty()) {
                Snackbar.make(
                    signup_activity,
                    "Enter Password",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val progressDialog = ProgressDialog(this)
                progressDialog.setCancelable(false)
                progressDialog.show()
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.registration(name, email, phone, password)
                        .collect { resource ->
                            Log.d(TAG, "onCreate: Registration Response: " + resource)

                            when (resource.status) {
                                Status.SUCCESS -> {
                                    Log.d(TAG, "signUp: SUCCESS")
                                    startActivity(
                                        Intent(
                                            this@SignUpActivity,
                                            MainActivity::class.java
                                        )
                                    )
                                    finish()
                                    progressDialog.dismiss()
                                }
                                Status.ERROR -> {
                                    Log.d(TAG, "signUp: ${resource.message}")
                                    Snackbar.make(
                                        signup_activity,
                                        "Something Went Wrong",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                    progressDialog.dismiss()
                                }
                                Status.LOADING -> {
                                    Log.d(TAG, "signUp: Loading...")
                                }
                                Status.UNAUTHORIZED -> {
                                    Log.d(TAG, "signUp: UNAUTHORIZED")
                                    progressDialog.dismiss()
                                }
                            }
                        }
                }
            }
        }

    }
}