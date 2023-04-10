package io.johnedquinn.kanonic.runtime.parse

import io.johnedquinn.kanonic.runtime.ast.Node

public fun interface NodeCreator {
    // TODO: Potentially make alias an integer
    abstract fun create(state: Int, children: List<Node>, parent: Node?, alias: String?): Node
}
