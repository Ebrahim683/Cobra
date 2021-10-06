package com.rexoit.cobra.ui.block

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rexoit.cobra.CobraApplication
import com.rexoit.cobra.CobraViewModelFactory
import com.rexoit.cobra.R
import com.rexoit.cobra.ui.auth.viewmodel.AuthViewModel
import com.rexoit.cobra.utils.Status
import kotlinx.android.synthetic.main.activity_block_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "blockListActivity"

class BlockListActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthViewModel> {
        CobraViewModelFactory(
            (application as CobraApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_list)

        setSupportActionBar(blocklist_tool_bar_id)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getBlockedNumber()

    }

    private fun getBlockedNumber() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getBlockedNumbers().collect { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        Log.d(TAG, "onCreate: ${response.data}")
                    }
                    Status.ERROR -> {
                        Log.d(TAG, "onCreate: ${response.message}")
                    }
                    Status.LOADING -> {
                        Log.d(TAG, "onCreate: Loading")
                    }
                    Status.UNAUTHORIZED -> {

                    }
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}