package com.example.userapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.userapp.databinding.BottomSheetLayoutBinding
import com.example.userapp.model.UserData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddUserBottomSheet : BottomSheetDialogFragment() {

    // Interface to communicate with the hosting activity
    interface BottomSheetListener {
        fun onButtonClicked(data: UserData)
    }

    private var listener: BottomSheetListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Safely cast the hosting activity to the BottomSheetListener interface
        if (context is BottomSheetListener) {
            listener = context
        } else {
            throw ClassCastException("$context must implement BottomSheetListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: BottomSheetLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_layout, container, false)

        binding.buttonCreate.setOnClickListener {
            val name = binding.name.text.toString()
            val age = binding.age.text.toString()
            val city = binding.city.text.toString()
            // Notify the listener when the button is clicked
            listener?.onButtonClicked(UserData(name = name, age = age, city = city))
            dismiss() // Close the bottom sheet
        }

        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        listener = null // Prevent potential memory leaks
    }
}
