package com.kamath.timetrackersdk

import android.util.Log


object TimeTracker{
    private val startTimes = mutableMapOf<String,Long>()
    private val durations = mutableMapOf<String,Long>()
    private const val FILETAG = "TIMETRACKER"
    var isEnabled: Boolean = true

    val logger:((String,String) -> Unit)? = {tag,message ->
        Log.d(tag,message)
    }

    fun start(tag:String){
        if (!isEnabled) return
        startTimes[tag] = System.currentTimeMillis()
        logger?.invoke(FILETAG,"Started time for $tag")
    }

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

    fun getDurations(tag: String):Long?{
        return if (isEnabled) durations[tag] else null
    }

    fun printAllDurations(){
        if (!isEnabled) return
        durations.forEach {(tag,duration) ->
            logger?.invoke(FILETAG, "$tag took $duration to complete")
        }
    }

    fun reset(){
        startTimes.clear()
        durations.clear()
        logger?.invoke(FILETAG,"All times are cleared")
    }

    inline fun <T> measure(tag:String,block:() -> T):T{
        start(tag)
        val result = block()
        stop(tag)
        return result
    }
}