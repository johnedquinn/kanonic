package io.johnedquinn.kanonic.parse

internal sealed class Node(
    open val state: Int,
    open val children: List<Node>,
    open var parent: Node?
)
