package au.com.testapp.coroutinetest.timers

import timber.log.Timber
import java.util.Timer
import java.util.TimerTask

class AndroidTestTimer(
    override val initialDelay: Long,
    override val repeatDelay: Long,
    override val tag: String
) : TestTimer {
    private val timer: Timer = Timer()
    private val timerTask: TimerTask = object : TimerTask() {
        override fun run() {
            Timber.tag(tag).i("$tag - ticked after $repeatDelay")
        }
    }


    init {
        Timber.tag(tag).i("Starting $tag timer")
        timer.schedule(timerTask, initialDelay, repeatDelay)
    }

    override fun dispose() {
        Timber.tag(tag).i("Stopping $tag timer")
        timer.cancel()
    }
}