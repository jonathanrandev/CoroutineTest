package au.com.testapp.coroutinetest

import android.app.Application
import au.com.testapp.coroutinetest.logging.FileLoggingTree
import timber.log.Timber

class CoroutineTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.plant(FileLoggingTree(context = applicationContext))
    }
}