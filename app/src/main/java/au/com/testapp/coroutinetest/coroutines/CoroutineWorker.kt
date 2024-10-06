package au.com.testapp.coroutinetest.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class CoroutineWorker(
    private val delay: Long,
    private val tag: String,
    private val dispatcher: CoroutineDispatcher
) : CoroutineScope {

    private val job = SupervisorJob()

    // Overriding the coroutineContext to provide a default context with Dispatchers.Main and the job
    override val coroutineContext = dispatcher + job

    // Function to start a coroutine on Dispatchers.IO
    init {
        launch {
            Timber.tag(tag).i("Starting hard work on ${Thread.currentThread().name}")
            doHardWork()
            Timber.tag(tag).i("Finished hard work on ${Thread.currentThread().name}")
        }
    }

    // Simulate a function that performs hard work
    private suspend fun doHardWork() {
        while (true) {
            for(i in 1..100000) {
                test()
            }
            delay(delay)
            Timber.tag(tag).i("$tag: Checking in after $delay")
        }
    }

    // Call this method to clean up resources when you're done
    fun cancelJob() {
        job.cancel()
    }

    private fun test() {

    }
}