package io.johnedquinn.kanonic.parse

public data class TerminalNode(
    override val state: Int,
    override var parent: Node?,
    val token: TokenLiteral
) : Node(state, emptyList(), parent) {
    override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R {
        return visitor.visitTerminal(this, ctx)
    }

    override fun toString(): String {
        return "${this::class.simpleName}(state: $state, token: $token)"
    }
}
