package com.example.incomingcalltest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log

class PhoneCallRevicer : BroadcastReceiver() {

    private var  tm:TelephonyManager? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("state_123", "revcer")
        val phoneState  = PhoneState(context!!)
        tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        tm!!.listen(phoneState, PhoneStateListener.LISTEN_CALL_STATE)
    }

}