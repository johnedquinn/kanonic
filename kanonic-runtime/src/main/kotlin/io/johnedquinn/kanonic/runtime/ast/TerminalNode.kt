package io.johnedquinn.kanonic.runtime.ast

import io.johnedquinn.kanonic.runtime.parse.TokenLiteral

public data class TerminalNode(
    override val state: Int,
    override var parent: Node?,
    val token: TokenLiteral,
    override var alias: String?
) : Node(state, emptyList(), parent, alias) {
    override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R {
        return visitor.visitTerminal(this, ctx)
    }

    override fun toString(): String {
        return "${this::class.simpleName}(state: $state, token: $token)"
    }
}
