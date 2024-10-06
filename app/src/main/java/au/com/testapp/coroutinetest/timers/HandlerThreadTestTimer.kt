package au.com.testapp.coroutinetest.timers

import android.os.Handler
import android.os.HandlerThread
import timber.log.Timber

class HandlerThreadTestTimer(
    override val initialDelay: Long,
    override val repeatDelay: Long,
    override val tag: String
) : TestTimer {
    private val handlerThread = HandlerThread("TimerThread").apply { start() }
    private val handler = Handler(handlerThread.looper)
    private val runnable = object : Runnable {
        override fun run() {
            // Code to execute
            Timber.tag(tag).i("$tag - ticked after $repeatDelay")
            handler.postDelayed(this, repeatDelay)
        }
    }

    init {
        Timber.tag(tag).i("Starting $tag timer")
        handler.postDelayed(runnable, initialDelay) // Initial delay
    }
    override fun dispose() {
        Timber.tag(tag).i("Stopping $tag timer")
        handler.removeCallbacks(runnable)
    }
}