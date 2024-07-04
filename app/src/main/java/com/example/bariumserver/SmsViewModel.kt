package com.example.bariumserver

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SmsViewModel : ViewModel() {

    private val _smsDetails = MutableLiveData<String>()
    val smsDetails: LiveData<String> = _smsDetails

    fun updateSmsDetails(sender: String, message: String) {
        _smsDetails.value = "New request\nPhone number: $sender\nMessage: $message\nACK Sent"
    }
}
