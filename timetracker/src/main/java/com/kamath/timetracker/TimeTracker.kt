package com.kamath.timetracker

import android.util.Log
/**
 * TimeTracker is a lightweight utility for measuring execution time of code blocks or operations.
 * It can be used in both development and production environments.
 */
object TimeTracker{
    private val startTimes = mutableMapOf<String,Long>()
    private val durations = mutableMapOf<String,Long>()
    private const val FILETAG = "TIMETRACKER"

    /** Enable or disable tracking globally. */
    var isEnabled: Boolean = true

    /**
     * Optional custom logger.
     * Default: logs to Logcat with tag "TimeTracker"
     */
    val logger:((String,String) -> Unit)? = {tag,message ->
        Log.d(tag,message)
    }

    /** Starts tracking a tag */
    fun start(tag:String){
        if (!isEnabled) return
        startTimes[tag] = System.currentTimeMillis()
        logger?.invoke(FILETAG,"Started time for $tag")
    }

    /** Stops tracking a tag and calculates duration */
    fun stop(tag:String){
        if (!isEnabled) return
        val startTime = startTimes[tag]
        if (startTime!=null){
            val duration = System.currentTimeMillis() - startTime
            durations[tag] = duration
            logger?.invoke(FILETAG,"Stopped timer for $tag, duration :$duration ms")
        }
        else{
            logger?.invoke(FILETAG,"No timer initiated for this tag")
        }
    }

    /** Returns duration in milliseconds for a tag, or null if not available */
    fun getDurations(tag: String):Long?{
        return if (isEnabled) durations[tag] else null
    }

    /** Logs all recorded durations */
    fun printAllDurations(){
        if (!isEnabled) return
        durations.forEach {(tag,duration) ->
            logger?.invoke(FILETAG, "$tag took $duration to complete")
        }
    }

    /** Clears all start times and durations */
    fun reset(){
        startTimes.clear()
        durations.clear()
        logger?.invoke(FILETAG,"All times are cleared")
    }

    /**
     * Measures execution time of a code block automatically.
     * Usage:
     * TimeTracker.measure("load_data") {
     *     // Your logic
     * }
     */
    inline fun <T> measure(tag:String,block:() -> T):T{
        start(tag)
        val result = block()
        stop(tag)
        return result
    }
}