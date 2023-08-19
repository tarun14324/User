package com.example.userapp

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.example.userapp.base.BaseActivity
import com.example.userapp.databinding.ActivityLoginBinding
import com.example.userapp.util.showToast
import com.example.userapp.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : BaseActivity<ActivityLoginBinding>() {
    private val viewModel by viewModel<AuthenticationViewModel>()

    override fun setupViews() {
        dataBinding.viewModel = viewModel
        viewModel.isRegisterActivity = true
        dataBinding.loginButton.text = getString(R.string.register)
        dataBinding.loginText.text = getString(R.string.register)
        dataBinding.tvRedirectUp.text = getString(R.string.login_label)
    }

    override fun getLayoutResource(): Int = R.layout.activity_login

    override fun initObservers() {
        lifecycleScope.launch {
            viewModel.loginSuccess.observe(this@RegisterActivity) { success ->
                if (success) {
                    finish()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                }
            }
        }

        lifecycleScope.launch {
            viewModel.navigateToScreen.observe(this@RegisterActivity){ navigate ->
                if (navigate && viewModel.isRegisterActivity) {
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                }
            }
        }

        lifecycleScope.launch {
            viewModel.errorChannel.observe(this@RegisterActivity) { errorMessage ->
                showToast(errorMessage)
            }
        }
    }
}
