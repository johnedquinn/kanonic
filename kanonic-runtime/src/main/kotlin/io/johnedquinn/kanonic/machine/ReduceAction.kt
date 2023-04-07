package io.johnedquinn.kanonic.machine

internal data class ReduceAction(val rule: Int) : Action() {
    override fun toString(): String = "R$rule"
}
