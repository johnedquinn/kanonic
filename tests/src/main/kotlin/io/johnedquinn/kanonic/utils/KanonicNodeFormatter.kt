package io.johnedquinn.kanonic.utils

import io.johnedquinn.kanonic.generated.KanonicBaseVisitor
import io.johnedquinn.kanonic.generated.KanonicNode
import io.johnedquinn.kanonic.parse.Node
import io.johnedquinn.kanonic.parse.TerminalNode

class KanonicNodeFormatter : KanonicBaseVisitor<Unit, KanonicNodeFormatter.Context>() {
    data class Context(
        var level: Int = 0,
        val builder: StringBuilder = StringBuilder()
    )

    public fun format(node: Node): String {
        val ctx = Context()
        visit(node, ctx)
        return ctx.builder.toString()
    }

    private fun getPrefix(level: Int): String {
        return buildString {
            for (i in 0..level) {
                append("-")
            }
            append(" ")
        }
    }

    override fun defaultVisit(node: KanonicNode, ctx: Context) {
        val top = defaultReturn(node, ctx)
        for (child in node.children) {
            ctx.level++
            child.accept(this, ctx)
            ctx.level--
        }
        return top
    }

    override fun defaultReturn(node: KanonicNode, ctx: Context) {
        val content = "${getPrefix(ctx.level)}${node::class.simpleName}"
        ctx.builder.appendLine(content)
    }

    override fun visitTerminal(node: TerminalNode, ctx: Context) {
        val content = "${getPrefix(ctx.level)}${node::class.simpleName}"
        ctx.builder.appendLine(content)
    }
}
