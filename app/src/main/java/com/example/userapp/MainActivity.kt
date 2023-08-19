package com.example.userapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.example.userapp.adapter.UserAdapter
import com.example.userapp.base.BaseActivity
import com.example.userapp.databinding.ActivityMainBinding
import com.example.userapp.model.UserData
import com.example.userapp.util.SortOption
import com.example.userapp.util.showToast
import com.example.userapp.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(), AddUserBottomSheet.BottomSheetListener {

    private val viewModel by viewModel<MainViewModel>()
    private val adapter = UserAdapter()
    private lateinit var sharedPreferences: SharedPreferences


    override fun setupViews() {
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        dataBinding.viewModel = viewModel
        setSupportActionBar(dataBinding.toolbar)
        setupRecyclerView()
    }

    override fun getLayoutResource(): Int = R.layout.activity_main

    private fun setupRecyclerView() {
        dataBinding.recyclerView.adapter = adapter
    }

    override fun initObservers() {
        observeBottomSheetOpening()
        observeUserData()
        observeMessageChannel()
    }

    private fun observeBottomSheetOpening() {
        lifecycleScope.launch {
            viewModel.openBottomSheet.observe(this@MainActivity) { shouldOpen ->
                if (shouldOpen) {
                    val bottomSheetFragment = AddUserBottomSheet()
                    bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                }
            }
        }
    }

    private fun observeUserData() {
        lifecycleScope.launch {
            viewModel.userData.observe(this@MainActivity) { userData ->
                adapter.submitData(userData)
            }
        }
    }

    private fun observeMessageChannel() {
        lifecycleScope.launch {
            viewModel.messageChannel.observe(this@MainActivity) { message ->
                showToast(message)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onButtonClicked(data: UserData) {
        viewModel.addUserData(data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_name -> {
                viewModel.filterAndSortData("", SortOption.NAME)
                true
            }
            R.id.action_sort_age -> {
                viewModel.filterAndSortData("", SortOption.AGE)
                true
            }
            R.id.action_sort_city -> {
                viewModel.filterAndSortData("", SortOption.CITY)
                true
            }
            R.id.action_logout->{
                sharedPreferences.edit().remove("isLogged").apply()
                finish()
                startActivity(Intent(this@MainActivity,LoginActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
