package com.example.bariumserver

import androidx.lifecycle.ViewModelProvider

object SmsViewModelProvider {
    private lateinit var viewModel: SmsViewModel

    fun initialize(viewModel: SmsViewModel) {
        this.viewModel = viewModel
    }

    fun getInstance(): SmsViewModel {
        return viewModel
    }
}
