package com.rexoit.cobra.ui.auth

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.rexoit.cobra.CobraApplication
import com.rexoit.cobra.CobraViewModelFactory
import com.rexoit.cobra.R
import com.rexoit.cobra.ui.auth.viewmodel.AuthViewModel
import com.rexoit.cobra.ui.login.LoginActivity
import com.rexoit.cobra.utils.SharedPrefUtil
import com.rexoit.cobra.utils.Status
import kotlinx.android.synthetic.main.activity_auth.*
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
            authViewModel.login("tauhid8k@example.com", "12345678").collect { resource ->
                Log.d(TAG, "onCreate: Login Response: " + resource)

                when (resource.status) {
                    Status.UNAUTHORIZED -> {
                        alertDialog()
                    }
                    Status.LOADING -> {
                        Snackbar.make(auth_activity, "Loading...", Snackbar.LENGTH_SHORT).show()
                    }
                    Status.ERROR -> {
                        Snackbar.make(auth_activity, "Something Went Wrong", Snackbar.LENGTH_SHORT)
                            .show()
                        Log.d(TAG, "onCreate: ${resource.message}")
                    }
                    Status.SUCCESS -> {
                        val sharedPrefUtil = SharedPrefUtil(applicationContext)
                        sharedPrefUtil.setUserToken(resource.data?.token.toString())
                    }
                }
            }
        }
    }

    private fun alertDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage("Login Required")
            .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                startActivity(Intent(this@AuthActivity, LoginActivity::class.java))
                finish()
            }
        alertDialog.show()
    }

//    fun progressDialog(){
//        val progressDialog = ProgressDialog(this)
//        progressDialog.apply {
//            setCancelable(false)
//            setMessage("Wait...")
//        }
//        progressDialog.show()
//    }
}