package io.johnedquinn.kanonic.runtime.parse

import de.m3y.kformat.table
import io.johnedquinn.kanonic.runtime.grammar.Grammar
import kotlin.text.StringBuilder

data class ParseTable(
    val actionTable: Array<Array<Action?>>,
    val goToTable: Array<Array<Action?>>,
) {
    public constructor(actionTable: List<List<Action?>>, goToTable: List<List<Action?>>) : this(
        actionTable.map { it.toTypedArray() }.toTypedArray(),
        goToTable.map { it.toTypedArray() }.toTypedArray()
    )

    internal fun prettify(grammar: Grammar): String {
        val nonTerminals = grammar.rules.map { it.name }
        val table = table {
            val headers = mutableListOf<String>()
            headers.add("STATE")
            grammar.tokens.forEach { token -> headers.add(token.name) }
            nonTerminals.forEach { nonTerminal -> headers.add(nonTerminal) }
            header(headers)

            actionTable.forEachIndexed { rowIndex, actionRow ->
                val rowValues = mutableListOf<String>()
                rowValues.add(rowIndex.toString())
                actionRow.forEach { action -> rowValues.add(action?.prettify() ?: "null") }
                goToTable[rowIndex].forEach { action -> rowValues.add(action?.prettify() ?: "null") }
                row(*rowValues.toTypedArray())
            }
        }
        val builder = StringBuilder()
        table.render(builder)
        return builder.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParseTable

        if (!actionTable.contentDeepEquals(other.actionTable)) return false
        if (!goToTable.contentDeepEquals(other.goToTable)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = actionTable.contentDeepHashCode()
        result = 31 * result + goToTable.contentDeepHashCode()
        return result
    }
}
