package io.johnedquinn.kanonic.parse

public abstract class Node(
    val state: Int,
    val children: List<Node>,
    var parent: Node?
)
