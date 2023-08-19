package com.example.userapp.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

object StringUtil {
    fun isStrongPassword(password: String): Boolean {
        val pattern = Regex(
            "^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{7,}\$"
        )

        return pattern.matches(password)
    }
}

@BindingAdapter("errorText")
fun TextInputLayout.setErrorText(errorMessage: String?) {
    error = errorMessage
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun Context.showToast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
