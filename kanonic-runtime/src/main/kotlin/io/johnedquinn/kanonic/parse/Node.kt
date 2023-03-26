package io.johnedquinn.kanonic.parse

public abstract class Node(
    open val state: Int,
    open val children: List<Node>,
    open var parent: Node?
) {
    public abstract fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R
}
