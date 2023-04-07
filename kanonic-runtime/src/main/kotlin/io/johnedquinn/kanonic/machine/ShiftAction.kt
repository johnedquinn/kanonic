package io.johnedquinn.kanonic.machine

internal data class ShiftAction(val state: Int) : Action() {
    override fun toString(): String = "S$state"
}
