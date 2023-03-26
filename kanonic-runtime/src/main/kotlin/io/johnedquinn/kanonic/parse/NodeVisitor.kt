package io.johnedquinn.kanonic.parse

interface NodeVisitor<R, C> {
    public abstract fun visit(node: Node, ctx: C): R
    public abstract fun visitTerminal(node: TerminalNode, ctx: C): R
}
