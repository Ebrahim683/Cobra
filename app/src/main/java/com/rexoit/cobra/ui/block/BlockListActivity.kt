package com.rexoit.cobra.ui.block

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rexoit.cobra.CobraApplication
import com.rexoit.cobra.CobraViewModelFactory
import com.rexoit.cobra.R
import com.rexoit.cobra.ui.block.adapter.BlockListAdapter
import com.rexoit.cobra.ui.main.viewmodel.MainViewModel
import com.rexoit.cobra.utils.SharedPrefUtil
import com.rexoit.cobra.utils.Status
import kotlinx.android.synthetic.main.activity_block_list.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

private const val TAG = "blockListActivity"

class BlockListActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        CobraViewModelFactory(
            (application as CobraApplication).repository
        )
    }

    private lateinit var blockListAdapter: BlockListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_list)

        setSupportActionBar(blocklist_tool_bar_id)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val accessToken = SharedPrefUtil(this).getUserToken()

        blockListAdapter = BlockListAdapter()

        blocklist_rec_id.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@BlockListActivity)
            adapter = blockListAdapter
        }

        blocked_list_swipe_to_refresh.setOnRefreshListener {
            if (accessToken != null) {
                getBlockedList(accessToken)
            }
        }

        if (accessToken != null) {
            blocked_list_swipe_to_refresh.isRefreshing = true
            getBlockedList(accessToken)
        }
    }

    private fun getBlockedList(token: String) {
        runBlocking {
            viewModel.getBlockedNumbers(token).collect { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        response.data?.numbers?.let { numbers ->
                            blockListAdapter.submitList(numbers)
                            blockListAdapter.notifyDataSetChanged()
                        }
                        blocked_list_swipe_to_refresh.isRefreshing = false
                    }
                    Status.ERROR -> {
                        blocked_list_swipe_to_refresh.isRefreshing = false
                    }
                    Status.LOADING -> {
                        blocked_list_swipe_to_refresh.isRefreshing = true
                    }
                    Status.UNAUTHORIZED -> {
                        blocked_list_swipe_to_refresh.isRefreshing = false
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