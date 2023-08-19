package com.example.userapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.lifecycleScope
import com.example.userapp.base.BaseActivity
import com.example.userapp.databinding.ActivityLoginBinding
import com.example.userapp.util.showToast
import com.example.userapp.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    // Using the "by viewModel()" delegate to inject the ViewModel instance
    private val viewModel by viewModel<AuthenticationViewModel>()

    private lateinit var sharedPreferences: SharedPreferences

    override fun setupViews() {
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        // Using dataBinding with explicit binding lifecycle owner
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel
        val isLogged = sharedPreferences.getBoolean("isLogged", false)
        if (isLogged) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        // Set UI text values using resources directly
        dataBinding.loginButton.text = getString(R.string.login)
        dataBinding.loginText.text = getString(R.string.login)
        dataBinding.tvRedirectUp.text = getString(R.string.register_label)
    }


    override fun getLayoutResource(): Int = R.layout.activity_login

    override fun initObservers() {
        // Collect loginSuccess event using Kotlin's Flow
        lifecycleScope.launch {
            viewModel.loginSuccess.observe(this@LoginActivity) { success ->
                if (success) {
                    finish()
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putBoolean("isLogged", true)
                    editor.apply()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }
            }
        }

        // Collect navigateToScreen event using Kotlin's Flow
        lifecycleScope.launch {
            viewModel.navigateToScreen.observe(this@LoginActivity) { navigate ->
                if (navigate && !viewModel.isRegisterActivity) {
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                }
            }
        }

        // Observe errors using LiveData
        viewModel.errorChannel.observe(this) { error ->
            showToast(error)
        }
    }
}
