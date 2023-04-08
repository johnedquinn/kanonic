package io.johnedquinn.kanonic.runtime.parse.impl

import io.johnedquinn.kanonic.runtime.parse.Action
import io.johnedquinn.kanonic.runtime.parse.ParseTable

internal object ParseTableDeserializer {
    public fun deserialize(value: String, numTerminals: Int): ParseTable {
        val actionTable = mutableListOf<List<Action?>>()
        val goToTable = mutableListOf<List<Action?>>()
        value.lines().forEach { line ->
            val finalRow = line.split(',').map { convertToAction(it.trim()) }
            val actionRow = finalRow.subList(0, numTerminals)
            actionTable.add(actionRow)
            val goToRow = finalRow.subList(numTerminals, finalRow.size)
            goToTable.add(goToRow)
        }
        return ParseTable(actionTable, goToTable)
    }

    private fun convertToAction(value: String): Action? {
        if (value.isEmpty()) return null
        return when (value[0].uppercase()) {
            "A" -> Action.Accept(value.substring(1).toInt())
            "S" -> Action.Shift(value.substring(1).toInt())
            "R" -> Action.Reduce(value.substring(1).toInt())
            else -> error("Could not parse action: \"$value\"")
        }
    }
}
