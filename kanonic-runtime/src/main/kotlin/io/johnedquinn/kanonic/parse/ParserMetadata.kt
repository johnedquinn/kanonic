package io.johnedquinn.kanonic.parse

public interface ParserMetadata {
    public fun createRuleNode(index: Int, state: Int, children: List<Node>, parent: Node?): Node
}
