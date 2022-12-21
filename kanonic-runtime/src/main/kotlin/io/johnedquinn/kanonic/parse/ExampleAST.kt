package io.johnedquinn.kanonic.parse

internal class ExampleAST {

    internal abstract class PNode(state: Int, children: List<Node>, parent: Node?) : Node(state, children, parent) {
        internal data class RootNode(override val state: Int, override val children: List<Node>, override var parent: Node?)
            : PNode(state, children, parent)
    }

    internal abstract class ENode(state: Int, children: List<Node>, parent: Node?) : Node(state, children, parent) {
        internal data class ExprPlusNode(override val state: Int, override val children: List<Node>, override var parent: Node?) : ENode(state, children, parent)
        internal data class ExprFallNode(override val state: Int, override val children: List<Node>, override var parent: Node?) : ENode(state, children, parent)
    }

    internal abstract class TNode(state: Int, children: List<Node>, parent: Node?) : Node(state, children, parent) {
        internal data class IndexNode(override val state: Int, override val children: List<Node>, override var parent: Node?) : TNode(state, children, parent)
        internal data class IdentNode(override val state: Int, override val children: List<Node>, override var parent: Node?) : TNode(state, children, parent)
    }
}
