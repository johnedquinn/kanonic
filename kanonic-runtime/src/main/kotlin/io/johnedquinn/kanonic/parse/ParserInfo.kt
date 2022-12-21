package io.johnedquinn.kanonic.parse

public interface ParserInfo {
    public fun createRuleNode(index: Int, state: Int, children: List<Node>, parent: Node?): Node
}
