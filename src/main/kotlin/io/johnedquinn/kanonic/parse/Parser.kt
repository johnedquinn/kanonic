package io.johnedquinn.kanonic.parse

import io.johnedquinn.kanonic.Grammar
import io.johnedquinn.kanonic.SymbolReference
import io.johnedquinn.kanonic.machine.ParseTable

class Parser(val grammar: Grammar, val table: ParseTable) {
    internal fun parse(tokens: List<SymbolReference>) {
        TODO()
    }
}