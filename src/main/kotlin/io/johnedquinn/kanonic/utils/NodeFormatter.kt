package io.johnedquinn.kanonic.utils

import io.johnedquinn.kanonic.parse.Node

internal object NodeFormatter {

    internal fun format(node: Node): String = format(node, 0)

    private fun format(node: Node, indent: Int): String = buildString {
        repeat(indent) {
            append("  ")
        }
        val parentString = when (node.parent) {
            null -> "null"
            else -> node.parent!!::class.simpleName
        }
        appendLine("- ($indent) ${node::class.simpleName}; State: ${node.state}; Parent: $parentString")
        node.children.forEach { child ->
            append(format(child, indent + 1))
        }
    }
}