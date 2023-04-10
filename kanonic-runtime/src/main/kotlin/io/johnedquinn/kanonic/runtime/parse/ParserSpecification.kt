package io.johnedquinn.kanonic.runtime.parse

import io.johnedquinn.kanonic.runtime.ast.Node
import io.johnedquinn.kanonic.runtime.grammar.Grammar

public interface ParserSpecification {
    public val grammar: Grammar
    public fun getTable(): String
    public fun createRuleNode(index: Int, state: Int, children: List<Node>, parent: Node?, alias: String?): Node
}
