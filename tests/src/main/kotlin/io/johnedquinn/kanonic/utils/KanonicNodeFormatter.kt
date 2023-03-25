package io.johnedquinn.kanonic.utils

import io.johnedquinn.kanonic.generated.KanonicBaseVisitor
import io.johnedquinn.kanonic.generated.KanonicNode
import io.johnedquinn.kanonic.parse.TerminalNode

class KanonicNodeFormatter : KanonicBaseVisitor<Unit, KanonicNodeFormatter.Context>() {
    data class Context(
        val level: Int = 0,
        val builder: StringBuilder = StringBuilder()
    )

    public fun format(node: KanonicNode): String {
        val ctx = Context()
        visit(node, ctx)
        return ctx.builder.toString()
    }

    override fun defaultVisit(node: KanonicNode, ctx: Context) {
        val content = "${getPrefix(ctx.level)}${node::class.simpleName}"
        ctx.builder.appendLine(content)
        node.children.forEach { child ->
            val newContext = ctx.copy(level = ctx.level + 1)
            when (child) {
                is KanonicNode -> ctx.builder.appendLine(visit(child, newContext))
                is TerminalNode -> ctx.builder.appendLine("${getPrefix(newContext.level)}${child::class.simpleName}")
                else -> error("Found a node that isn't a valid node.")
            }
        }
    }

    private fun getPrefix(level: Int): String {
        return buildString {
            for (i in 0..level) {
                append("-")
            }
            append(" ")
        }
    }
}
