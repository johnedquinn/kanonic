package io.johnedquinn.kanonic.gen.impl

import io.johnedquinn.kanonic.runtime.parse.Action
import io.johnedquinn.kanonic.runtime.parse.ParseTable

internal object ParseTableSerializer {
    public fun serialize(table: ParseTable): String = buildString {
        table.actionTable.forEachIndexed { index, row ->
            val finalRow = row + table.goToTable[index]
            val stringified = finalRow.map { action ->
                when (action) {
                    null -> ""
                    is Action.Accept -> "A${action.type}"
                    is Action.Reduce -> "R${action.rule}"
                    is Action.Shift -> "S${action.state}"
                }
            }
            val string = stringified.joinToString(",")
            when (table.actionTable.lastIndex == index) {
                true -> append(string)
                false -> appendLine(string)
            }
        }
    }
}
