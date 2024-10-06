package au.com.testapp.coroutinetest.timers

import android.os.Handler
import android.os.Looper
import timber.log.Timber

class HandlerRunnableTestTimer(
    override val initialDelay: Long,
    override val repeatDelay: Long,
    override val tag: String
) : TestTimer {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            // Code to execute
            Timber.tag(tag).i("$tag - ticked after $repeatDelay")
            handler.postDelayed(this, repeatDelay) // 1 second delay
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