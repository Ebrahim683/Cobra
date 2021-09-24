package com.rexoit.cobra.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rexoit.cobra.R
import com.rexoit.cobra.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var mobileNumber: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        go_to_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btn_sign_up.setOnClickListener {
            signUp()
        }

    }

    //sign up process
    private fun signUp() {
        name = name_edit_text.text.toString()
        email = email_edit_text.text.toString().trim()
        mobileNumber = mobile_number_edit_text.text.toString().trim()
        password = password_edit_text.text.toString().trim()

    }
}