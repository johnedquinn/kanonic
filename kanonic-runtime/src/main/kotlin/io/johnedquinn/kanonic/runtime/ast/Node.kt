package io.johnedquinn.kanonic.runtime.ast

public abstract class Node(
    open val state: Int,
    open val children: List<Node>,
    open var parent: Node?,
    open var alias: String?
) {
    public abstract fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R
}
