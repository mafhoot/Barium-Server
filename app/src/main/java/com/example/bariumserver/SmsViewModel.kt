package com.example.bariumserver

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SmsViewModel : ViewModel() {

    private val _smsDetailsList = MutableLiveData<List<String>>(emptyList())
    val smsDetailsList: LiveData<List<String>> = _smsDetailsList

    fun addSmsDetails(sender: String, message: String) {
        val newEntry = "New request\nPhone number: $sender\nMessage: $message\nACK Sent"
        val updatedList = _smsDetailsList.value.orEmpty().toMutableList()
        updatedList.add(newEntry)
        _smsDetailsList.value = updatedList
    }
}
