package io.johnedquinn.kanonic.example

import io.johnedquinn.kanonic.example.ENode.ExprFallNode
import io.johnedquinn.kanonic.example.ENode.ExprPlusNode
import io.johnedquinn.kanonic.example.PNode.RootNode
import io.johnedquinn.kanonic.example.TNode.IdentNode
import io.johnedquinn.kanonic.example.TNode.IndexNode
import io.johnedquinn.kanonic.parse.CreateNode
import io.johnedquinn.kanonic.parse.Node
import io.johnedquinn.kanonic.parse.ParserInfo
import kotlin.Int
import kotlin.collections.List

public class G10Info : ParserInfo {
    private val nodeLambdaList: List<CreateNode> = initializeLambdas()

    private fun initializeLambdas(): List<CreateNode> = buildList {
        add(CreateNode { state, children, parent -> RootNode(state, children, parent) })
        add(CreateNode { state, children, parent -> ExprPlusNode(state, children, parent) })
        add(CreateNode { state, children, parent -> ExprFallNode(state, children, parent) })
        add(CreateNode { state, children, parent -> IndexNode(state, children, parent) })
        add(CreateNode { state, children, parent -> IdentNode(state, children, parent) })
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

internal sealed class PNode(
    state: Int,
    children: List<Node>,
    parent: Node?,
) : Node(state, children, parent) {
    internal class RootNode(
        state: Int,
        children: List<Node>,
        parent: Node?,
    ) : PNode(state, children, parent)
}

internal sealed class ENode(
    state: Int,
    children: List<Node>,
    parent: Node?,
) : Node(state, children, parent) {
    internal class ExprPlusNode(
        state: Int,
        children: List<Node>,
        parent: Node?,
    ) : ENode(state, children, parent)

    internal class ExprFallNode(
        state: Int,
        children: List<Node>,
        parent: Node?,
    ) : ENode(state, children, parent)
}

internal sealed class TNode(
    state: Int,
    children: List<Node>,
    parent: Node?,
) : Node(state, children, parent) {
    internal class IndexNode(
        state: Int,
        children: List<Node>,
        parent: Node?,
    ) : TNode(state, children, parent)

    internal class IdentNode(
        state: Int,
        children: List<Node>,
        parent: Node?,
    ) : TNode(state, children, parent)
}
