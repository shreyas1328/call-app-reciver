package com.example.incomingcalltest

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

private var PREF_NAME: String = "MYAPP"
private var PREF_MODE: Int = 0

private var ISCALL: String = "ISCALL"

fun setIsCall(context: Context, isCall: Boolean) {
    var prefs = context.getSharedPreferences(PREF_NAME, PREF_MODE)
    var editor = prefs.edit()
    editor.putBoolean(ISCALL, isCall)
    editor.apply()
}

fun getIsCall(context: Context): Boolean {
    var prefs = context.getSharedPreferences(PREF_NAME, PREF_MODE)
    return prefs.getBoolean(ISCALL, false)
}

fun enableReciver(context: Context) {
    val receiver = ComponentName(context, PhoneCallRevicer::class.java)

    context.packageManager.setComponentEnabledSetting(
        receiver,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )
}

fun disableReciver(context: Context) {
    val receiver = ComponentName(context, PhoneCallRevicer::class.java)

    context.packageManager.setComponentEnabledSetting(
        receiver,
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
        PackageManager.DONT_KILL_APP
    )
}