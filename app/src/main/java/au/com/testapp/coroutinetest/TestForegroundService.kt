package au.com.testapp.coroutinetest

import android.app.ForegroundServiceStartNotAllowedException
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import au.com.testapp.coroutinetest.coroutines.CoroutineWorker
import au.com.testapp.coroutinetest.sensors.SensorReader
import au.com.testapp.coroutinetest.timers.AlarmManagerTestTimer
import au.com.testapp.coroutinetest.timers.AndroidTestTimer
import au.com.testapp.coroutinetest.timers.CountdownTimerTestTimer
import au.com.testapp.coroutinetest.timers.HandlerRunnableTestTimer
import au.com.testapp.coroutinetest.timers.HandlerThreadTestTimer
import au.com.testapp.coroutinetest.timers.SchedulerExecutorTestTimer
import au.com.testapp.coroutinetest.timers.TestTimer
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import kotlin.time.Duration.Companion.minutes


class TestForegroundService : LifecycleService() {

    companion object {
        private const val CHANNEL_ID = "TestCoroutingChannelId"
    }


    //Coroutine Workers
    private val ioWorkers: List<CoroutineWorker> = emptyList()
    private val mainWorkers: List<CoroutineWorker> = emptyList()
    private val defaultWorkers: List<CoroutineWorker> = emptyList()

    //Sensors
    private var sensorReader: SensorReader? = null

    //Timers
    private val alarmManagerTestTimers: List<TestTimer> = emptyList()
    private val countDownTestTimers: List<TestTimer> = emptyList()
    private val handlerRunnableTestTimers: List<TestTimer> = emptyList()
    private val handlerThreadTestTimers: List<TestTimer> = emptyList()
    private val schedulerExecutorTestTimers: List<TestTimer> = emptyList()
    private val androidTestTimers: List<TestTimer> = emptyList()


    override fun onCreate() {
        super.onCreate()
        Timber.i("Starting foreground service")
        try {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Coroutine Test Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Channel for foreground service"

            val notificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setContentTitle("Coroutine Test")
                .setContentText("This is a test to run coroutines")
                // Create the notification to display while the service is running
                .build()
            ServiceCompat.startForeground(
                /* service = */ this,
                /* id = */ 100, // Cannot be 0
                /* notification = */ notification,
                /* foregroundServiceType = */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                } else {
                    0
                },
            )
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
                Timber.tag("MainService").e("Can't start foreground service, not allowed")
                // App not in a valid state to start foreground service
                // (e.g. started from bg)
            }
            // ...
        }

        initWorkers()
        initSensorReader()
        initTimers(context = this)
    }

    override fun onDestroy() {
        Timber.i("TestForegroundService destroyed")
        super.onDestroy()
        for (worker in ioWorkers) {
            worker.cancelJob()
        }

        for (worker in mainWorkers) {
            worker.cancelJob()
        }

        for (worker in defaultWorkers) {
            worker.cancelJob()
        }

        sensorReader?.dispose()
        sensorReader = null
    }

    private fun initWorkers() {
        for (i in 1..250) {
            val coroutineWorker = CoroutineWorker(i * 1000L, "io-worker-$i", Dispatchers.IO)
            ioWorkers.plus(coroutineWorker)
        }

        for (i in 1..250) {
            val coroutineWorker = CoroutineWorker(i * 1000L, "default-worker-$i", Dispatchers.IO)
            defaultWorkers.plus(coroutineWorker)
        }

        for (i in 1..250) {
            val coroutineWorker = CoroutineWorker(i * 1000L, "main-worker-$i", Dispatchers.IO)
            mainWorkers.plus(coroutineWorker)
        }
    }

    private fun initSensorReader() {
        if (sensorReader == null) {
            sensorReader = SensorReader(this)
        }
    }


    private fun initTimers(
        timersInParallelCount: Int = 3,
        timeMultiplier: Long = 2000L,
        context: Context
    ) {
        for (i in 1..timersInParallelCount) {
            //Alarm managers minimum time is 10 minutes
            val testTimer = AlarmManagerTestTimer(
                context = context,
                initialDelay = 1_000L,
                repeatDelay = i.minutes.inWholeMilliseconds * 10,
                tag = "AlarmManagerTestTimer-$i"
            )
            alarmManagerTestTimers.plus(testTimer)
        }

        for (i in 1..timersInParallelCount) {
            val testTimer = CountdownTimerTestTimer(
                initialDelay = 1_000L,
                repeatDelay = i * timeMultiplier,
                tag = "CountdownTimerTestTimer-$i"
            )
            countDownTestTimers.plus(testTimer)
        }

        for (i in 1..timersInParallelCount) {
            val testTimer = HandlerRunnableTestTimer(
                initialDelay = 1_000L,
                repeatDelay = i * timeMultiplier,
                tag = "HandlerRunnableTestTimer-$i"
            )
            handlerRunnableTestTimers.plus(testTimer)
        }

        for (i in 1..timersInParallelCount) {
            val testTimer = HandlerThreadTestTimer(
                initialDelay = 1_000L,
                repeatDelay = i * timeMultiplier,
                tag = "HandlerThreadTestTimer-$i"
            )
            handlerThreadTestTimers.plus(testTimer)
        }

        for (i in 1..timersInParallelCount) {
            val testTimer = SchedulerExecutorTestTimer(
                initialDelay = 1_000L,
                repeatDelay = i * timeMultiplier,
                tag = "SchedulerExecutorTestTimer-$i"
            )
            schedulerExecutorTestTimers.plus(testTimer)
        }

        for (i in 1..timersInParallelCount) {
            val testTimer = AndroidTestTimer(
                initialDelay = 1_000L,
                repeatDelay = i * timeMultiplier,
                tag = "AndroidTestTimer-$i"
            )
            androidTestTimers.plus(testTimer)
        }
    }
}