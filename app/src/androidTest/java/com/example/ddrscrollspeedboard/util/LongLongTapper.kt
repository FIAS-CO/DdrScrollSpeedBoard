package com.example.ddrscrollspeedboard.util

import android.view.ViewConfiguration
import androidx.test.espresso.UiController
import androidx.test.espresso.action.MotionEvents
import androidx.test.espresso.action.Tapper
import com.google.common.base.Preconditions

class LongLongTapper : Tapper {
    override fun sendTap(
        uiController: UiController, coordinates: FloatArray, precision: FloatArray
    ): Tapper.Status {
        return sendTap(uiController, coordinates, precision, 0, 0)
    }

    override fun sendTap(
        uiController: UiController,
        coordinates: FloatArray,
        precision: FloatArray,
        inputDevice: Int,
        buttonState: Int
    ): Tapper.Status {
        Preconditions.checkNotNull(uiController)
        Preconditions.checkNotNull(coordinates)
        Preconditions.checkNotNull(precision)
        val downEvent = MotionEvents.sendDown(
            uiController,
            coordinates,
            precision,
            inputDevice,
            buttonState
        ).down
        try {
            // Duration before a press turns into a long press.
            // Factor 1.5 is needed, otherwise a long press is not safely detected.
            // See android.test.TouchUtils longClickView
            val longPressTimeout = (ViewConfiguration.getLongPressTimeout() * 10f).toLong()
            uiController.loopMainThreadForAtLeast(longPressTimeout)
            if (!MotionEvents.sendUp(uiController, downEvent)) {
                MotionEvents.sendCancel(uiController, downEvent)
                return Tapper.Status.FAILURE
            }
        } finally {
            downEvent!!.recycle()
            // 既存の LongClick メソッドを Java から kotlin に変換したときに残ったコード。不要か判断できていない。
            // downEvent = null
        }
        return Tapper.Status.SUCCESS
    }
}
