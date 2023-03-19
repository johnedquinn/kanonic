package io.johnedquinn.tests.simple

import io.johnedquinn.kanonic.parse.CreateNode
import io.johnedquinn.kanonic.parse.Node
import io.johnedquinn.kanonic.parse.ParserMetadata
import kotlin.Int
import kotlin.collections.List

public class SimpleMetadata : ParserMetadata {
    private val nodeLambdaList: List<CreateNode> = initializeLambdas()

    private fun initializeLambdas(): List<CreateNode> = buildList {
        add(CreateNode { state, children, parent -> SimpleNode.pNode.RootNode(state, children, parent) })
        add(CreateNode { state, children, parent -> SimpleNode.eNode.ExprPlusNode(state, children, parent) })
        add(CreateNode { state, children, parent -> SimpleNode.eNode.ExprFallNode(state, children, parent) })
        add(CreateNode { state, children, parent -> SimpleNode.tNode.IndexNode(state, children, parent) })
        add(CreateNode { state, children, parent -> SimpleNode.tNode.IdentNode(state, children, parent) })
    }

    public override fun createRuleNode(
        index: Int,
        state: Int,
        children: List<Node>,
        parent: Node?,
    ): Node {
        val nodeCreator = nodeLambdaList[index]
        return nodeCreator.create(state, children, parent)
    }
}
