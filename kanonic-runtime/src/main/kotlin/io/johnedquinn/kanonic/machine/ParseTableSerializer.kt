package io.johnedquinn.kanonic.machine

object ParseTableSerializer {
    public fun serialize(table: ParseTable): String = buildString {
        table.actionTable.forEachIndexed { index, row ->
            val finalRow = row + table.goToTable[index]
            val stringified = finalRow.map { action ->
                when (action) {
                    null -> ""
                    is AcceptAction -> "A${action.type}"
                    is ReduceAction -> "R${action.rule}"
                    is ShiftAction -> "S${action.state}"
                }
            }
            val string = stringified.joinToString(",")
            when (table.actionTable.lastIndex == index) {
                true -> append(string)
                false -> appendLine(string)
            }
        }
    }

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
            "A" -> AcceptAction(value.substring(1).toInt())
            "S" -> ShiftAction(value.substring(1).toInt())
            "R" -> ReduceAction(value.substring(1).toInt())
            else -> error("Could not parse action: \"$value\"")
        }
    }
}
