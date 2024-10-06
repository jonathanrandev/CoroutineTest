package au.com.testapp.coroutinetest.timers

interface TestTimer {
    val initialDelay: Long
    val repeatDelay: Long
    val tag: String

    fun dispose()
}