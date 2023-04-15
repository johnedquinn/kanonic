package io.johnedquinn.kanonic.runtime.utils

import java.util.logging.Level
import java.util.logging.Logger

public object KanonicLogger {

    private const val NAME: String = "KanonicLogger"
    public fun getLogger(): Logger = Logger.getLogger(NAME)

    public enum class LogLevel {
        OFF {
            override fun getJavaLogLevel(): Level = Level.OFF
        },
        FINE {
            override fun getJavaLogLevel(): Level = Level.FINE
        },
        SEVERE {
            override fun getJavaLogLevel(): Level = Level.SEVERE
        },
        ALL {
            override fun getJavaLogLevel(): Level = Level.ALL
        };
        abstract fun getJavaLogLevel(): Level
    }

    public var tolerance: Tolerance = Tolerance.ERROR

    enum class Tolerance {
        ERROR,
        DEBUG;
    }

    public fun log(str: String, level: Tolerance = Tolerance.ERROR) {
        if (level <= tolerance) {
            println(str)
        }
    }

    public fun debug(str: String) {
        log(str, Tolerance.DEBUG)
    }

    public fun error(str: String) {
        log(str, Tolerance.ERROR)
    }
}
