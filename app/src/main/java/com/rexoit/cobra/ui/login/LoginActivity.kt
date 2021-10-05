package com.rexoit.cobra.ui.login

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
import com.rexoit.cobra.utils.Status
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private val viewmodel by viewModels<AuthViewModel> {
        CobraViewModelFactory(
            (application as CobraApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


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
                    viewmodel.login(email, password).collect { resource ->
                        Log.d(TAG, "onCreate: Login Response: " + resource)

                        when (resource.status) {
                            Status.SUCCESS -> {
                                Log.d(TAG, "onCreate: ${resource.message}")
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            }
                            Status.ERROR -> {
                                Log.d(TAG, "onCreate: ${resource.message}")
                                Snackbar.make(
                                    login_activity,
                                    "Something Went Wrong",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                            Status.LOADING -> {

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