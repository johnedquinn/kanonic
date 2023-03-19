package io.johnedquinn.tests.simple

import io.johnedquinn.kanonic.parse.Node
import kotlin.Int
import kotlin.collections.List

internal sealed class SimpleNode(
    state: Int,
    children: List<Node>,
    parent: Node?,
) : Node(state, children, parent) {
    internal sealed class pNode(
        state: Int,
        children: List<Node>,
        parent: Node?,
    ) : SimpleNode(state, children, parent) {
        internal class RootNode(
            state: Int,
            children: List<Node>,
            parent: Node?,
        ) : pNode(state, children, parent)
    }

    internal sealed class eNode(
        state: Int,
        children: List<Node>,
        parent: Node?,
    ) : SimpleNode(state, children, parent) {
        internal class ExprPlusNode(
            state: Int,
            children: List<Node>,
            parent: Node?,
        ) : eNode(state, children, parent)

        internal class ExprFallNode(
            state: Int,
            children: List<Node>,
            parent: Node?,
        ) : eNode(state, children, parent)
    }

    internal sealed class tNode(
        state: Int,
        children: List<Node>,
        parent: Node?,
    ) : SimpleNode(state, children, parent) {
        internal class IndexNode(
            state: Int,
            children: List<Node>,
            parent: Node?,
        ) : tNode(state, children, parent)

        internal class IdentNode(
            state: Int,
            children: List<Node>,
            parent: Node?,
        ) : tNode(state, children, parent)
    }
}
