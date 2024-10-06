package au.com.testapp.coroutinetest.timers

import android.os.CountDownTimer
import timber.log.Timber
import kotlin.time.Duration.Companion.hours

class CountdownTimerTestTimer(
    override val initialDelay: Long,
    override val repeatDelay: Long,
    override val tag: String
) : TestTimer {
    private val timer: CountDownTimer =
        object : CountDownTimer(100.hours.inWholeMilliseconds, repeatDelay) {
            override fun onTick(millisUntilFinished: Long) {
                Timber.tag(tag).i("$tag - ticked after $repeatDelay")
            }

            override fun onFinish() {
                // Code to execute when the timer finishes
            }
        }

    init {
        Timber.tag(tag).i("Starting $tag timer")
        timer.start()
    }

    override fun dispose() {
        Timber.tag(tag).i("Stopping $tag timer")
        timer.cancel()
    }
}