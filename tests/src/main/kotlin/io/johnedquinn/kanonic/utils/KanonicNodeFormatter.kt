package io.johnedquinn.kanonic.utils

import io.johnedquinn.kanonic.parse.Node
import io.johnedquinn.kanonic.parse.TerminalNode
import io.johnedquinn.kanonic.runtime.generated.KanonicBaseVisitor
import io.johnedquinn.kanonic.runtime.generated.KanonicNode

object KanonicNodeFormatter : KanonicBaseVisitor<Unit, KanonicNodeFormatter.Context>() {

    val EOL = System.lineSeparator()
    private const val INDENTS = "   "
    private const val INDENT_ROOT = "⚬"
    private const val INDENT_PIPE = "──"
    private const val INDENT_T = "├$INDENT_PIPE"
    private const val INDENT_I = "│  "
    private const val INDENT_ELBOW = "└$INDENT_PIPE"

    data class Context(
        var level: Int = 0,
        val builder: StringBuilder = StringBuilder()
    )

    public fun format(node: Node): String {
        val ctx = Context()
        return formatSupport(node)
        // visit(node, ctx)
        // return ctx.builder.toString()
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
        defaultReturn(node, ctx)
        for (child in node.children) {
            ctx.level++
            child.accept(this, ctx)
            ctx.level--
        }
    }

    override fun defaultReturn(node: KanonicNode, ctx: Context) {
        val content = "${getLead(ctx.level)}${node::class.simpleName}"
        ctx.builder.appendLine(content)
    }

    override fun visitTerminal(node: TerminalNode, ctx: Context) {
        val content = "${getLead(ctx.level)}TOKEN: ${node.token.content}"
        ctx.builder.appendLine(content)
    }

    private data class PropertyInfo(val item: Any?, val param: String? = null)

    internal fun formatSupport(
        input: Any?,
        level: Int = 0,
        levels: Set<Int> = emptySet(),
        isLast: Boolean = true,
        param: String? = null,
        lineSeparator: String = EOL
    ): String = buildString {
        // Build Current Level String
        val name = when (input) {
            null -> "null"
            is TerminalNode -> "Token: \"${input.token}\""
            else -> input::class.simpleName.toString()
        }
        append(getLead(level, levels, isLast))
        append(' ')
        if (param != null) {
            append(param)
            append(": ")
        }
        append(name)
        append(lineSeparator)

        // Create Children (PropertyInfo)
        val levelsForChildren: MutableSet<Int> = HashSet(levels)
        if (!isLast) { levelsForChildren.add(level - 1) }
        val children = when (input) {
            null -> emptyList()
            is Node -> input.children.mapIndexed { index, child -> PropertyInfo(child, index.toString()) }
            else -> emptyList()
        }

        // Add Children Strings
        children.forEachIndexed { index, child ->
            append(
                formatSupport(
                    child.item,
                    level + 1,
                    levelsForChildren,
                    index == children.size - 1,
                    child.param,
                    lineSeparator
                )
            )
        }
    }

    /**
     * Returns the prefix of a level.
     */
    private fun getLead(level: Int, levels: Set<Int> = emptySet(), useElbow: Boolean = true): String {
        if (level == 0) return INDENT_ROOT
        return buildString {
            for (l in 0 until level - 1) {
                val indent = when (levels.contains(l)) {
                    true -> INDENT_I
                    false -> INDENTS
                }
                append(indent)
            }
            val elbow = when (useElbow) {
                true -> INDENT_ELBOW
                false -> INDENT_T
            }
            append(elbow)
        }
    }
}
