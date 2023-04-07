package io.johnedquinn.kanonic.runtime.utils

public object Logger {

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
