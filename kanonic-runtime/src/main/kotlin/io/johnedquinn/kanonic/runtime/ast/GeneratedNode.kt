package io.johnedquinn.kanonic.runtime.ast

data class GeneratedNode(
    override val state: Int,
    override val children: List<Node>,
    override var parent: Node?,
    override var alias: String?
) : Node(state, children, parent, alias) {
    override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R {
        error("This shouldn't have happened")
    }
}
