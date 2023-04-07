package io.johnedquinn.kanonic.machine

data class AcceptAction(val type: Int) : Action() {
    override fun toString(): String = "A$type"
}
