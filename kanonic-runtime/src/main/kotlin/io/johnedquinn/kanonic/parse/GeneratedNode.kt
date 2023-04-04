package io.johnedquinn.kanonic.parse

data class GeneratedNode(
    override val state: Int,
    override val children: List<Node>,
    override var parent: Node?
) : Node(state, children, parent) {
    override fun <R, C> accept(visitor: NodeVisitor<R, C>, ctx: C): R {
        error("This shouldn't have happened")
    }
}
