package io.johnedquinn.kanonic.parse

import io.johnedquinn.kanonic.Grammar

public interface ParserMetadata {
    public val grammar: Grammar
    public fun getTable(): String
    public fun createRuleNode(index: Int, state: Int, children: List<Node>, parent: Node?): Node
}
