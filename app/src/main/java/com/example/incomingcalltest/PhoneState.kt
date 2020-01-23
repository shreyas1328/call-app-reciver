package com.example.incomingcalltest

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class PhoneState(private var context: Context) : PhoneStateListener() {

    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var previousState: String? = null
    private var isCall: Boolean = false

    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        Log.d("state_123", "revcerhiiiii:  $state")
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
//                Toast.makeText(context, "ring", Toast.LENGTH_SHORT).show()
                Log.d("state_123", "state: RINGING" + "   " + getIsCall(context))
                if (!getIsCall(context)) {
                    setFalshScreen()
                }
                setIsCall(context, true)
                previousState = "ring"
            }

            TelephonyManager.CALL_STATE_OFFHOOK -> {
//                Toast.makeText(context, "offhook", Toast.LENGTH_SHORT).show()
                Log.d("state_123", "state: OFFHOOK")
                if (previousState.equals("ring")) {
                    setlimit()
                }
                previousState = "offHook"
            }

            TelephonyManager.CALL_STATE_IDLE -> {
//                Toast.makeText(context, "idle", Toast.LENGTH_SHORT).show()
                Log.d("state_123", "state: IDLE")
                if (previousState.equals("offHook")) {
                    releaseThread()
                }
                setIsCall(context, false)
                previousState = null
            }
        }
    }

    private fun releaseThread() {
        try{
            handler!!.removeCallbacks(runnable!!)
        }catch (e:Exception) {
            Log.d("thread_test", "test:   "+e.message)
        }
    }

    private fun setFalshScreen() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val ll = LinearLayout(context.applicationContext)
        ll.setBackgroundColor(Color.WHITE)
        ll.orientation = LinearLayout.HORIZONTAL
        val myView = inflater.inflate(R.layout.flash_window, ll,false)
        setData(myView, wm)
        if (Build.VERSION.SDK_INT >= O) {

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
            params.gravity = Gravity.CENTER or Gravity.CENTER
            wm.addView(myView, params)

        } else {
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                PixelFormat.TRANSLUCENT
            )
            params.gravity = Gravity.CENTER or Gravity.CENTER
            wm.addView(myView, params)
        }
    }

    private fun setData(myView: View?, wm: WindowManager) {
        var title = myView?.findViewById<TextView>(R.id.tv_title)
        var des = myView?.findViewById<TextView>(R.id.tv_des)
        var mBtnOk = myView?.findViewById<TextView>(R.id.btn_ok)

        des?.setText("Hi this is flash message")

        mBtnOk?.setOnClickListener(View.OnClickListener {
            wm.removeView(myView)
        })



    }

    private fun setlimit() {
        handler = Handler()
        runnable = Runnable {
            setActions()
            handler!!.postDelayed(runnable!!, 5000)
        }
        handler!!.postDelayed(runnable!!, 5000)

    }

    private fun setActions() {
        var vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= O) {
            vibrate.vibrate(VibrationEffect.createOneShot(500, 1))
        } else {
            vibrate.vibrate(500)
        }
    }
}