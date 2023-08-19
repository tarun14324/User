package com.example.userapp.base

import android.content.ContentValues.TAG
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity <Binding : ViewDataBinding> : AppCompatActivity() {

    lateinit var dataBinding: Binding

    abstract fun setupViews()

    @LayoutRes
    abstract fun getLayoutResource(): Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            dataBinding = DataBindingUtil.setContentView(this, getLayoutResource())
        } catch (e: Exception) {
            throw RuntimeException(TAG, e)
        }
        dataBinding.lifecycleOwner = this
        setupViews()
        initObservers()
    }


    protected abstract fun initObservers()
}