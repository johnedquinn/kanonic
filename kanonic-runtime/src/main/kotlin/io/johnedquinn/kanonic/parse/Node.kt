package io.johnedquinn.kanonic.parse

public abstract class Node(
    open val state: Int,
    open val children: List<Node>,
    open var parent: Node?
)
