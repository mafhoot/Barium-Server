package com.example.bariumserver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("SmsReceiver", "onReceive called")
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            Log.d("SmsReceiver", "SMS received")
            val bundle = intent.extras
            if (bundle != null) {
                try {
                    val pdus = bundle.get("pdus") as Array<*>
                    val messages: Array<SmsMessage?> = arrayOfNulls(pdus.size)
                    for (i in pdus.indices) {
                        messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        val sender = messages[i]?.originatingAddress
                        val messageBody = messages[i]?.messageBody
                        if (sender != null && messageBody != null) {
                            Log.d("SmsReceiver", "Message from $sender: $messageBody")
                            // Start the service to send an acknowledgment
                            val serviceIntent = Intent(context, SmsService::class.java)
                            serviceIntent.putExtra("sender", sender)
                            serviceIntent.putExtra("message", messageBody)
                            context.startService(serviceIntent)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SmsReceiver", "Exception in onReceive: ${e.message}")
                }
            }
        }
    }
}
