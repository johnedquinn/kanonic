package io.johnedquinn.kanonic.runtime.grammar

import io.johnedquinn.kanonic.runtime.parse.TokenLiteral

data class RuleVariant(
    var name: String,
    val parentName: String,
    val items: List<SymbolReference>,
) {
    val normalizedSize = items.filterNot {
        it is TerminalReference && it.type == TokenLiteral.ReservedTypes.EPSILON
    }.size
}
