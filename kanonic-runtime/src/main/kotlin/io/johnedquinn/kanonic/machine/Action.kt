package io.johnedquinn.kanonic.machine

sealed class Action {
    internal fun prettify() = when (this) {
        is ShiftAction -> "S${this.state}"
        is ReduceAction -> "R${this.rule}"
        is AcceptAction -> "ACCEPT"
//        else -> error("None found")
    }
}
