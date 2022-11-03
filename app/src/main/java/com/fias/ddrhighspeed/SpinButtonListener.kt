package com.fias.ddrhighspeed

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * func にカウントアップ/ダウンを入れる想定
 */
class SpinButtonListener(private val func: () -> Unit) : View.OnClickListener,
    View.OnLongClickListener, View.OnTouchListener {
    private val handler: Handler = Handler(Looper.getMainLooper())

    private var runnable = object : Runnable {
        override fun run() {
            func()
            handler.postDelayed(this, 100)
        }
    }

    override fun onClick(_view: View) {
        func()
    }

    override fun onLongClick(_view: View): Boolean {
        func()
        handler.postDelayed(runnable, 500)

        return true       // trueはonClick処理しない。falseでonClick処理を続けて実行。
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(_view: View, motEvent: MotionEvent): Boolean {
        when (motEvent.action) {
            MotionEvent.ACTION_UP -> {      // 離す
                // Runnable解除
                handler.removeCallbacks(runnable)
            }
            else -> {
                Log.d("app5/onTouch", motEvent.action.toString())
            }
        }

        return false       // trueでコールバック処理終了。falseはコールバック処理を継続。
    }
}