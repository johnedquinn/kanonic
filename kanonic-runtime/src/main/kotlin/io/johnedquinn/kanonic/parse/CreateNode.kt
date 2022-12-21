package io.johnedquinn.kanonic.parse

public fun interface CreateNode {
    abstract fun create(state: Int, children: List<Node>, parent: Node?): Node
}
