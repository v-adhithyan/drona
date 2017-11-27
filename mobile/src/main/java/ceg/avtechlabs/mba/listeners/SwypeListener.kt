package ceg.avtechlabs.mba.listeners

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.logging.Logger

/**
 * Created by Adhithyan V on 18-11-2017.
 */

open class SwypeListener(context: Context) : View.OnTouchListener {
   companion object {
        val SWIPE_THRESHOLD = 100
        val SWIPE_VELOCITY_THRESHOLD = 100
        var gestureDetecor: GestureDetector? = null
    }

    init {
        gestureDetecor = GestureDetector(context, GestureListener())
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if(gestureDetecor != null)
            return gestureDetecor!!.onTouchEvent(event)

        return false
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        protected var mLastOnDownEvent: MotionEvent? = null

        override fun onDown(e: MotionEvent): Boolean {
            mLastOnDownEvent = e
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result = false

            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                //exception.printStackTrace()
                //ceg.avtechlabs.mba.util.Logger.out("hello ${exception.message!!}")
                //ceg.avtechlabs.mba.util.Logger.out(exception.message!!)
            }

            return result
        }

    }

    open fun onSwipeRight() {}
    open fun onSwipeLeft() {}
    open fun onSwipeTop() {}
    open fun onSwipeBottom() {}
}