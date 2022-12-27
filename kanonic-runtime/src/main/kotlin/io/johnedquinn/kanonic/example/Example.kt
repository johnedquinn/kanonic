package io.johnedquinn.kanonic.example

import io.johnedquinn.kanonic.parse.CreateNode
import io.johnedquinn.kanonic.parse.Node
import io.johnedquinn.kanonic.parse.ParserInfo
import kotlin.Int
import kotlin.collections.List

public class G10Info : ParserInfo {
    private val nodeLambdaList: List<CreateNode> = initializeLambdas()

    private fun initializeLambdas(): List<CreateNode> = buildList {
        add(CreateNode { state, children, parent -> G10Node.PNode.RootNode(state, children, parent) })
        add(CreateNode { state, children, parent -> G10Node.ENode.ExprPlusNode(state, children, parent) })
        add(CreateNode { state, children, parent -> G10Node.ENode.ExprFallNode(state, children, parent) })
        add(CreateNode { state, children, parent -> G10Node.TNode.IndexNode(state, children, parent) })
        add(CreateNode { state, children, parent -> G10Node.TNode.IdentNode(state, children, parent) })
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

internal sealed class G10Node(
    state: Int,
    children: List<Node>,
    parent: Node?,
) : Node(state, children, parent) {
    internal sealed class PNode(
        state: Int,
        children: List<Node>,
        parent: Node?,
    ) : G10Node(state, children, parent) {
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
    ) : G10Node(state, children, parent) {
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
    ) : G10Node(state, children, parent) {
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
}
