package com.rexoit.cobra.ui.userinfo

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rexoit.cobra.CobraApplication
import com.rexoit.cobra.CobraViewModelFactory
import com.rexoit.cobra.R
import com.rexoit.cobra.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_user_info.*

private const val TAG = "userInfoActivity"

class UserInfoActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        CobraViewModelFactory(
            (application as CobraApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        setSupportActionBar(userinfo_tool_bar_id)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

//    private fun getUserInfo() {
//        lifecycleScope.launch(Dispatchers.IO) {
//            viewModel.getUserInfo().collect { response ->
//                when (response.status) {
//                    Status.SUCCESS -> {
//                        Log.d(TAG, "onCreate: ${response.data}")
//                    }
//                    Status.ERROR -> {
//                        Log.d(TAG, "onCreate: ${response.message}")
//                    }
//                    Status.LOADING -> {
//                        Log.d(TAG, "onCreate: Loading")
//                    }
//                    Status.UNAUTHORIZED -> {
//
//                    }
//                }
//            }
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}