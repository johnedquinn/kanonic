package io.johnedquinn.kanonic.runtime.parse

import io.johnedquinn.kanonic.runtime.ast.Node

public fun interface NodeCreator {
    abstract fun create(state: Int, children: List<Node>, parent: Node?): Node
}
