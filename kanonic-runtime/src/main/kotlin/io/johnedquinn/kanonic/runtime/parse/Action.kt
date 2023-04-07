package io.johnedquinn.kanonic.runtime.parse

sealed class Action {
    internal fun prettify() = when (this) {
        is Shift -> "S${this.state}"
        is Reduce -> "R${this.rule}"
        is Accept -> "ACCEPT"
    }

    public data class Shift(val state: Int) : Action()

    public data class Accept(val type: Int) : Action()

    public data class Reduce(val rule: Int) : Action()
}
