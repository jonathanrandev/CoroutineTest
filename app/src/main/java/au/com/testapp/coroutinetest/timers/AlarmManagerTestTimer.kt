package au.com.testapp.coroutinetest.timers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import timber.log.Timber

class AlarmManagerTestTimer(
    context: Context,
    override val initialDelay: Long,
    override val repeatDelay: Long,
    override val tag: String
) : TestTimer {

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val intent = Intent(context, TimerBroadCastReceiver()::class.java)
    private val pendingIntent =
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    init {
        Timber.tag(tag).i("Starting $tag timer")
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(),
            repeatDelay,
            pendingIntent
        )
    }


    override fun dispose() {
        Timber.tag(tag).i("Stopping $tag timer")
        alarmManager.cancel(pendingIntent)
    }

    inner class TimerBroadCastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Timber.tag(tag).i("$tag - ticked after $repeatDelay")
        }

    }
}