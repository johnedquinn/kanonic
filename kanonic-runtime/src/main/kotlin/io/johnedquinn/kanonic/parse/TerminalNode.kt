package io.johnedquinn.kanonic.parse

internal data class TerminalNode(
    override val state: Int,
    override var parent: Node?
) : Node(state, emptyList(), parent) {
    override fun toString(): String {
        return "${this::class.simpleName}(state: $state)"
    }
}
