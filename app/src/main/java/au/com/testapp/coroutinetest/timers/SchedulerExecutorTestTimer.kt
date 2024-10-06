package au.com.testapp.coroutinetest.timers

import timber.log.Timber
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class SchedulerExecutorTestTimer(
    override val initialDelay: Long,
    override val repeatDelay: Long,
    override val tag: String
) : TestTimer {
    private val scheduler: ScheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(1)
    private val task = Runnable { Timber.tag(tag).i("$tag - ticked after $repeatDelay") }


    init {
        Timber.tag(tag).i("Starting $tag timer")
        scheduler.scheduleWithFixedDelay(task, initialDelay, repeatDelay, TimeUnit.MILLISECONDS)

    }

    override fun dispose() {
        Timber.tag(tag).i("Stopping $tag timer")
        scheduler.shutdown()
    }
}