package io.johnedquinn.kanonic.parse

internal class ExampleAST {

    internal abstract class PNode(state: Int, children: List<Node>, parent: Node?) : Node(state, children, parent) {
        internal class RootNode(state: Int, children: List<Node>, parent: Node?) : PNode(state, children, parent)
    }

    internal abstract class ENode(state: Int, children: List<Node>, parent: Node?) : Node(state, children, parent) {
        internal class ExprPlusNode(state: Int, children: List<Node>, parent: Node?) : ENode(state, children, parent)
        internal class ExprFallNode(state: Int, children: List<Node>, parent: Node?) : ENode(state, children, parent)
    }

    internal abstract class TNode(state: Int, children: List<Node>, parent: Node?) : Node(state, children, parent) {
        internal class IndexNode(state: Int, children: List<Node>, parent: Node?) : TNode(state, children, parent)
        internal class IdentNode(state: Int, children: List<Node>, parent: Node?) : TNode(state, children, parent)
    }

    internal class RuleToNodeMapper {
        internal fun map(index: Int) = nodeList[index]

        private val nodeList: List<(Int, List<Node>, Node?) -> Node> = listOf(
            { state: Int, children: List<Node>, parent: Node? ->
                PNode.RootNode(state, children, parent)
            },
            { state: Int, children: List<Node>, parent: Node? ->
                ENode.ExprPlusNode(state, children, parent)
            },
            { state: Int, children: List<Node>, parent: Node? ->
                ENode.ExprFallNode(state, children, parent)
            },
            { state: Int, children: List<Node>, parent: Node? ->
                TNode.IndexNode(state, children, parent)
            },
            { state: Int, children: List<Node>, parent: Node? ->
                TNode.IdentNode(state, children, parent)
            }
        )
    }
}
