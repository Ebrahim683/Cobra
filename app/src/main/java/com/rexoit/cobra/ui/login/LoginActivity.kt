package com.rexoit.cobra.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rexoit.cobra.R
import com.rexoit.cobra.ui.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        go_to_sign_up.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        btn_login.setOnClickListener {
            logIn()
        }

    }

    //login process
    private fun logIn() {
        email = login_email_edit_text.text.toString().trim()
        password = login_password_edit_text.text.toString().trim()

    }
}