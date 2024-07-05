package com.example.bariumserver

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast

class SmsService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val sender = intent?.getStringExtra("sender")
        val message = intent?.getStringExtra("message")

        if (sender != null && message != null) {
            handleReceivedSms(sender, message)
        } else {
            Toast.makeText(this, "Sender or message is missing", Toast.LENGTH_SHORT).show()
        }

        return START_STICKY
    }

    private fun handleReceivedSms(sender: String, message: String) {
        // Process the received SMS here
        if (message.startsWith("ps123wd")) {
            Toast.makeText(this, "Received SMS from $sender: $message", Toast.LENGTH_SHORT).show()
            Log.d("SmsService", "Received SMS from $sender: $message")

            // Extract ID from the message
            val idRegex = "id: ([\\w-]+)".toRegex()
            val matchResult = idRegex.find(message)
            val messageId = matchResult?.groups?.get(1)?.value

            if (messageId != null) {
                // Send acknowledgment SMS back to sender with ID
                sendAckSms(sender, messageId)

                // Update the ViewModel
                SmsViewModelProvider.getInstance().addSmsDetails(sender, message)
            } else {
                Log.d("SmsService", "Message ID not found in the message.")
            }
        } else {
            Log.d("SmsService", "Message does not start with 'ps123wd'. Ignoring.")
        }
    }

    private fun sendAckSms(phoneNumber: String, messageId: String) {
        val message = "Acknowledgment:\n id: $messageId"
        val sentPI: PendingIntent = PendingIntent.getBroadcast(this, 0, Intent("SMS_SENT"),
            PendingIntent.FLAG_IMMUTABLE)
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, sentPI, null)
        Toast.makeText(this, "ACK SMS sent to $phoneNumber", Toast.LENGTH_SHORT).show()
        Log.d("SmsService", "ACK SMS sent to $phoneNumber")
    }
}
