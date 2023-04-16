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
        INFO {
            override fun getJavaLogLevel(): Level = Level.INFO
        },
        WARNING {
            override fun getJavaLogLevel(): Level = Level.WARNING
        },
        SEVERE {
            override fun getJavaLogLevel(): Level = Level.SEVERE
        },
        ALL {
            override fun getJavaLogLevel(): Level = Level.ALL
        };

        abstract fun getJavaLogLevel(): Level
    }
}
